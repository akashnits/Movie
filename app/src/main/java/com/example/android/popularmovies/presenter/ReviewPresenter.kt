package com.example.android.popularmovies.presenter

import com.example.android.popularmovies.contract.ReviewContract
import com.example.android.popularmovies.contract.ReviewContract.View
import com.example.android.popularmovies.interactor.ReviewInteractor
import com.example.android.popularmovies.model.ReviewItem
import io.reactivex.disposables.CompositeDisposable

class ReviewPresenter : ReviewContract.Presenter {
  var mReviewView: ReviewContract.View?= null
  var mReviewInteractor: ReviewInteractor?= null

  constructor(mReviewView: View?) {
    this.mReviewView = mReviewView
    mReviewView?.setPresenter(this)

    mReviewInteractor= ReviewInteractor(this)
  }

  override fun loadError(error: String?) {
    mReviewView?.showError(error)
  }

  override fun start() {

  }

  override fun bindDataReviewAdapter(reviewList: List<ReviewItem>) {
   mReviewView?.updateReviewAdapter(reviewList)
  }

  override fun setUpDisposable(disposable: CompositeDisposable?) {
    mReviewInteractor?.setDisposable(disposable)
  }

  override fun loadMovieReviews(movieId: Int) {
    mReviewInteractor?.getMovieReview(movieId)
  }
}