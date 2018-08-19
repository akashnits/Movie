package com.example.android.popularmovies.model

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
class Trailer {

  @SerializedName("id")
  var id: Int = 0

  @SerializedName("results")
  var results: List<TrailerItem>? = null

  override fun toString(): String {
    return "Trailer{id = '$id${'\''.toString()},results = '$results${'\''.toString()}}"
  }
}