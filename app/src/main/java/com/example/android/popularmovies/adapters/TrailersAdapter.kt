package com.example.android.popularmovies.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.example.android.popularmovies.R

import butterknife.BindView
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.list_item_trailers.view.ivPlayButton
import kotlinx.android.synthetic.main.list_item_trailers.view.tvTrailerName

class TrailersAdapter(private var mContext: Context, private var mClickHandler: OnTrailerClickHandler) : RecyclerView.Adapter<TrailersAdapter.TrailersAdapterViewHolder>() {
  private var mTrailerName: Array<String?>? = null

  interface OnTrailerClickHandler {
    fun onTrailerItemClickListener(position: Int)
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): TrailersAdapter.TrailersAdapterViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.list_item_trailers, parent, false)
    view.isFocusable = true
    return TrailersAdapterViewHolder(view)
  }

  override fun getItemCount(): Int {
    return if (mTrailerName == null) 0 else mTrailerName!!.size
  }

  override fun onBindViewHolder(
    holder: TrailersAdapter.TrailersAdapterViewHolder,
    position: Int
  ) {
    holder.itemView.tag = position
    val trailerName = mTrailerName!![position]
    holder.tvTrailerName!!.text = trailerName
    Log.v(TAG, "Trailer at position: $position is $trailerName")
  }

  fun setTrailersData(trailersData: Array<String?>) {
    mTrailerName = trailersData
    notifyDataSetChanged()
  }

  inner class TrailersAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val ivPlayButton= itemView.ivPlayButton
    val tvTrailerName= itemView.tvTrailerName

    init {
      itemView.setOnClickListener {
        val position = adapterPosition
        mClickHandler.onTrailerItemClickListener(position)
      }
    }
  }

  companion object {

    val TAG = TrailersAdapter::class.java.simpleName
  }

}
