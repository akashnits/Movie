package com.example.android.popularmovies.data;


import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String CONTENT_AUTHORITY= "com.example.android.popularmovies";

    public static final Uri BASE_CONTENT_URI= Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_MOVIE= "movie";

    public static class MovieEntry implements BaseColumns{

        public static final Uri CONTENT_URI= BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String TABLE_NAME= "movie";


        public static final String COLUMN_MOVIE_ID= "movieId";

        public static final String COULMN_IMAGE_URL= "imageUrl";

        public static final String COLUMN_DATE= "date";

        public static final String COLUMN_RATINGS= "ratings";

        public static final String COLUMN_MOVIE_TITLE= "movieTitle";

        public static final String COLUMN_OVERVIEW= "overview";


        public static Uri buildUriWithMovieId(int _id){
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(_id))
                    .build();
        }
    }
}
