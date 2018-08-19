package com.example.android.popularmovies

import android.arch.lifecycle.ViewModelProviders
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.example.android.popularmovies.adapters.MoviesAdapter
import com.example.android.popularmovies.adapters.TrailersAdapter
import com.example.android.popularmovies.data.MovieContract
import com.example.android.popularmovies.data.MovieDetailViewModel
import com.example.android.popularmovies.model.Movies
import com.example.android.popularmovies.model.Trailer
import com.example.android.popularmovies.model.TrailerItem
import com.example.android.popularmovies.networkUtils.ApiClient
import com.example.android.popularmovies.networkUtils.ApiService
import com.example.android.popularmovies.utilities.MoviesJsonUtils
import com.example.android.popularmovies.utilities.NetworkUtils
import com.squareup.picasso.Picasso

import org.json.JSONException

import java.net.URL

import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import io.reactivex.SingleSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class MovieDetailActivity : AppCompatActivity(), TrailersAdapter.OnTrailerClickHandler {

  @BindView(R.id.tvMovieName)
  @JvmField var tvMovieName: TextView? = null
  @BindView(R.id.ivDetail)
  @JvmField var ivDetail: ImageView? = null
  @BindView(R.id.tvDate)
  @JvmField var tvDate: TextView? = null
  @BindView(R.id.tvRating)
  @JvmField var tvRating: TextView? = null
  @BindView(R.id.tvReview)
  @JvmField var tvReview: TextView? = null
  @BindView(R.id.activity_detail)
  @JvmField var activityDetail: ConstraintLayout? = null
  @BindView(R.id.recyclerview_movie_trailers)
  @JvmField var recyclerViewTrailers: RecyclerView? = null
  @BindView(R.id.divider)
  @JvmField var divider: View? = null
  @BindView(R.id.tvTrailers)
  @JvmField var tvTrailers: TextView? = null
  @BindView(R.id.btReview)
  @JvmField var btReview: Button? = null
  @BindView(R.id.btFavorite)
  @JvmField var btFavorite: CheckBox? = null

  private var movie: Movies? = null
  private var mMovieId: Int = 0
  private var mTrailersAdapter: TrailersAdapter? = null
  private var isMarkedFavorite: Boolean = false
  private var apiService: ApiService? = null
  private val disposable = CompositeDisposable()
  private var mMovieDetailViewModel: MovieDetailViewModel? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_detail)
    ButterKnife.bind(this)
    val actionBar = supportActionBar
    actionBar?.setDisplayHomeAsUpEnabled(true)
    movie = intent.getParcelableExtra(getString(R.string.MovieData))
    tvMovieName!!.text = movie!!.title
    Picasso.with(this)
        .load(MoviesAdapter.BASE_IMAGE_URL + movie!!.posterPath!!)
        .into(ivDetail)
    tvDate!!.text = movie!!.releaseDate!!.substring(0, 4)
    tvRating!!.text = movie!!.voteAverage.toString() + getString(R.string.OutOfTen)
    tvReview!!.text = movie!!.overview
    if (savedInstanceState != null) {
      isMarkedFavorite = savedInstanceState.getBoolean("favoriteBtMarked")
    } else {
      mMovieId = movie!!.id
      markedFavoriteStatus(MovieContract.MovieEntry.CONTENT_URI)
    }
    if (isMarkedFavorite) {
      btFavorite!!.isChecked = true
    } else {
      btFavorite!!.isChecked = false
    }


    apiService = ApiClient.client.create(ApiService::class.java)
    val linearLayoutManager = LinearLayoutManager(
        this,
        LinearLayoutManager.VERTICAL, false
    )
    recyclerViewTrailers!!.layoutManager = linearLayoutManager
    recyclerViewTrailers!!.hasFixedSize()

    mTrailersAdapter = TrailersAdapter(this, this)
    recyclerViewTrailers!!.adapter = mTrailersAdapter
    mMovieDetailViewModel = ViewModelProviders.of(this)
        .get(MovieDetailViewModel::class.java)
    if (mMovieDetailViewModel!!.trailer == null)
      loadMovieTrailers()
    else {
      mTrailersAdapter!!.setTrailersData(getTrailerNames(mMovieDetailViewModel?.trailer!!))
    }
  }

  private fun loadMovieTrailers() {
    disposable.add(apiService!!.getMovieTrailers(mMovieId, NetworkUtils.key, NetworkUtils.language)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .flatMap { trailer ->
          mMovieDetailViewModel!!.trailer = trailer
          getTrailerNameObservable(trailer)
        }.subscribeWith(object : DisposableSingleObserver<Array<String?>>() {
          override fun onSuccess(strings: Array<String?>) {
            mTrailersAdapter!!.setTrailersData(strings)
          }

          override fun onError(e: Throwable) {

          }
        })
    )
  }

  fun getTrailerNameObservable(trailer: Trailer): Single<Array<String?>> {
    return Single.create { emitter ->
      if (!emitter.isDisposed)
        emitter.onSuccess(getTrailerNames(trailer))
    }
  }

  private fun getTrailerNames(trailer: Trailer): Array<String?> {
    val trailerNames = arrayOfNulls<String>(trailer.results!!.size)
    val trailerItems = trailer.results
    for (i in 0 until trailer.results!!.size) {
      trailerNames[i] = trailerItems!![i].name
    }
    return trailerNames
  }

  override fun onTrailerItemClickListener(position: Int) {
    var key: String? = null
    key = mMovieDetailViewModel!!.trailer!!.results!![position].key
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key!!))
    if (intent.resolveActivity(packageManager) != null) {
      startActivity(Intent.createChooser(intent, "View Trailer"))
    }

  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> {
        onBackPressed()
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  fun reviewClicked(view: View) {
    val intent = Intent(this, ReviewsActivity::class.java)
    intent.putExtra("movieId", mMovieId)
    startActivity(intent)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putBoolean("favoriteBtMarked", isMarkedFavorite)
  }

  fun favoriteClicked(view: View) {
    val resolver = contentResolver
    val uri = MovieContract.MovieEntry.CONTENT_URI


    markedFavoriteStatus(uri)

    if (!isMarkedFavorite) {
      val cv = ContentValues()
      cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovieId)
      cv.put(MovieContract.MovieEntry.COULMN_IMAGE_URL, movie!!.posterPath)
      cv.put(MovieContract.MovieEntry.COLUMN_DATE, movie!!.releaseDate)
      cv.put(MovieContract.MovieEntry.COLUMN_RATINGS, movie!!.voteAverage)
      cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movie!!.title)
      cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie!!.overview)

      val returnedUri = resolver.insert(uri, cv)
      Log.v(TAG, "Inserted uri: " + returnedUri!!)
      //btFavorite.setBackgroundColor(getResources().getColor(R.color.green));
      isMarkedFavorite = true
    } else {
      val numberOfRows = resolver.delete(
          uri, MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
          arrayOf(mMovieId.toString())
      )
      //btFavorite.setBackgroundColor(getResources().getColor(R.color.lightGray));
      Log.v(TAG, "Rows deleted $numberOfRows")
      isMarkedFavorite = false
    }

  }

  private fun markedFavoriteStatus(uri: Uri) {
    val cursor = contentResolver.query(
        uri, null, MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?", arrayOf(mMovieId.toString()),
        null
    )
    if (cursor != null && cursor.count != 0)
      isMarkedFavorite = true
    else
      isMarkedFavorite = false
  }

  companion object {

    private val LOADER_FOR_TRAILER = 949

    val TAG = MovieDetailActivity::class.java.simpleName
  }
}
