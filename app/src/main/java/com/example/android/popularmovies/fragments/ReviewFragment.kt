package com.example.android.popularmovies.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast

import com.example.android.popularmovies.R
import com.example.android.popularmovies.adapters.ReviewsAdapter
import com.example.android.popularmovies.contract.ReviewContract
import com.example.android.popularmovies.contract.ReviewContract.Presenter
import com.example.android.popularmovies.model.ReviewItem
import com.example.android.popularmovies.presenter.ReviewPresenter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_review.rvReview

/**
 * A simple [Fragment] subclass.
 * Use the [ReviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ReviewFragment : Fragment(), ReviewContract.View {
  var pb_loading_indicator: ProgressBar? = null


  private var mReviewAdapter: ReviewsAdapter? = null
  private var mMovieId: Int = 0
  private val disposable = CompositeDisposable()
  private var mContext: Context? = null
  private var mReviewPresenter: ReviewContract.Presenter?= null

  companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewFragment.
     */
    @JvmStatic fun newInstance(
      param1: Int
    ) =
      ReviewFragment().apply {
        arguments = Bundle().apply {
          putInt("movieId", param1)
        }
      }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      mMovieId = it.getInt("movieId")
    }
    mReviewAdapter = ReviewsAdapter(mContext)
    mReviewPresenter= ReviewPresenter(this)
    mReviewPresenter?.setUpDisposable(disposable)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_review, container, false)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    initViews()
    mReviewPresenter?.loadMovieReviews(mMovieId)
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    mContext= context
  }

  private fun initViews(){
    val linearLayoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
    rvReview!!.layoutManager = linearLayoutManager
    rvReview!!.hasFixedSize()
    rvReview!!.adapter = mReviewAdapter
  }

  override fun showError(error: String?) {
    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
  }

  override fun setPresenter(presenter: Presenter?) {
    mReviewPresenter= presenter
  }

  override fun updateReviewAdapter(reviewList: List<ReviewItem>) {
    mReviewAdapter!!.setData(reviewList)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> {
        activity?.onBackPressed()
        return true
      }
      else -> {
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onDestroy() {
    super.onDestroy()
    disposable.dispose()
  }
}
