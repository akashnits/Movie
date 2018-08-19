package com.example.android.popularmovies.model

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
class Response {

  @SerializedName("page")
  var page: Int = 0

  @SerializedName("total_pages")
  var totalPages: Int = 0

  @SerializedName("results")
  var results: List<Movies>? = null

  @SerializedName("total_results")
  var totalResults: Int = 0

  override fun toString(): String {
    return "Response{page = $page,total_pages = $totalPages,results = $results,total_results = $totalResults}"
  }
}