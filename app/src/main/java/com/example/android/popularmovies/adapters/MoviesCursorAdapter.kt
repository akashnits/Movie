package com.example.android.popularmovies.adapters

import android.content.Context
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.example.android.popularmovies.R
import com.example.android.popularmovies.data.MovieContract
import com.example.android.popularmovies.displayUtilities.MoviesImageView
import com.squareup.picasso.Picasso

import butterknife.BindView
import butterknife.ButterKnife
import com.example.android.popularmovies.fragments.FavoriteFragment
import kotlinx.android.synthetic.main.list_item_movies.view.imageView
import kotlinx.android.synthetic.main.list_item_movies.view.linearLayout

class MoviesCursorAdapter(private val context: Context?, private val mHandler: OnGridItemClickHandler)
  : RecyclerView.Adapter<MoviesCursorAdapter.FavoriteMoviesViewHolder>() {
  private var mCursor: Cursor? = null
  companion object {

    val TAG = MoviesCursorAdapter::class.java.simpleName
  }

  interface OnGridItemClickHandler {
    fun onGridItemClickListener(movieId: Int)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMoviesViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.list_item_movies, parent, false)
    view.isFocusable = true
    return FavoriteMoviesViewHolder(view)
  }

  override fun onBindViewHolder(holder: FavoriteMoviesViewHolder, position: Int) {
    val moved = mCursor!!.moveToPosition(position)
    if (moved && !mCursor!!.isClosed) {
      val stringImageUrl =
        MoviesAdapter.BASE_IMAGE_URL + mCursor!!.getString(FavoriteFragment.INDEX_IMAGE_URL)
      Picasso.with(context)
          .load(stringImageUrl)
          .into(holder.imageView)
      holder.itemView.tag = mCursor!!.getInt(FavoriteFragment.INDEX_MOVIE_ID)
      Log.v(TAG, "ImageUrl to load: $stringImageUrl")
    }

  }

  override fun getItemCount(): Int {
    return if (mCursor == null) 0 else mCursor!!.count
  }

  inner class FavoriteMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val imageView = itemView.imageView;
    val linearLayout= itemView.linearLayout;
    init {
      itemView.setOnClickListener { mHandler.onGridItemClickListener(itemView.tag as Int) }
    }
  }

  fun swapCursor(c: Cursor) {
    mCursor = c
    notifyDataSetChanged()
  }
}
