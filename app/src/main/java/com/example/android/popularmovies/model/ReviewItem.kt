package com.example.android.popularmovies.model

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
class ReviewItem {

  @SerializedName("author")
  var author: String? = null

  @SerializedName("id")
  var id: String? = null

  @SerializedName("content")
  var content: String? = null

  @SerializedName("url")
  var url: String? = null

  override fun toString(): String {
    return "ReviewItem{author = '$author${'\''.toString()},id = '$id${'\''.toString()},content = '$content${'\''.toString()},url = '$url${'\''.toString()}}"
  }
}