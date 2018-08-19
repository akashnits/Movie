package com.example.android.popularmovies.model

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
class Review {

  @SerializedName("id")
  var id: Int = 0

  @SerializedName("page")
  var page: Int = 0

  @SerializedName("total_pages")
  var totalPages: Int = 0

  @SerializedName("results")
  var results: List<ReviewItem>? = null

  @SerializedName("total_results")
  var totalResults: Int = 0

  override fun toString(): String {
    return "Review{id = '$id${'\''.toString()},page = '$page${'\''.toString()},total_pages = '$totalPages${'\''.toString()},results = '$results${'\''.toString()},total_results = '$totalResults${'\''.toString()}}"
  }
}