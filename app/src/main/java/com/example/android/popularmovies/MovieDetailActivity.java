package com.example.android.popularmovies;

import android.arch.lifecycle.ViewModelProviders;
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
import android.widget.Toast;

import com.example.android.popularmovies.adapters.MoviesAdapter;
import com.example.android.popularmovies.adapters.TrailersAdapter;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.MovieDetailViewModel;
import com.example.android.popularmovies.model.Movies;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.model.TrailerItem;
import com.example.android.popularmovies.networkUtils.ApiClient;
import com.example.android.popularmovies.networkUtils.ApiService;
import com.example.android.popularmovies.utilities.MoviesJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MovieDetailActivity extends AppCompatActivity implements TrailersAdapter.OnTrailerClickHandler {

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


    private Movies movie;
    private int mMovieId;
    private TrailersAdapter mTrailersAdapter;
    private boolean isMarkedFavorite;
    private ApiService apiService;
    private CompositeDisposable disposable= new CompositeDisposable();
    private MovieDetailViewModel mMovieDetailViewModel;

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
        tvMovieName.setText(movie.getTitle());
        Picasso.with(this).load(MoviesAdapter.BASE_IMAGE_URL + movie.getPosterPath()).into(ivDetail);
        tvDate.setText(movie.getReleaseDate().substring(0, 4));
        tvRating.setText(movie.getVoteAverage() + getString(R.string.OutOfTen));
        tvReview.setText(movie.getOverview());
        if (savedInstanceState != null) {
            isMarkedFavorite = savedInstanceState.getBoolean("favoriteBtMarked");
        } else {
            mMovieId = movie.getId();
            markedFavoriteStatus(MovieContract.MovieEntry.CONTENT_URI);
        }
        if(isMarkedFavorite){
            btFavorite.setChecked(true);
        }else {
            btFavorite.setChecked(false);
        }


        apiService= ApiClient.getClient().create(ApiService.class);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerViewTrailers.setLayoutManager(linearLayoutManager);
        recyclerViewTrailers.hasFixedSize();

        mTrailersAdapter = new TrailersAdapter(this, this);
        recyclerViewTrailers.setAdapter(mTrailersAdapter);
        mMovieDetailViewModel= ViewModelProviders.of(this).get(MovieDetailViewModel.class);
        if(mMovieDetailViewModel.getTrailer() == null)
            loadMovieTrailers();
        else {
            mTrailersAdapter.setTrailersData(getTrailerNames(mMovieDetailViewModel.getTrailer()));
        }
    }

    private void loadMovieTrailers(){
        disposable.add(apiService.getMovieTrailers(mMovieId, NetworkUtils.key, NetworkUtils.language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Trailer, SingleSource<String[]>>() {
                    @Override
                    public SingleSource<String[]> apply(Trailer trailer) throws Exception {
                        mMovieDetailViewModel.setTrailer(trailer);
                        return getTrailerNameObservable(trailer);
                    }
                }).subscribeWith(new DisposableSingleObserver<String[]>(){
                    @Override
                    public void onSuccess(String[] strings) {
                        mTrailersAdapter.setTrailersData(strings);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                })
        );
    }

    public Single<String[]> getTrailerNameObservable(final Trailer trailer){
        return Single.create(new SingleOnSubscribe<String[]>() {
            @Override
            public void subscribe(SingleEmitter<String[]> emitter) throws Exception {
                if(!emitter.isDisposed())
                    emitter.onSuccess(getTrailerNames(trailer));
            }
        });
    }

    private String[] getTrailerNames(Trailer trailer){
        String[] trailerNames= new String[trailer.getResults().size()];
        List<TrailerItem> trailerItems= trailer.getResults();
        for(int i=0; i< trailer.getResults().size(); i++){
            trailerNames[i]= trailerItems.get(i).getName();
        }
        return trailerNames;
    }

    @Override
    public void onTrailerItemClickListener(int position) {
        String key = null;
        key= mMovieDetailViewModel.getTrailer().getResults().get(position).getKey();
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
            cv.put(MovieContract.MovieEntry.COULMN_IMAGE_URL, movie.getPosterPath());
            cv.put(MovieContract.MovieEntry.COLUMN_DATE, movie.getReleaseDate());
            cv.put(MovieContract.MovieEntry.COLUMN_RATINGS, movie.getVoteAverage());
            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
            cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());

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
