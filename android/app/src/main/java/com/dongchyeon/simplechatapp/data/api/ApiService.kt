package com.dongchyeon.simplechatapp.data.remote.api

import com.dongchyeon.simplechatapp.data.remote.model.Image
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("/upload")
    suspend fun uploadImage(@Part image: MultipartBody.Part): Response<Image>
}