package com.example.android.popularmovies.presenter

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentManager
import com.example.android.popularmovies.R
import com.example.android.popularmovies.contract.MoviesContract
import com.example.android.popularmovies.contract.MoviesContract.View
import com.example.android.popularmovies.data.MoviesViewModel
import com.example.android.popularmovies.fragments.MovieDetailFragment
import com.example.android.popularmovies.fragments.MoviesFragment
import com.example.android.popularmovies.interactor.MoviesInteractor
import io.reactivex.disposables.CompositeDisposable

class MoviesPresenter : MoviesContract.Presenter{
  var mMoviesView: MoviesContract.View? =null
  var mMoviesInteractor: MoviesInteractor? = null
  var mMoviesViewModel: MoviesViewModel?= null

  constructor(mMoviesView: View?) {
    this.mMoviesView = mMoviesView
    mMoviesView?.setPresenter(this)

    this.mMoviesInteractor= MoviesInteractor(this)
  }

  override fun start() {

  }

  override fun setUpViewModel(fragment: MoviesFragment) {
    mMoviesViewModel = ViewModelProviders.of(fragment)
        .get(MoviesViewModel::class.java)
    val moviesViewModel = mMoviesViewModel
    if (mMoviesViewModel!!.response == null) {
      mMoviesInteractor?.getPopularMovies(moviesViewModel)
    } else {
      val movieImageUrls = mMoviesInteractor!!.getImageUrls(mMoviesViewModel!!.response)
      mMoviesView?.updateMoviesAdapter(movieImageUrls)
    }
  }

  override fun bindDataMoviesAdapter(strings: Array<String?>) {
    mMoviesView?.updateMoviesAdapter(strings)
  }

  override fun loadPopularMovies() {
    mMoviesInteractor?.getPopularMovies(mMoviesViewModel)
  }

  override fun loadTopRatedMovies() {
    mMoviesInteractor?.getTopRatedMovies(mMoviesViewModel)
  }

  override fun loadError(error: String?) {
    mMoviesView?.showError(error)
  }

  override fun setUpDisposable(disposable: CompositeDisposable?) {
    mMoviesInteractor?.setDisposable(disposable)
  }

  override fun onItemClicked(
    supportFragmentManager: FragmentManager?,
    position: Int
  ) {
    val movie = mMoviesViewModel?.response?.results!![position]
    supportFragmentManager!!
        .beginTransaction()
        .replace(R.id.container, MovieDetailFragment.newInstance(movie), "Movie details")
        .addToBackStack(null)
        .commit()
  }
}
