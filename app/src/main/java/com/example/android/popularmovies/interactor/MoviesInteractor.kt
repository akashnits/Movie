package com.example.android.popularmovies.interactor

import com.example.android.popularmovies.data.MoviesViewModel
import com.example.android.popularmovies.model.Response
import com.example.android.popularmovies.networkUtils.ApiClient
import com.example.android.popularmovies.networkUtils.ApiService
import com.example.android.popularmovies.networkUtils.NetworkUtils
import com.example.android.popularmovies.presenter.MoviesPresenter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MoviesInteractor {
  private var mMoviesPresenter: MoviesPresenter? = null;
  private var apiService: ApiService? = null
  private var disposable: CompositeDisposable?= null

  constructor(mMoviesPresenter: MoviesPresenter?) {
    this.mMoviesPresenter = mMoviesPresenter
    apiService = ApiClient.client
        .create<ApiService>(ApiService::class.java)
  }

   fun getPopularMovies(moviesViewModel: MoviesViewModel?) {
    disposable?.add(apiService!!
        .getPopularMovies(
            NetworkUtils.key, NetworkUtils.language)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .flatMap { movie ->
          moviesViewModel!!.response = movie
          getImageUrlObservable(movie)
        }
        .subscribeWith(object : DisposableSingleObserver<Array<String?>>() {
          override fun onSuccess(strings: Array<String?>) {
            mMoviesPresenter?.bindDataMoviesAdapter(strings)
          }

          override fun onError(e: Throwable) {
            mMoviesPresenter?.loadError(e.message)
          }
        })
    )
  }

   fun getTopRatedMovies(moviesViewModel: MoviesViewModel?) {
    disposable?.add(apiService!!
        .getTopRatedMovies(
            NetworkUtils.key, NetworkUtils.language)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .flatMap { movie ->
          moviesViewModel?.response = movie
          getImageUrlObservable(movie)
        }
        .subscribeWith(object : DisposableSingleObserver<Array<String?>>() {
          override fun onSuccess(strings: Array<String?>) {
            mMoviesPresenter?.bindDataMoviesAdapter(strings)
          }

          override fun onError(e: Throwable) {
            mMoviesPresenter?.loadError(e.message)
          }
        })
    )
  }

   private fun getImageUrlObservable(response: Response): Single<Array<String?>> {
    val moviesImageUrl = getImageUrls(response)

    return Single.create { emitter ->
      if (!emitter.isDisposed) {
        emitter.onSuccess(moviesImageUrl)
      }
    }
  }

   fun getImageUrls(response: Response?): Array<String?> {
    val moviesImageUrl = arrayOfNulls<String>(response?.results!!.size)

    for (i in 0 until response.results!!.size) {
      moviesImageUrl[i] = response.results!![i].posterPath
    }
    return moviesImageUrl
  }

  fun setDisposable(d: CompositeDisposable?){
    disposable= d
  }
}
