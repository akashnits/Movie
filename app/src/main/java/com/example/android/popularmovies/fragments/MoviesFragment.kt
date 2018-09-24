package com.example.android.popularmovies.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.android.popularmovies.R
import com.example.android.popularmovies.adapters.MoviesAdapter
import com.example.android.popularmovies.contract.MoviesContract
import com.example.android.popularmovies.contract.MoviesContract.Presenter
import com.example.android.popularmovies.data.MoviesViewModel
import com.example.android.popularmovies.presenter.MoviesPresenter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_movies.recyclerview_movies

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MoviesFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MoviesFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MoviesFragment : Fragment() , MoviesAdapter.onGridItemClickHandler, MoviesContract.View{
  private var mContext: Context?= null
  private var mMoviesAdapter: MoviesAdapter? = null
  private var mMoviesPresenter: MoviesContract.Presenter? =null
  private var disposable: CompositeDisposable = CompositeDisposable()


  companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MoviesFragment.
     */
    @JvmStatic fun newInstance(
    ) =
      MoviesFragment()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mMoviesPresenter= MoviesPresenter(this)
    mMoviesPresenter?.setUpDisposable(disposable)
    setHasOptionsMenu(true)
    mMoviesAdapter = MoviesAdapter(mContext, this)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_movies, container, false)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    initViews()
    mMoviesPresenter?.setUpViewModel(this)
  }

  private fun initViews(){
    val staggeredGridLayoutManager =
      StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    recyclerview_movies.layoutManager = staggeredGridLayoutManager
    recyclerview_movies.adapter = mMoviesAdapter
  }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    mContext= context
  }

  override fun setPresenter(presenter: Presenter?) {
    mMoviesPresenter= presenter
  }

  override fun updateMoviesAdapter(movieUrlList: Array<String?>) {
    mMoviesAdapter!!.setMoviesData(movieUrlList)
  }

  override fun showError(error: String?) {
    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
  }

  override fun onGridItemClick(position: Int) {
    mMoviesPresenter?.onItemClicked(activity!!.supportFragmentManager, position)
  }

  override fun onCreateOptionsMenu(
    menu: Menu?,
    inflater: MenuInflater?
  ) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater?.inflate(R.menu.menu, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.popularMovies -> showPopularMovies()
      R.id.topRated -> showTopRatedMovies()
      R.id.favoriteMovies -> {
        activity!!.supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, FavoriteFragment.newInstance(), "Favorite")
            .addToBackStack(null)
            .commit()
      }
      else -> {
      }
    }
    return true
  }

  override fun showPopularMovies() {
    mMoviesPresenter?.loadPopularMovies()
  }

  override fun showTopRatedMovies() {
    mMoviesPresenter?.loadTopRatedMovies()
  }

  override fun onDestroy() {
    super.onDestroy()
    disposable.dispose()
  }
}
