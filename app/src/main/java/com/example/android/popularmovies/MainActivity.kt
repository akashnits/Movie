package com.example.android.popularmovies

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.Toast

import com.example.android.popularmovies.adapters.MoviesAdapter
import com.example.android.popularmovies.data.MoviesViewModel
import com.example.android.popularmovies.networkUtils.ApiClient
import com.example.android.popularmovies.networkUtils.ApiService
import com.example.android.popularmovies.model.Movies
import com.example.android.popularmovies.model.Response
import com.example.android.popularmovies.utilities.NetworkUtils

import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import io.reactivex.SingleSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_movies.*;


class MainActivity : AppCompatActivity(), MoviesAdapter.onGridItemClickHandler {

  private var mMoviesAdapter: MoviesAdapter? = null
  private var apiService: ApiService? = null
  private val disposable = CompositeDisposable()
  private var mMoviesViewModel: MoviesViewModel? = null



  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_movies)
    ButterKnife.bind(this)
    mMoviesAdapter = MoviesAdapter(this, this)

    val staggeredGridLayoutManager =
      StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

    apiService = ApiClient.client
        .create<ApiService>(ApiService::class.java)

    recyclerview_movies.layoutManager = staggeredGridLayoutManager
    recyclerview_movies.adapter = mMoviesAdapter

    mMoviesViewModel = ViewModelProviders.of(this)
        .get(MoviesViewModel::class.java)
    if (mMoviesViewModel!!.response == null) {
      loadPopularMovies()
    } else {
      val movieImageUrls = getImageUrls(mMoviesViewModel!!.response)
      mMoviesAdapter!!.setMoviesData(movieImageUrls)
    }
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
    val movieDetailsIntent = Intent(this, MovieDetailActivity::class.java)
    movieDetailsIntent.putExtra(getString(R.string.MovieDataDetails), movie)
    startActivity(movieDetailsIntent)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    super.onCreateOptionsMenu(menu)
    menuInflater.inflate(R.menu.menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.popularMovies -> loadPopularMovies()
      R.id.topRated -> loadTopRatedMovies()
      R.id.favoriteMovies -> {
        val intent = Intent(this, FavoritesActivity::class.java)
        startActivity(intent)
        Log.v("MainActivity", "Favorites intent launched")
      }
      else -> {
      }
    }
    return true
  }

  private fun loadPopularMovies() {
    disposable.add(apiService!!
        .getPopularMovies(NetworkUtils.key, NetworkUtils.language)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .flatMap { movie ->
          mMoviesViewModel!!.response = movie
          getImageUrlObservable(movie)
        }
        .subscribeWith(object : DisposableSingleObserver<Array<String?>>() {
          override fun onSuccess(strings: Array<String?>) {
            mMoviesAdapter!!.setMoviesData(strings)
          }

          override fun onError(e: Throwable) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT)
                .show()
          }
        })
    )
  }


  private fun loadTopRatedMovies() {
    disposable.add(apiService!!
        .getTopRatedMovies(NetworkUtils.key, NetworkUtils.language)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .flatMap { movie ->
          mMoviesViewModel!!.response = movie
          getImageUrlObservable(movie)
        }
        .subscribeWith(object : DisposableSingleObserver<Array<String?>>() {
          override fun onSuccess(strings: Array<String?>) {
            mMoviesAdapter!!.setMoviesData(strings)
          }

          override fun onError(e: Throwable) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT)
                .show()
          }
        })
    )
  }

  override fun onDestroy() {
    super.onDestroy()
    disposable.dispose()
  }
}
