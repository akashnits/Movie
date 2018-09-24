package com.example.android.popularmovies.presenter

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.support.v4.app.FragmentManager
import com.example.android.popularmovies.contract.MovieDetailContract
import com.example.android.popularmovies.contract.MovieDetailContract.View
import com.example.android.popularmovies.data.MovieDetailViewModel
import com.example.android.popularmovies.fragments.MovieDetailFragment
import com.example.android.popularmovies.interactor.MovieDetailInteractor
import io.reactivex.disposables.CompositeDisposable

class MovieDetailPresenter: MovieDetailContract.Presenter{

  private var mMovieDetailView: MovieDetailContract.View?= null
  private var mMovieDetailInteractor: MovieDetailInteractor?= null
  private var mMovieDetailViewModel: MovieDetailViewModel?= null

  constructor(mMovieDetailView: View?) {
    this.mMovieDetailView = mMovieDetailView
    mMovieDetailView?.setPresenter(this)

    mMovieDetailInteractor= MovieDetailInteractor(this)
  }

  override fun start() {
  }

  override fun setUpViewModel(
    fragment: MovieDetailFragment,
    movieId: Int
  ) {
    mMovieDetailViewModel = ViewModelProviders.of(fragment)
        .get(MovieDetailViewModel::class.java)
    val movieDetailViewModel= mMovieDetailViewModel
    if (mMovieDetailViewModel!!.trailer == null)
      mMovieDetailInteractor?.getMovieTrailers(movieDetailViewModel, movieId)
    else {
      mMovieDetailView?.updateTrailersAdapter(mMovieDetailInteractor!!.getTrailerNames(mMovieDetailViewModel?.trailer!!))
    }
  }

  override fun bindDataTrailersAdapter(strings: Array<String?>) {
    mMovieDetailView?.updateTrailersAdapter(strings)
  }

  override fun loadError(error: String?) {
    mMovieDetailView?.showError(error)
  }

  override fun setUpDisposable(disposable: CompositeDisposable?) {
    mMovieDetailInteractor?.setDisposable(disposable)
  }

  override fun onTrailerClicked(
    supportFragmentManager: FragmentManager?,
    position: Int
  ) {

    var key = mMovieDetailViewModel!!.trailer!!.results!![position].key
    mMovieDetailView?.showTrailer(key)
    }
  }