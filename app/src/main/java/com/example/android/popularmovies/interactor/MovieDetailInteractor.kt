package com.example.android.popularmovies.interactor

import android.widget.Toast
import com.example.android.popularmovies.contract.MovieDetailContract
import com.example.android.popularmovies.contract.MovieDetailContract.Presenter
import com.example.android.popularmovies.data.MovieDetailViewModel
import com.example.android.popularmovies.model.Trailer
import com.example.android.popularmovies.networkUtils.ApiClient
import com.example.android.popularmovies.networkUtils.ApiService
import com.example.android.popularmovies.networkUtils.NetworkUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MovieDetailInteractor {

  private var mMovieDetailPresenter: MovieDetailContract.Presenter?= null
  private var apiService: ApiService?= null
  private var disposable: CompositeDisposable?= null


  constructor(mMovieDetailPresenter: Presenter?) {
    this.mMovieDetailPresenter = mMovieDetailPresenter
    apiService = ApiClient.client
        .create<ApiService>(ApiService::class.java)
  }

   fun getMovieTrailers(movieDetailViewModel: MovieDetailViewModel?, movieId: Int) {
    disposable?.add(apiService!!.getMovieTrailers(movieId, NetworkUtils.key, NetworkUtils.language)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .flatMap { trailer ->
          movieDetailViewModel?.trailer = trailer
          getTrailerNameObservable(trailer)
        }.subscribeWith(object : DisposableSingleObserver<Array<String?>>() {
          override fun onSuccess(strings: Array<String?>) {
            mMovieDetailPresenter?.bindDataTrailersAdapter(strings)
          }

          override fun onError(e: Throwable) {
            mMovieDetailPresenter?.loadError(e.message)
          }
        })
    )
  }

  private fun getTrailerNameObservable(trailer: Trailer): Single<Array<String?>> {
    return Single.create { emitter ->
      if (!emitter.isDisposed)
        emitter.onSuccess(getTrailerNames(trailer))
    }
  }

   fun getTrailerNames(trailer: Trailer): Array<String?> {
    val trailerNames = arrayOfNulls<String>(trailer.results!!.size)
    val trailerItems = trailer.results
    for (i in 0 until trailer.results!!.size) {
      trailerNames[i] = trailerItems!![i].name
    }
    return trailerNames
  }

  fun setDisposable(d: CompositeDisposable?){
    disposable= d
  }
}