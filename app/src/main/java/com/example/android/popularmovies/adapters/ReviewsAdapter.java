package com.example.android.popularmovies.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Review;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder> {


    private Context context;
    private ArrayList<Review> mReview;

    public ReviewsAdapter() {
    }

    public ReviewsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reviews, parent, false);
        view.setFocusable(true);
        return new ReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapterViewHolder holder, int position) {
        holder.tvReview.setText(mReview.get(position).getmReview());
        holder.tvUsername.setText(mReview.get(position).getmUsername());
    }

    @Override
    public int getItemCount() {
        if(mReview != null)
            return mReview.size();
        return 0;
    }

    class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvUsername)
        TextView tvUsername;
        @BindView(R.id.tvReview)
        TextView tvReview;

        public ReviewsAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public void setData(ArrayList<Review> review) {
        mReview= review;
        notifyDataSetChanged();
    }
}
