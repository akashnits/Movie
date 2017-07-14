package com.example.android.popularmovies.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersAdapterViewHolder> {
    private Context context;
    private String[] mTrailerName;
    private OnTrailerClickHandler mClickHandler;

    public static final String TAG= TrailersAdapter.class.getSimpleName();


    public TrailersAdapter() {
    }


    public TrailersAdapter(Context context, OnTrailerClickHandler onTrailerClickHandler) {
        this.context = context;
        this.mClickHandler= onTrailerClickHandler;
    }

    public interface OnTrailerClickHandler{
         void onTrailerItemClickListener(int position);
    }

    @Override
    public TrailersAdapter.TrailersAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_trailers, parent, false);
        view.setFocusable(true);
        return new TrailersAdapterViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if(mTrailerName == null)
            return 0;
        return mTrailerName.length;
    }

    @Override
    public void onBindViewHolder(TrailersAdapter.TrailersAdapterViewHolder holder, int position) {
        holder.itemView.setTag(position);
        String trailerName= mTrailerName[position];
        holder.trailerName.setText(trailerName);
        Log.v(TAG, "Trailer at position: " + position + " is " + trailerName);
    }

    public void setTrailersData(String[] trailersData){
        mTrailerName= trailersData;
        notifyDataSetChanged();
    }

    class TrailersAdapterViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ivPlayButton)
        ImageView playButton;
        @BindView(R.id.tvTrailerName)
        TextView trailerName;
        public TrailersAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mClickHandler.onTrailerItemClickListener(position);
                }
            });
        }
    }

}
