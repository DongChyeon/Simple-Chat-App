package com.dongchyeon.simplechatapp.presentation.di

import com.dongchyeon.simplechatapp.data.remote.repository.NetworkRepository
import com.dongchyeon.simplechatapp.domain.UploadImageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Singleton
    @Provides
    fun providesUploadImageUseCase(networkRepository: NetworkRepository): UploadImageUseCase {
        return UploadImageUseCase(networkRepository)
    }
}