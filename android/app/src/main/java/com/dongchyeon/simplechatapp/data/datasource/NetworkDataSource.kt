package com.dongchyeon.simplechatapp.data.datasource

import com.dongchyeon.simplechatapp.data.model.Image
import okhttp3.MultipartBody
import retrofit2.Response

interface NetworkDataSource {

    suspend fun uploadImage(image: MultipartBody.Part): Response<Image>

}