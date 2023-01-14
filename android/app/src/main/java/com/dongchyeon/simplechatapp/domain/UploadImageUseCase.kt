package com.dongchyeon.simplechatapp.domain

import com.dongchyeon.simplechatapp.data.remote.model.Image
import com.dongchyeon.simplechatapp.data.remote.repository.NetworkRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(private val repository: NetworkRepository) {
    suspend operator fun invoke(image: MultipartBody.Part): retrofit2.Response<Image> {
        return repository.uploadImage(image)
    }
}