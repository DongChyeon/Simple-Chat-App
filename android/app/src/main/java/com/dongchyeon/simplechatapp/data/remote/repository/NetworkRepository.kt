package com.dongchyeon.simplechatapp.data.remote.repository

import com.dongchyeon.simplechatapp.data.remote.datasource.NetworkDataSource
import com.dongchyeon.simplechatapp.data.remote.model.Image
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource
) {
    suspend fun uploadImage(image: MultipartBody.Part): Response<Image> =
        networkDataSource.uploadImage(image)
}