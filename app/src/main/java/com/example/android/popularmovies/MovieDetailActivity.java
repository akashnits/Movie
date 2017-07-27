package com.example.android.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.adapters.MoviesAdapter;
import com.example.android.popularmovies.adapters.TrailersAdapter;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utilities.MoviesJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String[]>, TrailersAdapter.OnTrailerClickHandler {

    private static final int LOADER_FOR_TRAILER = 949;


    public static final String TAG = MovieDetailActivity.class.getSimpleName();

    @BindView(R.id.tvMovieName)
    TextView tvMovieName;
    @BindView(R.id.ivDetail)
    ImageView ivDetail;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvRating)
    TextView tvRating;
    @BindView(R.id.tvReview)
    TextView tvReview;
    @BindView(R.id.activity_detail)
    ConstraintLayout activityDetail;
    @BindView(R.id.recyclerview_movie_trailers)
    RecyclerView recyclerViewTrailers;
    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.tvTrailers)
    TextView tvTrailers;
    @BindView(R.id.btReview)
    Button btReview;
    @BindView(R.id.btFavorite)
    CheckBox btFavorite;


    private Movie movie;
    private int mMovieId;
    private TrailersAdapter mTrailersAdapter;
    private boolean isMarkedFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        movie = getIntent().getParcelableExtra(getString(R.string.MovieData));
        tvMovieName.setText(movie.getmMovieTitle());
        Picasso.with(this).load(MoviesAdapter.BASE_IMAGE_URL + movie.getmImageUrl()).into(ivDetail);
        tvDate.setText(movie.getmDate().substring(0, 4));
        tvRating.setText(movie.getmRatings() + getString(R.string.OutOfTen));
        tvReview.setText(movie.getmOverview());
        if (savedInstanceState != null) {
            isMarkedFavorite = savedInstanceState.getBoolean("favoriteBtMarked");
        } else {
            mMovieId = movie.getmMovieId();
            markedFavoriteStatus(MovieContract.MovieEntry.CONTENT_URI);
        }
        if(isMarkedFavorite){
            btFavorite.setChecked(true);
        }else {
            btFavorite.setChecked(false);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewTrailers.setLayoutManager(linearLayoutManager);
        recyclerViewTrailers.hasFixedSize();

        mTrailersAdapter = new TrailersAdapter(this, this);
        recyclerViewTrailers.setAdapter(mTrailersAdapter);
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(LOADER_FOR_TRAILER, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public Loader<String[]> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String[]>(this) {
            String[] trailersData = null;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (trailersData != null)
                    deliverResult(trailersData);
                else
                    forceLoad();
            }

            @Override
            public String[] loadInBackground() {
                URL url;
                mMovieId = movie.getmMovieId();
                String path = "/" + mMovieId + "/videos";
                url = NetworkUtils.buildUrl(path);

                try {
                    String jsonStringForTrailers = NetworkUtils.getResponseFromHttpUrl(url);
                    return MoviesJsonUtils.getTrailerStringsFromJSON(jsonStringForTrailers);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(String[] data) {
                trailersData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {
        mTrailersAdapter.setTrailersData(data);
    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

    }

    @Override
    public void onTrailerItemClickListener(int position) {
        String key = null;
        try {
            key = MoviesJsonUtils.getTrailerStringId(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "View Trailer"));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void reviewClicked(View view) {
        Intent intent = new Intent(this, ReviewsActivity.class);
        intent.putExtra("movieId", mMovieId);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("favoriteBtMarked", isMarkedFavorite);
    }

    public void favoriteClicked(View view) {
        ContentResolver resolver = getContentResolver();
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;


        markedFavoriteStatus(uri);

        if (!isMarkedFavorite) {
            ContentValues cv = new ContentValues();
            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovieId);
            cv.put(MovieContract.MovieEntry.COULMN_IMAGE_URL, movie.getmImageUrl());
            cv.put(MovieContract.MovieEntry.COLUMN_DATE, movie.getmDate());
            cv.put(MovieContract.MovieEntry.COLUMN_RATINGS, movie.getmRatings());
            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getmMovieTitle());
            cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getmOverview());

            Uri returnedUri = resolver.insert(uri, cv);
            Log.v(TAG, "Inserted uri: " + returnedUri);
            //btFavorite.setBackgroundColor(getResources().getColor(R.color.green));
            isMarkedFavorite = true;
        } else {
            int numberOfRows = resolver.delete(uri, MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                    new String[]{String.valueOf(mMovieId)});
            //btFavorite.setBackgroundColor(getResources().getColor(R.color.lightGray));
            Log.v(TAG, "Rows deleted " + numberOfRows);
            isMarkedFavorite = false;
        }


    }

    private void markedFavoriteStatus(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?", new String[]{String.valueOf(mMovieId)}, null);
        if (cursor != null && cursor.getCount() != 0)
            isMarkedFavorite = true;
        else
            isMarkedFavorite = false;
    }
}
