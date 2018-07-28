package com.example.android.popularmovies.networkUtils;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final long REQUEST_TIME_OUT= 60;
    private static OkHttpClient okHttpClient;
    private static final String MOVIES_BASE_URL= "http://api.themoviedb.org/3/movie/";


    public static Retrofit getClient(){
        if(okHttpClient == null)
            initOkHttp();

        Retrofit.Builder retrofitBuilder= new Retrofit.Builder()
                .baseUrl(MOVIES_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return retrofitBuilder.build();
    }

    private static void initOkHttp(){
        OkHttpClient.Builder builder= new OkHttpClient.Builder()
                .connectTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS);

        HttpLoggingInterceptor httpLoggingInterceptor= new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        builder.addInterceptor(httpLoggingInterceptor);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest= chain.request();
                Request.Builder requestBuilder= originalRequest.newBuilder();
                requestBuilder.addHeader("Accept", "application/json")
                        .addHeader("Request-Type", "Android")
                        .addHeader("Content-Type", "application/json");
                return chain.proceed(requestBuilder.build());
            }
        });
        okHttpClient= builder.build();
    }

}
