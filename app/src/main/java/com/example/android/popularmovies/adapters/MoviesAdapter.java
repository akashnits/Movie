package com.example.android.popularmovies.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder>{
    private Context mContext;
    private String[] mMoviesData;
    private final onGridItemClickHandler mClickHandler;

    public static final String TAG= MoviesAdapter.class.getSimpleName();
    public static final String BASE_IMAGE_URL= "http://image.tmdb.org/t/p/w342";



    public interface onGridItemClickHandler{
       void onGridItemClick(int position);
    }


    public MoviesAdapter(Context context, onGridItemClickHandler onClickHandler) {
        mContext= context;
        mClickHandler= onClickHandler;
    }

    @Override
    public MoviesAdapter.MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movies, parent, false);
        view.setFocusable(true);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.MoviesAdapterViewHolder holder, int position) {
        String stringImageUrl= BASE_IMAGE_URL+ mMoviesData[position];
        holder.itemView.setTag(position);
        Log.v(TAG, "Image url to load: " + stringImageUrl);
        Picasso.with(mContext).load(stringImageUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if(mMoviesData == null)
            return 0;
        return mMoviesData.length;
    }

     class MoviesAdapterViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imageView) ImageView imageView;
        public MoviesAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickHandler.onGridItemClick(getAdapterPosition());
                }
            });
        }
    }
    public void setMoviesData(String[] data){
        mMoviesData= data;
        notifyDataSetChanged();
    }
}
