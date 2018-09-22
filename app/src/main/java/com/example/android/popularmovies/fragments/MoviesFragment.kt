package com.example.android.popularmovies.fragments

import android.arch.lifecycle.ViewModelProviders
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
import com.example.android.popularmovies.data.MoviesViewModel
import com.example.android.popularmovies.model.Response
import com.example.android.popularmovies.networkUtils.ApiClient
import com.example.android.popularmovies.networkUtils.ApiService
import com.example.android.popularmovies.utilities.NetworkUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
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
class MoviesFragment : Fragment() , MoviesAdapter.onGridItemClickHandler{
  private var mContext: Context?= null
  private var mMoviesAdapter: MoviesAdapter? = null
  private var apiService: ApiService? = null
  private val disposable = CompositeDisposable()
  private var mMoviesViewModel: MoviesViewModel? = null


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
    setHasOptionsMenu(true)
    mMoviesAdapter = MoviesAdapter(mContext, this)
    apiService = ApiClient.client
        .create<ApiService>(ApiService::class.java)
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
    initViewModel()
  }

  private fun initViews(){
    val staggeredGridLayoutManager =
      StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    recyclerview_movies.layoutManager = staggeredGridLayoutManager
    recyclerview_movies.adapter = mMoviesAdapter
  }

  private fun initViewModel(){
    mMoviesViewModel = ViewModelProviders.of(this)
        .get(MoviesViewModel::class.java)
    if (mMoviesViewModel!!.response == null) {
      loadPopularMovies()
    } else {
      val movieImageUrls = getImageUrls(mMoviesViewModel!!.response)
      mMoviesAdapter!!.setMoviesData(movieImageUrls)
    }
  }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    mContext= context
  }

  private fun loadPopularMovies() {
    disposable.add(apiService!!
        .getPopularMovies(NetworkUtils.key, NetworkUtils.language)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .flatMap { movie ->
          mMoviesViewModel!!.response = movie
          getImageUrlObservable(movie)
        }
        .subscribeWith(object : DisposableSingleObserver<Array<String?>>() {
          override fun onSuccess(strings: Array<String?>) {
            mMoviesAdapter!!.setMoviesData(strings)
          }

          override fun onError(e: Throwable) {
            Toast.makeText(mContext, e.message, Toast.LENGTH_SHORT)
                .show()
          }
        })
    )
  }

  private fun loadTopRatedMovies() {
    disposable.add(apiService!!
        .getTopRatedMovies(NetworkUtils.key, NetworkUtils.language)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .flatMap { movie ->
          mMoviesViewModel!!.response = movie
          getImageUrlObservable(movie)
        }
        .subscribeWith(object : DisposableSingleObserver<Array<String?>>() {
          override fun onSuccess(strings: Array<String?>) {
            mMoviesAdapter!!.setMoviesData(strings)
          }

          override fun onError(e: Throwable) {
            Toast.makeText(mContext, e.message, Toast.LENGTH_SHORT)
                .show()
          }
        })
    )
  }

  private fun getImageUrlObservable(response: Response): Single<Array<String?>> {
    val moviesImageUrl = getImageUrls(response)

    return Single.create { emitter ->
      if (!emitter.isDisposed) {
        emitter.onSuccess(moviesImageUrl)
      }
    }
  }

  private fun getImageUrls(response: Response?): Array<String?> {
    val moviesImageUrl = arrayOfNulls<String>(response?.results!!.size)

    for (i in 0 until response.results!!.size) {
      moviesImageUrl[i] = response.results!![i].posterPath
    }
    return moviesImageUrl
  }

  override fun onGridItemClick(position: Int) {
    val movie = mMoviesViewModel?.response?.results!![position]
    activity!!.supportFragmentManager
        .beginTransaction()
        .replace(R.id.container, MovieDetailFragment.newInstance(movie), "Movie details")
        .addToBackStack(null)
        .commit()
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
      R.id.popularMovies -> loadPopularMovies()
      R.id.topRated -> loadTopRatedMovies()
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

  override fun onDestroy() {
    super.onDestroy()
    disposable.dispose()
  }
}
