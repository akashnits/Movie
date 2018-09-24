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
import com.example.android.popularmovies.contract.FavoriteContract
import com.example.android.popularmovies.contract.FavoriteContract.Presenter
import com.example.android.popularmovies.data.MovieContract
import com.example.android.popularmovies.data.MovieContract.MovieEntry
import com.example.android.popularmovies.model.Movies
import com.example.android.popularmovies.presenter.FavoritePresenter
import kotlinx.android.synthetic.main.fragment_favorite.pbLoadingIndicatorFav
import kotlinx.android.synthetic.main.fragment_favorite.rvFavMovies

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FavoriteFragment : Fragment(), FavoriteContract.View, MoviesCursorAdapter.OnGridItemClickHandler {

  private var mMoviesCursorAdapter: MoviesCursorAdapter? = null
  private var mFavoritePresenter: FavoriteContract.Presenter?= null
  var mContext: Context? = null


  companion object {
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
    mFavoritePresenter= FavoritePresenter(this, mContext)
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
    mFavoritePresenter?.startLoader(LOADER_FAV_MOVIES_ID, loaderManager)
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    mContext= context
  }

  private fun initViews(){
    val staggeredGridLayoutManager =
      StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    rvFavMovies!!.layoutManager = staggeredGridLayoutManager
    rvFavMovies!!.adapter = mMoviesCursorAdapter
  }

  override fun setPresenter(presenter: Presenter?) {
    mFavoritePresenter= presenter
  }

  override fun updateMovieCursorAdapter(cursor: Cursor) {
    mMoviesCursorAdapter!!.swapCursor(cursor)

  }

  override fun onGridItemClickListener(movieId: Int) {
    mFavoritePresenter?.onItemClicked(activity!!.supportFragmentManager, movieId)
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
