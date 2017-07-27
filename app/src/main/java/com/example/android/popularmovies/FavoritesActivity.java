package com.example.android.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.popularmovies.adapters.MoviesCursorAdapter;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.model.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, MoviesCursorAdapter.OnGridItemClickHandler{

    public static final String[] MOVIES_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COULMN_IMAGE_URL,
            MovieContract.MovieEntry.COLUMN_DATE,
            MovieContract.MovieEntry.COLUMN_RATINGS,
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW
    };


    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_IMAGE_URL = 1;
    public static final int INDEX_DATE = 2;
    public static final int INDEX_RATINGS = 3;
    public static final int INDEX_MOVIE_TITLE = 4;
    public static final int INDEX_OVERVIEW = 5;

    public static final String TAG= FavoritesActivity.class.getSimpleName();

    public static final int LOADER_FAV_MOVIES_ID= 183;
    private MoviesCursorAdapter mMoviesCursorAdapter;

    @BindView(R.id.recyclerview_fav_movies)
    RecyclerView recyclerviewFavmovies;
    @BindView(R.id.pb_loading_indicator_fav)
    ProgressBar pbLoadingIndicatorFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);

        ActionBar actionBar= getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mMoviesCursorAdapter= new MoviesCursorAdapter(this, this);
        StaggeredGridLayoutManager staggeredGridLayoutManager= new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerviewFavmovies.setLayoutManager(staggeredGridLayoutManager);
        recyclerviewFavmovies.setAdapter(mMoviesCursorAdapter);
        recyclerviewFavmovies.hasFixedSize();

        LoaderManager loaderManager= getSupportLoaderManager();
        loaderManager.initLoader(LOADER_FAV_MOVIES_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case LOADER_FAV_MOVIES_ID:
                pbLoadingIndicatorFav.setVisibility(View.VISIBLE);
                String sortOrder= MovieContract.MovieEntry._ID + " ASC";
                return new CursorLoader(this,
                    MovieContract.MovieEntry.CONTENT_URI,
                    MOVIES_PROJECTION,
                    null,
                    null,
                    sortOrder);
            default: throw new RuntimeException("Loader not implemented: " + LOADER_FAV_MOVIES_ID);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMoviesCursorAdapter.swapCursor(data);
        pbLoadingIndicatorFav.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onGridItemClickListener(int movieId) {
        Uri uri= MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movieId)).build();
        Log.v(TAG, "Uri for single movie: " + uri.toString());

        Cursor cursor= getContentResolver().query(
                uri,
                MOVIES_PROJECTION,
                null,
                null,
                null
        );
        if(cursor != null){
            cursor.moveToNext();
            Movie movie= new Movie(cursor.getInt(INDEX_MOVIE_ID), cursor.getString(INDEX_IMAGE_URL), cursor.getString(INDEX_DATE),
                    cursor.getString(INDEX_RATINGS), cursor.getString(INDEX_MOVIE_TITLE), cursor.getString(INDEX_OVERVIEW));
            Intent movieDetailsIntent = new Intent(this, MovieDetailActivity.class);
            movieDetailsIntent.putExtra(getString(R.string.MovieDataDetails), movie);
            startActivity(movieDetailsIntent);
            cursor.close();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
}
