package com.example.android.popularmovies.data

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri

class MovieProvider : ContentProvider() {

  private var mDbHelper: SQLiteOpenHelper? = null

  companion object {

    val CODE_MOVIE = 100
    val CODE_MOVIE_ID = 101

    private val sUriMatcher = buildUriMatcher()

    private fun buildUriMatcher(): UriMatcher {
      val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
      uriMatcher.addURI(
          MovieContract.CONTENT_AUTHORITY,
          MovieContract.PATH_MOVIE, CODE_MOVIE
      )
      uriMatcher.addURI(
          MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE + "/#", CODE_MOVIE_ID
      )
      return uriMatcher
    }
  }

  override fun onCreate(): Boolean {
    mDbHelper = MovieDbHelper(context!!)
    return true
  }

  override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
    var selectionArgs = selectionArgs
    var cursor: Cursor? = null

    when (sUriMatcher.match(uri)) {
      CODE_MOVIE -> cursor = mDbHelper!!.readableDatabase.query(
          MovieContract.MovieEntry.TABLE_NAME,
          projection,
          selection,
          selectionArgs, null, null,
          sortOrder
      )
      CODE_MOVIE_ID -> {
        //to be implemented
        selectionArgs = arrayOf(uri.lastPathSegment)
        cursor = mDbHelper!!.readableDatabase.query(
            MovieContract.MovieEntry.TABLE_NAME,
            projection,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
            selectionArgs, null, null,
            sortOrder
        )
      }
      else -> throw UnsupportedOperationException("Unknown uri: $uri")
    }
    cursor!!.setNotificationUri(context!!.contentResolver, uri)
    return cursor
  }

  override fun getType(uri: Uri): String? {
    return null
  }

  override fun insert(uri: Uri, values: ContentValues?): Uri? {
    val db = mDbHelper!!.writableDatabase
    var id: Long = 0
    var returnUri: Uri? = null
    when (sUriMatcher.match(uri)) {
      CODE_MOVIE -> try {
        id = db.insertOrThrow(MovieContract.MovieEntry.TABLE_NAME, null, values)
        if (id > 0)
          returnUri = ContentUris.withAppendedId(
              MovieContract.MovieEntry.CONTENT_URI, id
          )
      } catch (e: SQLException) {
        e.printStackTrace()
      }

      else -> throw UnsupportedOperationException("Unknown uri: $uri")
    }
    if (id == -1L)
      return null

    context!!.contentResolver.notifyChange(uri, null)
    return returnUri
  }

  override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
    val db = mDbHelper!!.writableDatabase
    var rowsDeleted = 0
    when (sUriMatcher.match(uri)) {
      CODE_MOVIE -> try {
        rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs)
      } catch (s: SQLException) {
        s.printStackTrace()
      }

    }
    return rowsDeleted
  }

  override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
    return 0
  }
}
