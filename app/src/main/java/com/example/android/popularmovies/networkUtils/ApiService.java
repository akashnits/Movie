package com.example.android.popularmovies.networkUtils;

import com.example.android.popularmovies.model.Response;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("popular")
    public Single<Response> getPopularMovies(@Query("api_key") String apiKey, @Query("language") String language);

    @GET("top_rated")
    public Single<Response> getTopRatedMovies(@Query("api_key") String apiKey, @Query("language") String language);

    @GET("{id}/videos")
    Single<Trailer> getMovieTrailers(@Path("id") int id, @Query("api_key") String apiKey, @Query("language") String language);

    @GET("{id}/reviews")
    Single<Review> getMovieReviews(@Path("id") int id, @Query("api_key") String apiKey, @Query("language") String language);
}
