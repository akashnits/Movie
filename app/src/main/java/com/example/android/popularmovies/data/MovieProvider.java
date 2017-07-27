package com.example.android.popularmovies.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieProvider extends ContentProvider{

    private SQLiteOpenHelper mDbHelper;

    public static final int CODE_MOVIE= 100;
    public static final int CODE_MOVIE_ID= 101;

    private static final UriMatcher sUriMatcher= buildUriMatcher();



    @Override
    public boolean onCreate() {
        mDbHelper= new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor=null;

        switch (sUriMatcher.match(uri)){
            case CODE_MOVIE:
                cursor= mDbHelper.getReadableDatabase().query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_MOVIE_ID:
                //to be implemented
                selectionArgs= new String[]{uri.getLastPathSegment()};
                cursor= mDbHelper.getReadableDatabase().query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db= mDbHelper.getWritableDatabase();
        long id=0;
        Uri returnUri=null;
        switch (sUriMatcher.match(uri)){
            case CODE_MOVIE:
                try {
                     id = db.insertOrThrow(MovieContract.MovieEntry.TABLE_NAME, null, values);
                    if(id > 0)
                        returnUri= ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                }catch (SQLException e){
                    e.printStackTrace();
                }
                break;
            default: throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(id == -1)
            return null;

        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db= mDbHelper.getWritableDatabase();
        int rowsDeleted=0 ;
        switch (sUriMatcher.match(uri)){
            case CODE_MOVIE:
                try{
                    rowsDeleted= db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                }catch (SQLException s){
                    s.printStackTrace();
                }
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    private static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher= new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE, CODE_MOVIE);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE + "/#", CODE_MOVIE_ID);
        return uriMatcher;
    }
}
