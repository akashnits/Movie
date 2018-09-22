package com.example.android.popularmovies.fragments

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.BaseColumns
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import com.example.android.popularmovies.R
import com.example.android.popularmovies.adapters.MoviesCursorAdapter
import com.example.android.popularmovies.data.MovieContract
import com.example.android.popularmovies.data.MovieContract.MovieEntry
import com.example.android.popularmovies.model.Movies
import kotlinx.android.synthetic.main.fragment_favorite.pbLoadingIndicatorFav
import kotlinx.android.synthetic.main.fragment_favorite.rvFavMovies

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FavoriteFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor>, MoviesCursorAdapter.OnGridItemClickHandler {

  private var mMoviesCursorAdapter: MoviesCursorAdapter? = null
  var mContext: Context? = null

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

    val LOADER_FAV_MOVIES_ID = 183

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FavoriteFragment.
     */
    @JvmStatic fun newInstance() =
      FavoriteFragment()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mMoviesCursorAdapter = MoviesCursorAdapter(mContext, this)

  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_favorite, container, false)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    initViews()
    loaderManager.initLoader(
        LOADER_FAV_MOVIES_ID, null, this)
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    mContext= context
  }

  private fun initViews(){
    //TODO: Fix toolbar
    val staggeredGridLayoutManager =
      StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    rvFavMovies!!.layoutManager = staggeredGridLayoutManager
    rvFavMovies!!.adapter = mMoviesCursorAdapter
  }

  override fun onCreateLoader(
    id: Int,
    args: Bundle?
  ): Loader<Cursor> {

    when (id) {
      LOADER_FAV_MOVIES_ID -> {
        pbLoadingIndicatorFav!!.visibility = View.VISIBLE
        val sortOrder = BaseColumns._ID + " ASC"
        return CursorLoader(
            mContext!!,
            MovieEntry.CONTENT_URI,
            MOVIES_PROJECTION, null, null,
            sortOrder
        )
      }
      else -> throw RuntimeException("Loader not implemented: ${LOADER_FAV_MOVIES_ID}")
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
      activity?.supportFragmentManager
          ?.beginTransaction()
          ?.replace(R.id.container, MovieDetailFragment.newInstance(movie), "Movie detail")
          ?.addToBackStack(null)
          ?.commit();
      cursor.close()
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> activity?.onBackPressed()
      else -> {
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    loaderManager.destroyLoader(LOADER_FAV_MOVIES_ID)
  }
}
