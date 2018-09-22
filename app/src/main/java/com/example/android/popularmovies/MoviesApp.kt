package com.example.android.popularmovies

import android.app.Application
import android.content.Context

class MoviesApp : Application() {
  companion object {

    operator fun get(context: Context): MoviesApp {
      return context.applicationContext as MoviesApp
    }
  }

}
