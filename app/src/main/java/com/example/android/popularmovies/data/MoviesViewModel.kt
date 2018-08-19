package com.example.android.popularmovies.data

import android.app.Application
import android.arch.lifecycle.AndroidViewModel

import com.example.android.popularmovies.model.Response

class MoviesViewModel(application: Application) : AndroidViewModel(application) {

  var response: Response? = null
}
