package com.example.android.popularmovies.interactor

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.BaseColumns
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import com.example.android.popularmovies.data.MovieContract.MovieEntry
import com.example.android.popularmovies.fragments.FavoriteFragment
import com.example.android.popularmovies.fragments.FavoriteFragment.Companion
import com.example.android.popularmovies.presenter.FavoritePresenter
import java.sql.RowId

class FavoriteInteractor : LoaderManager.LoaderCallbacks<Cursor>{
  private var mFavoritePresenter: FavoritePresenter?= null
  private var mContext: Context?= null

  constructor(mFavoritePresenter: FavoritePresenter?, context: Context?) {
    this.mFavoritePresenter = mFavoritePresenter

    mContext= context
  }

  override fun onCreateLoader(
    id: Int,
    args: Bundle?
  ): Loader<Cursor> {

    when (id) {
      FavoriteFragment.LOADER_FAV_MOVIES_ID -> {
        val sortOrder = BaseColumns._ID + " ASC"
        return CursorLoader(
            mContext!!,
            MovieEntry.CONTENT_URI,
            FavoritePresenter.MOVIES_PROJECTION, null, null,
            sortOrder
        )
      }
      else -> throw RuntimeException("Loader not implemented: ${FavoriteFragment.LOADER_FAV_MOVIES_ID}")
    }
  }

  override fun onLoadFinished(
    loader: Loader<Cursor>,
    data: Cursor
  ) {
    mFavoritePresenter?.bindDataMovieCursorAdapter(data)
  }

  override fun onLoaderReset(loader: Loader<Cursor>) {

  }

  fun initLoader(loaderId: Int, loaderManager: LoaderManager?){
    loaderManager?.initLoader(
        loaderId, null, this)
  }
}