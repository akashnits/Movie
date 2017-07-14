package com.example.android.popularmovies.utilities;


import android.content.Context;
import android.util.Log;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class MoviesJsonUtils {
    private static final String TAG = MoviesJsonUtils.class.getSimpleName();
    private static JSONArray resultsArray;

    private final static String OWN_RESULTS = "results";
    private final static String OWN_POSTER_PATH = "poster_path";
    private final static String OWN_TRAILER_NAME = "name";


    private static JSONArray resultsArrayTrailers;


    public static String[] getMoviePosterStringsFromJson(Context context, String moviesJSONString) throws JSONException {


        JSONObject moviesJSON = new JSONObject(moviesJSONString);

        resultsArray = moviesJSON.getJSONArray(OWN_RESULTS);

        String[] parsedMoviesData = new String[resultsArray.length()];
        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject moviePath = resultsArray.getJSONObject(i);

            String posterPath = moviePath.getString(OWN_POSTER_PATH);
            parsedMoviesData[i] = posterPath;

            Log.v(TAG, "Poster path is: " + posterPath);
        }
        return parsedMoviesData;
    }

    public static String[] getTrailerStringsFromJSON(String trailersJSONString) throws JSONException {

        JSONObject trailersJSON = new JSONObject(trailersJSONString);
        resultsArrayTrailers = trailersJSON.getJSONArray(OWN_RESULTS);
        String[] trailerName = new String[resultsArrayTrailers.length()];
        for (int i = 0; i < resultsArrayTrailers.length(); i++) {
            JSONObject trailer = resultsArrayTrailers.getJSONObject(i);
            trailerName[i] = trailer.getString(OWN_TRAILER_NAME);

            Log.v(TAG, "Trailer name is: " + trailerName[i]);
        }
        return trailerName;
    }

    public static String getTrailerStringId(int position) throws JSONException {
        JSONObject trailer = resultsArrayTrailers.getJSONObject(position);
        return trailer.getString("key");
    }

    public static ArrayList<Review> getReviewObjectsFromJSON(String reviewJSONString) throws JSONException {
        JSONObject reviewObject = new JSONObject(reviewJSONString);
        JSONArray resultsArrayReviews = reviewObject.getJSONArray(OWN_RESULTS);

        if (resultsArrayReviews == null || resultsArrayReviews.length() == 0) {
            return null;
        }
        ArrayList<Review> reviewsList = new ArrayList<>(resultsArrayReviews.length());
        for (int i = 0; i < resultsArrayReviews.length(); i++) {
            JSONObject reviewDetails = resultsArrayReviews.getJSONObject(i);
            String author= reviewDetails.getString("author");
            String content= reviewDetails.getString("content");
            reviewsList.add(i, new Review(author, content));
        }
        return reviewsList;
    }


    public static Movie getMovieDetailsFromJson(Context context, int position) throws JSONException {
        JSONObject movieObject = resultsArray.getJSONObject(position);
        String imageUrl = movieObject.getString(OWN_POSTER_PATH);
        String date = movieObject.getString(context.getResources().getString(R.string.ReleaseDate));
        String ratings = movieObject.getString(context.getResources().getString(R.string.Vote));
        String title = movieObject.getString(context.getResources().getString(R.string.MovieTitle));
        String review = movieObject.getString(context.getResources().getString(R.string.MovieOverview));
        int id = movieObject.getInt("id");

        return new Movie(imageUrl, date, ratings, title, review, id);
    }


}