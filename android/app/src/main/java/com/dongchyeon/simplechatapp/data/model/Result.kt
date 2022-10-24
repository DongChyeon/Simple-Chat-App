package com.dongchyeon.simplechatapp.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("result")
    @Expose
    val result: Int,

    @SerializedName("imageUri")
    @Expose
    val imageUri: String
)