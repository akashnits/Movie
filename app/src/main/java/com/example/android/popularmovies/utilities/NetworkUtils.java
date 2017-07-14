package com.example.android.popularmovies.utilities;


import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG= NetworkUtils.class.getSimpleName();

    private static final String MOVIES_BASE_URL= "http://api.themoviedb.org/3/movie";

    private static final String format= "json";

    private static final String key= "676eeceb42c83ae99f98f7683077a7de";

    private static final String language= "en-US";


    private final static String QUERY_PARAM= "api_key";
    private final static String LANGUAGE_PARAM= "language";

    public static URL buildUrl(String path){
        Uri builtUri= Uri.parse(MOVIES_BASE_URL + path).buildUpon()
                .appendQueryParameter(QUERY_PARAM, key)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .build();

        URL url= null;
        try{
            url= new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        Log.v(TAG, "Built url: " + url);
        return url;
    }



    public static String getResponseFromHttpUrl(URL url) throws IOException{
        HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scan = new Scanner(in);
            scan.useDelimiter("\\A");

            if(scan.hasNext()){
                return scan.next();
            }else {
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }
}
