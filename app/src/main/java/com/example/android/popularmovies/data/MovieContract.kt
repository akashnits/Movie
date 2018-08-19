package com.example.android.popularmovies.data

import android.net.Uri
import android.provider.BaseColumns

object MovieContract {

  val CONTENT_AUTHORITY = "com.example.android.popularmovies"

  val BASE_CONTENT_URI = Uri.parse("content://$CONTENT_AUTHORITY")

  val PATH_MOVIE = "movie"

  class MovieEntry : BaseColumns {
    companion object {

      val CONTENT_URI = BASE_CONTENT_URI.buildUpon()
          .appendPath(PATH_MOVIE)
          .build()

      val TABLE_NAME = "movie"

      val COLUMN_MOVIE_ID = "movieId"

      val COULMN_IMAGE_URL = "imageUrl"

      val COLUMN_DATE = "date"

      val COLUMN_RATINGS = "ratings"

      val COLUMN_MOVIE_TITLE = "movieTitle"

      val COLUMN_OVERVIEW = "overview"

      fun buildUriWithMovieId(_id: Int): Uri {
        return CONTENT_URI.buildUpon()
            .appendPath(Integer.toString(_id))
            .build()
      }
    }
  }
}
