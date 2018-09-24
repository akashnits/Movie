package com.example.android.popularmovies.contract

import android.support.v4.app.FragmentManager
import com.example.android.popularmovies.BasePresenter
import com.example.android.popularmovies.BaseView
import com.example.android.popularmovies.fragments.MovieDetailFragment
import io.reactivex.disposables.CompositeDisposable

class MovieDetailContract {
  interface View : BaseView<Presenter> {

    fun updateTrailersAdapter(trailerUrlArray: Array<String?>)

    fun showError(error: String?)

    fun showTrailer(key: String?)
  }

  interface Presenter : BasePresenter {

    fun setUpViewModel(
      fragment: MovieDetailFragment,
      movieId: Int
    )

    fun bindDataTrailersAdapter(strings: Array<String?>)

    fun loadError(error: String?)

    fun setUpDisposable(disposable: CompositeDisposable?)

    fun onTrailerClicked(supportFragmentManager: FragmentManager?, position: Int)
  }
}