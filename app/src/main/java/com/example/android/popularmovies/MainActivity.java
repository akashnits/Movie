package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.popularmovies.adapters.MoviesAdapter;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utilities.MoviesJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String[]>, MoviesAdapter.onGridItemClickHandler {


    private MoviesAdapter mMoviesAdapter;
    private static final int FORECAST_LOADER_ID = 0;
    private String moviesJsonString= null;
    @BindView(R.id.pb_loading_indicator)  ProgressBar mProgressBar;
    @BindView(R.id.recyclerview_movies)  RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        ButterKnife.bind(this);
        mMoviesAdapter= new MoviesAdapter(this, this);

      StaggeredGridLayoutManager staggeredGridLayoutManager= new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setAdapter(mMoviesAdapter);
        LoaderManager loaderManager= getSupportLoaderManager();
        loaderManager.initLoader(FORECAST_LOADER_ID, null, this);
    }

    @Override
    public Loader<String[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String[]>(this) {
            String[] mMoviesData= null;
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(mMoviesData == null){
                mProgressBar.setVisibility(View.VISIBLE);
                forceLoad();
                }
                else{
                deliverResult(mMoviesData);
                }
            }

            @Override
            public String[] loadInBackground() {
                URL url= null;
                if(args != null) {
                    url = NetworkUtils.buildUrl(args.getString(getResources().getString(R.string.urlPath)));
                }else {
                    url= NetworkUtils.buildUrl(getResources().getString(R.string.popularMovies));
                }

                try{
                    moviesJsonString= NetworkUtils.getResponseFromHttpUrl(url);
                    String[] simpleJsonMoviesData= MoviesJsonUtils.getMoviePosterStringsFromJson(MainActivity.this, moviesJsonString);
                    return simpleJsonMoviesData;
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(String[] data) {
                mMoviesData= data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mMoviesAdapter.setMoviesData(data);
    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

    }

    @Override
    public void onGridItemClick(int position) {
        Movie movie= null;
        try {
            movie = MoviesJsonUtils.getMovieDetailsFromJson(this, position);
        }catch (JSONException e){
            e.printStackTrace();
        }
        Intent movieDetailsIntent = new Intent(this, MovieDetailActivity.class);
        movieDetailsIntent.putExtra(getString(R.string.MovieDataDetails), movie);
        startActivity(movieDetailsIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle args=new Bundle();
        switch (item.getItemId()){
            case R.id.popularMovies:
                args.putString(getString(R.string.urlPath), getString(R.string.popularMovies));
                getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, args, MainActivity.this);
                break;
            case R.id.topRated:
                args.putString(getString(R.string.urlPath), getString(R.string.topRated));
                getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, args, MainActivity.this);
                break;
            case R.id.favoriteMovies:
                Intent intent= new Intent(this, FavoritesActivity.class);
                startActivity(intent);
                Log.v("MainActivity", "Favorites intent launched");
                break;
            default:break;
        }
        return true;
    }
}
