package com.example.android.popularmovies.contract

import android.content.Context
import android.database.Cursor
import android.support.v4.app.FragmentManager
import android.support.v4.app.LoaderManager
import com.example.android.popularmovies.BasePresenter
import com.example.android.popularmovies.BaseView

class FavoriteContract {
  interface View : BaseView<Presenter> {

    fun updateMovieCursorAdapter(cursor: Cursor)

  }

  interface Presenter : BasePresenter {

    fun bindDataMovieCursorAdapter(cursor: Cursor)

    fun startLoader(loaderId: Int, loaderManager: LoaderManager?)

    fun onItemClicked(supportFragmentManager: FragmentManager?, movieId: Int?)
  }
}