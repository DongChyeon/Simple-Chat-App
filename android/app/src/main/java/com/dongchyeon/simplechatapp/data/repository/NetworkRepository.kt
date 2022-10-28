package com.dongchyeon.simplechatapp.data.repository

import com.dongchyeon.simplechatapp.data.model.Image
import okhttp3.MultipartBody
import retrofit2.Response

interface NetworkRepository {

    suspend fun uploadImage(image: MultipartBody.Part): Response<Image>

}
