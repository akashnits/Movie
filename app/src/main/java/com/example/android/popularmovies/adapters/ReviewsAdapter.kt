package com.example.android.popularmovies.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.android.popularmovies.R
import com.example.android.popularmovies.model.Review
import com.example.android.popularmovies.model.ReviewItem

import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.list_item_reviews.view.tvReview
import kotlinx.android.synthetic.main.list_item_reviews.view.tvUsername

class ReviewsAdapter : RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder> {

  private var context: Context? = null
  private var mReview: List<ReviewItem>? = null

  constructor() {}

  constructor(context: Context?) {
    this.context = context
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ReviewsAdapterViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.list_item_reviews, parent, false)
    view.isFocusable = true
    return ReviewsAdapterViewHolder(view)
  }

  override fun onBindViewHolder(
    holder: ReviewsAdapterViewHolder,
    position: Int
  ) {
    holder.tvReview!!.text = mReview!![position]
        .content
    holder.tvUsername!!.text = mReview!![position]
        .author
  }

  override fun getItemCount(): Int {
    return if (mReview != null) mReview!!.size else 0
  }

  inner class ReviewsAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val tvUsername = itemView.tvUsername;
    val tvReview = itemView.tvReview;

  }

  fun setData(review: List<ReviewItem>) {
    mReview = review
    notifyDataSetChanged()
  }
}
