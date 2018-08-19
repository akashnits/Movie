package com.example.android.popularmovies.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID
import com.example.android.popularmovies.data.MovieContract.MovieEntry.Companion.COLUMN_DATE
import com.example.android.popularmovies.data.MovieContract.MovieEntry.Companion.COLUMN_MOVIE_ID
import com.example.android.popularmovies.data.MovieContract.MovieEntry.Companion.COLUMN_MOVIE_TITLE
import com.example.android.popularmovies.data.MovieContract.MovieEntry.Companion.COLUMN_OVERVIEW
import com.example.android.popularmovies.data.MovieContract.MovieEntry.Companion.COLUMN_RATINGS
import com.example.android.popularmovies.data.MovieContract.MovieEntry.Companion.COULMN_IMAGE_URL

class MovieDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
  companion object {

    val DATABASE_NAME = "movie.db"

    private val DATABASE_VERSION = 2
  }

  override fun onCreate(db: SQLiteDatabase) {
    val SQL_CREATE_MOVIE_TABLE =
      "CREATE TABLE ${MovieContract.MovieEntry.TABLE_NAME} ($_ID INTEGER PRIMARY KEY, $COLUMN_MOVIE_ID INTEGER UNIQUE NOT NULL, $COULMN_IMAGE_URL VARCHAR(20) NOT NULL, $COLUMN_DATE VARCHAR(20), $COLUMN_RATINGS VARCHAR(20), $COLUMN_MOVIE_TITLE VARCHAR(20), $COLUMN_OVERVIEW VARCHAR(900));"

    db.execSQL(SQL_CREATE_MOVIE_TABLE)
  }

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    db.execSQL("DROP TABLE IF EXISTS ${MovieContract.MovieEntry.TABLE_NAME}")
    onCreate(db)
  }
}
