package com.dongchyeon.simplechatapp.data.model

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("imageUri")
    val imageUri: String
)