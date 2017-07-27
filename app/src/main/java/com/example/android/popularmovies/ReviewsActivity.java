package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.popularmovies.adapters.ReviewsAdapter;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.utilities.MoviesJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Review>> {

    public static final int LOADER_ID_REVIEW = 123;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar pbLoadingIndicator;
    private ReviewsAdapter mReviewAdapter;
    private int mMovieId;

    @BindView(R.id.rvReviewActivity)
    RecyclerView rvReviewActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            mMovieId = getIntent().getIntExtra("movieId", 0);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvReviewActivity.setLayoutManager(linearLayoutManager);
        rvReviewActivity.hasFixedSize();
        mReviewAdapter = new ReviewsAdapter(this);
        rvReviewActivity.setAdapter(mReviewAdapter);

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(LOADER_ID_REVIEW, null, this);
    }

    @Override
    public Loader<ArrayList<Review>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Review>>(this) {
            private ArrayList<Review> mReview = null;

            @Override
            protected void onStartLoading() {
                pbLoadingIndicator.setVisibility(View.VISIBLE);
                super.onStartLoading();
                if (mReview != null)
                    deliverResult(mReview);
                else
                    forceLoad();
            }

            @Override
            public ArrayList<Review> loadInBackground() {
                URL url = null;
                String path = "/" + mMovieId + "/reviews";
                url = NetworkUtils.buildUrl(path);
                try {
                    String reviewJSONString = NetworkUtils.getResponseFromHttpUrl(url);
                    return MoviesJsonUtils.getReviewObjectsFromJSON(reviewJSONString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void deliverResult(ArrayList<Review> data) {
                mReview = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> data) {
        pbLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data != null) {
            mReviewAdapter.setData(data);
        } else {
            Toast.makeText(ReviewsActivity.this, "No Reviews", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Review>> loader) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
