package com.example.android.popularmovies.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.example.android.popularmovies.R
import com.squareup.picasso.Picasso

import butterknife.BindView
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.list_item_movies.view.imageView

class MoviesAdapter(private val mContext: Context, private val mClickHandler: onGridItemClickHandler) : RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder>() {
  private var mMoviesData: Array<String?>? = null

  companion object {

    val TAG = MoviesAdapter::class.java.simpleName
    val BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185"
  }

  interface onGridItemClickHandler {
    fun onGridItemClick(position: Int)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesAdapter.MoviesAdapterViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.list_item_movies, parent, false)
    view.isFocusable = true
    return MoviesAdapterViewHolder(view)
  }

  override fun onBindViewHolder(holder: MoviesAdapter.MoviesAdapterViewHolder, position: Int) {
    val stringImageUrl = BASE_IMAGE_URL + mMoviesData!![position]
    holder.itemView.tag = position
    Log.v(TAG, "Image url to load: $stringImageUrl")
    Picasso.with(mContext)
        .load(stringImageUrl)
        .into(holder.imageView)
  }

  override fun getItemCount(): Int {
    return if (mMoviesData == null) 0 else mMoviesData!!.size
  }

    inner class MoviesAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val imageView= view.imageView

    init {
      view.setOnClickListener { mClickHandler.onGridItemClick(adapterPosition) }
    }
  }

  fun setMoviesData(data: Array<String?>) {
    mMoviesData = data
    notifyDataSetChanged()
  }
}
