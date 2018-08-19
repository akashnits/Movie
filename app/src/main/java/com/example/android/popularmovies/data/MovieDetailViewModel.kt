package com.example.android.popularmovies.data

import android.app.Application
import android.arch.lifecycle.AndroidViewModel

import com.example.android.popularmovies.model.Trailer

class MovieDetailViewModel(application: Application) : AndroidViewModel(application) {

  var trailer: Trailer? = null
}
