package com.example.android.popularmovies.networkUtils;

import com.example.android.popularmovies.pojo.Response;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("popular")
    public Single<Response> getPopularMovies(@Query("api_key") String apiKey, @Query("language") String language);

    @GET("top_rated")
    public Single<Response> getTopRatedMovies(@Query("api_key") String apiKey, @Query("language") String language);

}
