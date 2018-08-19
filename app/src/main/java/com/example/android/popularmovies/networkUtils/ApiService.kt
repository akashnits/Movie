package com.example.android.popularmovies.networkUtils

import com.example.android.popularmovies.model.Response
import com.example.android.popularmovies.model.Review
import com.example.android.popularmovies.model.Trailer

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

  @GET("popular")
  fun getPopularMovies(
    @Query("api_key") apiKey: String, @Query("language") language: String): Single<Response>

  @GET("top_rated")
  fun getTopRatedMovies(@Query("api_key") apiKey: String, @Query("language") language: String): Single<Response>

  @GET("{id}/videos")
  fun getMovieTrailers(@Path("id") id: Int, @Query("api_key") apiKey: String, @Query("language") language: String): Single<Trailer>

  @GET("{id}/reviews")
  fun getMovieReviews(@Path("id") id: Int, @Query("api_key") apiKey: String, @Query("language") language: String): Single<Review>
}
