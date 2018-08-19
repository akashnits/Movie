package com.example.android.popularmovies

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.BaseColumns._ID
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar

import com.example.android.popularmovies.adapters.MoviesCursorAdapter
import com.example.android.popularmovies.data.MovieContract
import com.example.android.popularmovies.model.Movies

import butterknife.BindView
import butterknife.ButterKnife

class FavoritesActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor>, MoviesCursorAdapter.OnGridItemClickHandler {
  private var mMoviesCursorAdapter: MoviesCursorAdapter? = null
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

    val TAG = FavoritesActivity::class.java.simpleName

    val LOADER_FAV_MOVIES_ID = 183
  }

  @BindView(R.id.recyclerview_fav_movies)
  @JvmField var recyclerviewFavmovies: RecyclerView? = null
  @BindView(R.id.pb_loading_indicator_fav)
  @JvmField var pbLoadingIndicatorFav: ProgressBar? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_favorites)
    ButterKnife.bind(this)

    val actionBar = supportActionBar
    actionBar?.setDisplayHomeAsUpEnabled(true)

    mMoviesCursorAdapter = MoviesCursorAdapter(this, this)
    val staggeredGridLayoutManager =
      StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    recyclerviewFavmovies!!.layoutManager = staggeredGridLayoutManager
    recyclerviewFavmovies!!.adapter = mMoviesCursorAdapter
    recyclerviewFavmovies!!.hasFixedSize()

    val loaderManager = supportLoaderManager
    loaderManager.initLoader(LOADER_FAV_MOVIES_ID, null, this)
  }

  override fun onCreateLoader(
    id: Int,
    args: Bundle?
  ): Loader<Cursor> {

    when (id) {
      LOADER_FAV_MOVIES_ID -> {
        pbLoadingIndicatorFav!!.visibility = View.VISIBLE
        val sortOrder = _ID + " ASC"
        return CursorLoader(
            this,
            MovieContract.MovieEntry.CONTENT_URI,
            MOVIES_PROJECTION, null, null,
            sortOrder
        )
      }
      else -> throw RuntimeException("Loader not implemented: $LOADER_FAV_MOVIES_ID")
    }
  }

  override fun onLoadFinished(
    loader: Loader<Cursor>,
    data: Cursor
  ) {
    mMoviesCursorAdapter!!.swapCursor(data)
    pbLoadingIndicatorFav!!.visibility = View.INVISIBLE
  }

  override fun onLoaderReset(loader: Loader<Cursor>) {

  }

  override fun onGridItemClickListener(movieId: Int) {
    val uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon()
        .appendPath(movieId.toString())
        .build()
    Log.v(TAG, "Uri for single movie: " + uri.toString())

    val cursor = contentResolver.query(
        uri,
        MOVIES_PROJECTION, null, null, null
    )
    if (cursor != null) {
      cursor.moveToNext()
      val movie = Movies(
          cursor.getInt(INDEX_MOVIE_ID), cursor.getString(INDEX_IMAGE_URL),
          cursor.getString(INDEX_DATE),
          cursor.getString(INDEX_RATINGS), cursor.getString(INDEX_MOVIE_TITLE),
          cursor.getString(INDEX_OVERVIEW)
      )
      val movieDetailsIntent = Intent(this, MovieDetailActivity::class.java)
      movieDetailsIntent.putExtra(getString(R.string.MovieDataDetails), movie)
      startActivity(movieDetailsIntent)
      cursor.close()
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> onBackPressed()
      else -> {
      }
    }
    return super.onOptionsItemSelected(item)
  }
}
