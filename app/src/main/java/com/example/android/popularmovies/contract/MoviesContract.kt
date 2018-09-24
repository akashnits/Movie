package com.example.android.popularmovies.contract

import android.support.v4.app.FragmentManager
import com.example.android.popularmovies.BasePresenter
import com.example.android.popularmovies.BaseView
import com.example.android.popularmovies.fragments.MoviesFragment
import io.reactivex.disposables.CompositeDisposable

class MoviesContract {
  interface View : BaseView<Presenter> {

    fun updateMoviesAdapter(movieUrlArray : Array<String?>)

    fun showPopularMovies()

    fun showTopRatedMovies()

    fun showError(error: String?)
  }

  interface Presenter : BasePresenter {

    fun loadPopularMovies()

    fun loadTopRatedMovies()

    fun setUpViewModel(fragment: MoviesFragment)

    fun bindDataMoviesAdapter(strings: Array<String?>)

    fun loadError(error: String?)

    fun setUpDisposable(disposable: CompositeDisposable?)

    fun onItemClicked(
      supportFragmentManager: FragmentManager?,
      position: Int
    )
  }
}
