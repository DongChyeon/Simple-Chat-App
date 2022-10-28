package com.dongchyeon.simplechatapp.data.repository

import com.dongchyeon.simplechatapp.data.datasource.NetworkDataSource
import com.dongchyeon.simplechatapp.data.model.Image
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : NetworkRepository {

    override suspend fun uploadImage(image: MultipartBody.Part): Response<Image> =
        networkDataSource.uploadImage(image)

}