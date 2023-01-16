package com.dongchyeon.simplechatapp.data.datasource

import com.dongchyeon.simplechatapp.data.api.ApiService
import com.dongchyeon.simplechatapp.data.model.Image
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun uploadImage(image: MultipartBody.Part): Response<Image> =
        withContext(ioDispatcher) {
            return@withContext apiService.uploadImage(image)
        }
}