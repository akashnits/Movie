package com.example.android.popularmovies;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.popularmovies.adapters.MoviesAdapter;
import com.example.android.popularmovies.data.MoviesViewModel;
import com.example.android.popularmovies.networkUtils.ApiClient;
import com.example.android.popularmovies.networkUtils.ApiService;
import com.example.android.popularmovies.model.Movies;
import com.example.android.popularmovies.model.Response;
import com.example.android.popularmovies.utilities.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.onGridItemClickHandler {


    private MoviesAdapter mMoviesAdapter;
    private ApiService apiService;
    private CompositeDisposable disposable= new CompositeDisposable();
    private MoviesViewModel mMoviesViewModel;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBar;
    @BindView(R.id.recyclerview_movies)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        ButterKnife.bind(this);
        mMoviesAdapter= new MoviesAdapter(this, this);

        StaggeredGridLayoutManager staggeredGridLayoutManager= new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        apiService= ApiClient.getClient().create(ApiService.class);

        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setAdapter(mMoviesAdapter);

        mMoviesViewModel= ViewModelProviders.of(this).get(MoviesViewModel.class);
        if(mMoviesViewModel.getResponse() == null) {
            loadPopularMovies();
        }else {
            String[] movieImageUrls = getImageUrls(mMoviesViewModel.getResponse());
            mMoviesAdapter.setMoviesData(movieImageUrls);
        }
    }

    private Single<String[]> getImageUrlObservable(Response response){
        final String[] moviesImageUrl= getImageUrls(response);

        return Single.create(new SingleOnSubscribe<String[]>() {
            @Override
            public void subscribe(SingleEmitter<String[]> emitter) throws Exception {
                if(!emitter.isDisposed()){
                    emitter.onSuccess(moviesImageUrl);
                }
            }
        });
    }

    private String[] getImageUrls(Response response){
        final String[] moviesImageUrl= new String[response.getResults().size()];

        for(int i=0; i < response.getResults().size(); i++){
            moviesImageUrl[i] = response.getResults().get(i).getPosterPath();
        }
        return moviesImageUrl;
    }

    @Override
    public void onGridItemClick(int position) {
        Movies movie= mMoviesViewModel.getResponse().getResults().get(position);
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
        switch (item.getItemId()){
            case R.id.popularMovies:
                loadPopularMovies();
                break;
            case R.id.topRated:
                loadTopRatedMovies();
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

    private void loadPopularMovies(){
        disposable.add(apiService
                .getPopularMovies(NetworkUtils.key, NetworkUtils.language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Response, SingleSource<String[]>>() {
                    @Override
                    public SingleSource<String[]> apply(Response movie) throws Exception {
                        mMoviesViewModel.setResponse(movie);
                        return getImageUrlObservable(movie);
                    }
                })
                .subscribeWith(new DisposableSingleObserver<String[]>() {
                    @Override
                    public void onSuccess(String[] strings) {
                        mMoviesAdapter.setMoviesData(strings);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void loadTopRatedMovies(){
        disposable.add(apiService
                .getTopRatedMovies( NetworkUtils.key, NetworkUtils.language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Response, SingleSource<String[]>>() {
                    @Override
                    public SingleSource<String[]> apply(Response movie) throws Exception {
                        mMoviesViewModel.setResponse(movie);
                        return getImageUrlObservable(movie);
                    }
                })
                .subscribeWith(new DisposableSingleObserver<String[]>() {
                    @Override
                    public void onSuccess(String[] strings) {
                        mMoviesAdapter.setMoviesData(strings);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
