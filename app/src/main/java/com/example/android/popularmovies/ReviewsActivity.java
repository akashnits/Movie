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
import com.example.android.popularmovies.model.ReviewItem;
import com.example.android.popularmovies.networkUtils.ApiClient;
import com.example.android.popularmovies.networkUtils.ApiService;
import com.example.android.popularmovies.utilities.MoviesJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ReviewsActivity extends AppCompatActivity {

    public static final int LOADER_ID_REVIEW = 123;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar pbLoadingIndicator;
    private ReviewsAdapter mReviewAdapter;
    private int mMovieId;
    private ApiService apiService;
    private CompositeDisposable disposable= new CompositeDisposable();

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

        apiService= ApiClient.getClient().create(ApiService.class);
        loadMovieReview();
    }

    private void loadMovieReview(){
        disposable.add(apiService
                .getMovieReviews(mMovieId, NetworkUtils.key, NetworkUtils.language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Review, List<ReviewItem>>() {
                    @Override
                    public List<ReviewItem> apply(Review review) throws Exception {
                        return review.getResults();
                    }
                })
                .subscribeWith(new DisposableSingleObserver<List<ReviewItem>>(){
                    @Override
                    public void onSuccess(List<ReviewItem> reviewItems) {
                       mReviewAdapter.setData(reviewItems);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        );

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
