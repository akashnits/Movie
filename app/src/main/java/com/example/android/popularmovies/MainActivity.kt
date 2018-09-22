package com.example.android.popularmovies

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import com.example.android.popularmovies.adapters.MoviesAdapter
import com.example.android.popularmovies.data.MoviesViewModel
import com.example.android.popularmovies.networkUtils.ApiClient
import com.example.android.popularmovies.networkUtils.ApiService
import com.example.android.popularmovies.model.Response
import com.example.android.popularmovies.utilities.NetworkUtils

import butterknife.ButterKnife
import com.example.android.popularmovies.fragments.MoviesFragment
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity(){

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    if(savedInstanceState == null){
      supportFragmentManager
          .beginTransaction()
          .replace(R.id.container, MoviesFragment.newInstance(), "Movies")
          .commit()
    }
  }

  /*private fun getImageUrlObservable(response: Response): Single<Array<String?>> {
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
  }*/

  /*override fun onGridItemClick(position: Int) {
    val movie = mMoviesViewModel?.response?.results!![position]
    val movieDetailsIntent = Intent(this, MovieDetailActivity::class.java)
    movieDetailsIntent.putExtra(getString(R.string.MovieDataDetails), movie)
    startActivity(movieDetailsIntent)
  }*/

  /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
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
  }*/

 /* private fun loadPopularMovies() {
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
  }*/




 /* override fun onDestroy() {
    super.onDestroy()
    disposable.dispose()
  }*/
}
