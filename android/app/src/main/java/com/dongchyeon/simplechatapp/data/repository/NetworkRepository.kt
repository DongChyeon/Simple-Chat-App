package com.dongchyeon.simplechatapp.data.repository

import com.dongchyeon.simplechatapp.data.api.ImageUploadService
import okhttp3.MultipartBody
import javax.inject.Inject

class NetworkRepository @Inject constructor(private val imageUploadService: ImageUploadService) {

    suspend fun uploadImage(image: MultipartBody.Part) = imageUploadService.uploadImage(image)

}