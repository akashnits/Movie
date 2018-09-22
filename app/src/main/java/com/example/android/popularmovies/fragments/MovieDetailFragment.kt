package com.example.android.popularmovies.fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.android.popularmovies.R
import com.example.android.popularmovies.adapters.MoviesAdapter
import com.example.android.popularmovies.adapters.TrailersAdapter
import com.example.android.popularmovies.data.MovieContract
import com.example.android.popularmovies.data.MovieDetailViewModel
import com.example.android.popularmovies.model.Movies
import com.example.android.popularmovies.model.Trailer
import com.example.android.popularmovies.networkUtils.ApiClient
import com.example.android.popularmovies.networkUtils.ApiService
import com.example.android.popularmovies.utilities.NetworkUtils
import com.squareup.picasso.Picasso
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.ivDetail
import kotlinx.android.synthetic.main.activity_detail.recyclerViewTrailers
import kotlinx.android.synthetic.main.activity_detail.tvDate
import kotlinx.android.synthetic.main.activity_detail.tvMovieName
import kotlinx.android.synthetic.main.activity_detail.tvRating
import kotlinx.android.synthetic.main.activity_detail.tvReview
import kotlinx.android.synthetic.main.fragment_movie_detail.btFavorite
import kotlinx.android.synthetic.main.fragment_movie_detail.btReview

/**
 * A simple [Fragment] subclass.
 * Use the [MovieDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

class MovieDetailFragment : Fragment(), TrailersAdapter.OnTrailerClickHandler  {
  private var movie: Movies? = null
  private var mMovieId: Int = 0
  private var mTrailersAdapter: TrailersAdapter? = null
  private var isMarkedFavorite: Boolean = false
  private var apiService: ApiService? = null
  private val disposable = CompositeDisposable()
  private var mMovieDetailViewModel: MovieDetailViewModel? = null
  private var mContext: Context? =null

  companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MovieDetailFragment.
     */
    @JvmStatic fun newInstance(
      movie: Movies
    ) =
      MovieDetailFragment().apply {
        arguments = Bundle().apply {
          putParcelable("movieData", movie)
        }
      }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      movie = it.getParcelable("movieData")
    }
    mMovieId = movie!!.id
    if (savedInstanceState != null) {
      isMarkedFavorite = savedInstanceState.getBoolean("favoriteBtMarked")
    } else {
      markedFavoriteStatus(MovieContract.MovieEntry.CONTENT_URI)
    }
    apiService = ApiClient.client
        .create<ApiService>(ApiService::class.java)
    setHasOptionsMenu(true)
    mTrailersAdapter = TrailersAdapter(mContext, this)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_movie_detail, container, false)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    initViews()
    initViewModel()
    populateViews(savedInstanceState)
  }

  private fun initViews(){
    //TODO: Fix toolbar

    val linearLayoutManager = LinearLayoutManager(
        mContext,
        LinearLayoutManager.VERTICAL, false
    )
    recyclerViewTrailers!!.layoutManager = linearLayoutManager
    recyclerViewTrailers!!.hasFixedSize()
    recyclerViewTrailers!!.adapter = mTrailersAdapter

    btReview.setOnClickListener(View.OnClickListener {
      activity!!.supportFragmentManager
        .beginTransaction()
        .replace(R.id.container, ReviewFragment.newInstance(mMovieId), "Review")
          .addToBackStack(null)
        .commit()  })

    btFavorite.setOnClickListener{ view ->
      val resolver = activity?.contentResolver
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

        val returnedUri = resolver?.insert(uri, cv)
        isMarkedFavorite = true
      } else {
        val numberOfRows = resolver?.delete(
            uri, MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
            arrayOf(mMovieId.toString())
        )
        isMarkedFavorite = false
      }
    }
  }

  private fun initViewModel(){
    mMovieDetailViewModel = ViewModelProviders.of(this)
        .get(MovieDetailViewModel::class.java)
    if (mMovieDetailViewModel!!.trailer == null)
      loadMovieTrailers()
    else {
      mTrailersAdapter!!.setTrailersData(getTrailerNames(mMovieDetailViewModel?.trailer!!))
    }
  }

  private fun populateViews(savedInstanceState: Bundle?){
    tvMovieName!!.text = movie!!.title
    Picasso.with(mContext)
        .load(MoviesAdapter.BASE_IMAGE_URL + movie!!.posterPath!!)
        .into(ivDetail)
    tvDate!!.text = movie!!.releaseDate!!.substring(0, 4)
    tvRating!!.text = movie!!.voteAverage.toString() + getString(R.string.OutOfTen)
    tvReview!!.text = movie!!.overview
    btFavorite!!.isChecked = isMarkedFavorite
  }


  override fun onAttach(context: Context?) {
    super.onAttach(context)
    mContext= context
  }

  private fun loadMovieTrailers() {
    disposable.add(apiService!!.getMovieTrailers(mMovieId, NetworkUtils.key, NetworkUtils.language)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .flatMap { trailer ->
          mMovieDetailViewModel!!.trailer = trailer
          getTrailerNameObservable(trailer)
        }.subscribeWith(object : DisposableSingleObserver<Array<String?>>() {
          override fun onSuccess(strings: Array<String?>) {
            mTrailersAdapter!!.setTrailersData(strings)
          }

          override fun onError(e: Throwable) {
            Toast.makeText(mContext, e.message, Toast.LENGTH_LONG).show()
          }
        })
    )
  }

  private fun getTrailerNameObservable(trailer: Trailer): Single<Array<String?>> {
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
    if (intent.resolveActivity(activity?.packageManager) != null) {
      startActivity(Intent.createChooser(intent, "View Trailer"))
    }
  }

  private fun markedFavoriteStatus(uri: Uri) {
    val cursor = mContext?.contentResolver?.query(
        uri, null, MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?", arrayOf(mMovieId.toString()),
        null
    )
    isMarkedFavorite = (cursor != null && cursor.count != 0)
    cursor?.close()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> {
        activity?.onBackPressed()
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putBoolean("favoriteBtMarked", isMarkedFavorite)
  }

  override fun onDestroy() {
    super.onDestroy()
    disposable.dispose()
  }
}
