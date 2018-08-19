package com.example.android.popularmovies.networkUtils

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

import java.io.IOException
import java.util.concurrent.TimeUnit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

  private val REQUEST_TIME_OUT: Long = 60
  private var okHttpClient: OkHttpClient? = null
  private val MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/"

  val client: Retrofit
    get() {
      if (okHttpClient == null)
        initOkHttp()

      val retrofitBuilder = Retrofit.Builder()
          .baseUrl(MOVIES_BASE_URL)
          .client(okHttpClient!!)
          .addConverterFactory(GsonConverterFactory.create())
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      return retrofitBuilder.build()
    }

  private fun initOkHttp() {
    val builder = OkHttpClient.Builder()
        .connectTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)

    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    builder.addInterceptor(httpLoggingInterceptor)
    builder.addInterceptor { chain ->
      val originalRequest = chain.request()
      val requestBuilder = originalRequest.newBuilder()
      requestBuilder.addHeader("Accept", "application/json")
          .addHeader("Request-Type", "Android")
          .addHeader("Content-Type", "application/json")
      chain.proceed(requestBuilder.build())
    }
    okHttpClient = builder.build()
  }

}
