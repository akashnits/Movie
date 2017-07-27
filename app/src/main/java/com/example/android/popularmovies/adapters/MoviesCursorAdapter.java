package com.example.android.popularmovies.adapters;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.android.popularmovies.FavoritesActivity;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.displayUtilities.MoviesImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesCursorAdapter extends RecyclerView.Adapter<MoviesCursorAdapter.FavoriteMoviesViewHolder> {

    public static final String TAG= MoviesCursorAdapter.class.getSimpleName();

    private Context context;
    private Cursor mCursor;
    private OnGridItemClickHandler mHandler;

    public MoviesCursorAdapter(Context context, OnGridItemClickHandler handler) {
        this.context = context;
        mHandler= handler;
    }

    public interface OnGridItemClickHandler{
        void onGridItemClickListener(int movieId);
    }

    @Override
    public FavoriteMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movies, parent, false);
        view.setFocusable(true);
        return new FavoriteMoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteMoviesViewHolder holder, int position) {
        boolean moved= mCursor.moveToPosition(position);
        if(moved) {
            String stringImageUrl = MoviesAdapter.BASE_IMAGE_URL + mCursor.getString(FavoritesActivity.INDEX_IMAGE_URL);
            Picasso.with(context).load(stringImageUrl).into(holder.imageView);
            holder.itemView.setTag(mCursor.getInt(FavoritesActivity.INDEX_MOVIE_ID));
            Log.v(TAG, "ImageUrl to load: " + stringImageUrl);
        }


    }

    @Override
    public int getItemCount() {
        if(mCursor == null)
            return 0;
        return mCursor.getCount();
    }

    class FavoriteMoviesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        MoviesImageView imageView;
        @BindView(R.id.linearLayout)
        LinearLayout linearLayout;

        public FavoriteMoviesViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHandler.onGridItemClickListener((int) itemView.getTag());
                }
            });
        }
    }

    public void swapCursor(Cursor c){
        mCursor= c;
        notifyDataSetChanged();
    }
}
