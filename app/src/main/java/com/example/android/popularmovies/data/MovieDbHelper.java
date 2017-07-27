package com.example.android.popularmovies.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME= "movie.db";

    private static final int DATABASE_VERSION= 2;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE=
                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                        MovieContract.MovieEntry.COULMN_IMAGE_URL + " VARCHAR(20) NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_DATE + " VARCHAR(20), " +
                        MovieContract.MovieEntry.COLUMN_RATINGS + " VARCHAR(20), " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " VARCHAR(20), " +
                        MovieContract.MovieEntry.COLUMN_OVERVIEW + " VARCHAR(900) " +
                        ");";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
