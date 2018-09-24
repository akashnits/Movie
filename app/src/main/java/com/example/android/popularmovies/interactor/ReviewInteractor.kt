package com.example.android.popularmovies.interactor

import android.widget.Toast
import com.example.android.popularmovies.contract.ReviewContract
import com.example.android.popularmovies.contract.ReviewContract.Presenter
import com.example.android.popularmovies.model.ReviewItem
import com.example.android.popularmovies.networkUtils.ApiClient
import com.example.android.popularmovies.networkUtils.ApiService
import com.example.android.popularmovies.networkUtils.NetworkUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ReviewInteractor {

  private var mReviewPresenter: ReviewContract.Presenter?= null
  private var apiService: ApiService?= null
  private var disposable: CompositeDisposable?= null

  constructor(mReviewPresenter: Presenter?) {
    this.mReviewPresenter = mReviewPresenter
    apiService = ApiClient.client
        .create<ApiService>(ApiService::class.java)
  }

   fun getMovieReview(movieId: Int) {
    disposable?.add(apiService!!
        .getMovieReviews(movieId, NetworkUtils.key, NetworkUtils.language)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .map { review -> review.results }
        .subscribeWith(object : DisposableSingleObserver<List<ReviewItem>>() {
          override fun onSuccess(reviewItems: List<ReviewItem>) {
            mReviewPresenter?.bindDataReviewAdapter(reviewItems)
          }

          override fun onError(e: Throwable) {
            mReviewPresenter?.loadError(e.message)
          }
        })
    )
  }

  fun setDisposable(d: CompositeDisposable?){
    disposable= d
  }
}