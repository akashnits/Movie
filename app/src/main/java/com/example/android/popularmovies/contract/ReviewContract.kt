package com.example.android.popularmovies.contract

import com.example.android.popularmovies.BasePresenter
import com.example.android.popularmovies.BaseView
import com.example.android.popularmovies.model.ReviewItem
import io.reactivex.disposables.CompositeDisposable

class ReviewContract {

  interface View : BaseView<Presenter> {

    fun showError(error: String?)

    fun updateReviewAdapter(reviewList: List<ReviewItem>)

  }

  interface Presenter : BasePresenter {

    fun loadError(error: String?)

    fun bindDataReviewAdapter(reviewList: List<ReviewItem>)

    fun setUpDisposable(disposable: CompositeDisposable?)

    fun loadMovieReviews(movieId: Int)
  }
}