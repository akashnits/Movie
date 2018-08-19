package com.example.android.popularmovies

import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.app.NavUtils
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast

import com.example.android.popularmovies.adapters.ReviewsAdapter
import com.example.android.popularmovies.model.Review
import com.example.android.popularmovies.model.ReviewItem
import com.example.android.popularmovies.networkUtils.ApiClient
import com.example.android.popularmovies.networkUtils.ApiService
import com.example.android.popularmovies.utilities.MoviesJsonUtils
import com.example.android.popularmovies.utilities.NetworkUtils

import java.net.URL
import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ReviewsActivity : AppCompatActivity() {
  @BindView(R.id.pb_loading_indicator)
  @JvmField var pbLoadingIndicator: ProgressBar? = null

  private var mReviewAdapter: ReviewsAdapter? = null
  private var mMovieId: Int = 0
  private var apiService: ApiService? = null
  private val disposable = CompositeDisposable()

  @BindView(R.id.rvReviewActivity)
  @JvmField var rvReviewActivity: RecyclerView? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_reviews)
    ButterKnife.bind(this)

    if (intent != null) {
      mMovieId = intent.getIntExtra("movieId", 0)
    }
    val actionBar = supportActionBar
    actionBar?.setDisplayHomeAsUpEnabled(true)
    val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    rvReviewActivity!!.layoutManager = linearLayoutManager
    rvReviewActivity!!.hasFixedSize()
    mReviewAdapter = ReviewsAdapter(this)
    rvReviewActivity!!.adapter = mReviewAdapter

    apiService = ApiClient.client.create(ApiService::class.java)
    loadMovieReview()
  }

  private fun loadMovieReview() {
    disposable.add(apiService!!
        .getMovieReviews(mMovieId, NetworkUtils.key, NetworkUtils.language)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map { review -> review.results }
        .subscribeWith(object : DisposableSingleObserver<List<ReviewItem>>() {
          override fun onSuccess(reviewItems: List<ReviewItem>) {
            mReviewAdapter!!.setData(reviewItems)
          }

          override fun onError(e: Throwable) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT)
                .show()
          }
        })
    )

  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> {
        onBackPressed()
        return true
      }
      else -> {
      }
    }
    return super.onOptionsItemSelected(item)
  }

  companion object {

    val LOADER_ID_REVIEW = 123
  }
}
