package com.example.android.popularmovies.model

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
class TrailerItem {

  @SerializedName("site")
  var site: String? = null

  @SerializedName("size")
  var size: Int = 0

  @SerializedName("iso_3166_1")
  var iso31661: String? = null

  @SerializedName("name")
  var name: String? = null

  @SerializedName("id")
  var id: String? = null

  @SerializedName("type")
  var type: String? = null

  @SerializedName("iso_639_1")
  var iso6391: String? = null

  @SerializedName("key")
  var key: String? = null

  override fun toString(): String {
    return "TrailerItem{site = '$site${'\''.toString()},size = '$size${'\''.toString()},iso_3166_1 = '$iso31661${'\''.toString()},name = '$name${'\''.toString()},id = '$id${'\''.toString()},type = '$type${'\''.toString()},iso_639_1 = '$iso6391${'\''.toString()},key = '$key${'\''.toString()}}"
  }
}