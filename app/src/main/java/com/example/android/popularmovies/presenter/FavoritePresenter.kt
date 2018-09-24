package com.example.android.popularmovies.presenter

import android.content.Context
import android.database.Cursor
import android.support.v4.app.FragmentManager
import android.support.v4.app.LoaderManager
import com.example.android.popularmovies.R
import com.example.android.popularmovies.contract.FavoriteContract
import com.example.android.popularmovies.contract.FavoriteContract.View
import com.example.android.popularmovies.data.MovieContract
import com.example.android.popularmovies.fragments.MovieDetailFragment
import com.example.android.popularmovies.interactor.FavoriteInteractor
import com.example.android.popularmovies.model.Movies

class FavoritePresenter : FavoriteContract.Presenter {

  private var mFavoriteView: FavoriteContract.View?= null
  private var mFavoriteInteractor: FavoriteInteractor?= null
  private var mContext: Context?= null

  companion object {

    val MOVIES_PROJECTION = arrayOf(
        MovieContract.MovieEntry.COLUMN_MOVIE_ID, MovieContract.MovieEntry.COULMN_IMAGE_URL,
        MovieContract.MovieEntry.COLUMN_DATE, MovieContract.MovieEntry.COLUMN_RATINGS,
        MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, MovieContract.MovieEntry.COLUMN_OVERVIEW
    )

    val INDEX_MOVIE_ID = 0
    val INDEX_IMAGE_URL = 1
    val INDEX_DATE = 2
    val INDEX_RATINGS = 3
    val INDEX_MOVIE_TITLE = 4
    val INDEX_OVERVIEW = 5
  }

  constructor(mFavoriteView: View?, context: Context?) {
    this.mFavoriteView = mFavoriteView
    mFavoriteView?.setPresenter(this)
    mContext= context

    mFavoriteInteractor= FavoriteInteractor(this, context)
  }

  override fun start() {
  }

  override fun bindDataMovieCursorAdapter(cursor: Cursor) {
    mFavoriteView?.updateMovieCursorAdapter(cursor)
  }

  override fun startLoader(
    loaderId: Int,
    loaderManager: LoaderManager?
  ) {
    mFavoriteInteractor?.initLoader(loaderId, loaderManager)
  }

  override fun onItemClicked(
    supportFragmentManager: FragmentManager?,
    movieId: Int?
  ) {
    val uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon()
        .appendPath(movieId.toString())
        .build()

    val cursor = mContext?.contentResolver?.query(
        uri,
        MOVIES_PROJECTION, null, null, null
    )
    if (cursor != null) {
      cursor.moveToNext()
      val movie = Movies(
          cursor.getInt(INDEX_MOVIE_ID), cursor.getString(
          INDEX_IMAGE_URL
      ),
          cursor.getString(INDEX_DATE),
          cursor.getString(INDEX_RATINGS), cursor.getString(
          INDEX_MOVIE_TITLE
      ),
          cursor.getString(INDEX_OVERVIEW)
      )
      supportFragmentManager!!
          .beginTransaction()
          .replace(R.id.container, MovieDetailFragment.newInstance(movie), "Movie detail")
          .addToBackStack(null)
          .commit();
      cursor.close()
    }
  }
}