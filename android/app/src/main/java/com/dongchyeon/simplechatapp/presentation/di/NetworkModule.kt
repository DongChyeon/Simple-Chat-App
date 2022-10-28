package com.dongchyeon.simplechatapp.presentation.di

import com.dongchyeon.simplechatapp.BuildConfig
import com.dongchyeon.simplechatapp.data.api.ApiService
import com.dongchyeon.simplechatapp.data.datasource.NetworkDataSource
import com.dongchyeon.simplechatapp.data.datasource.NetworkDataSourceImpl
import com.dongchyeon.simplechatapp.data.repository.NetworkRepository
import com.dongchyeon.simplechatapp.data.repository.NetworkRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient.Builder().build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun providesNetworkDataSource(
        apiService: ApiService,
        dispatcher: CoroutineDispatcher
    ): NetworkDataSource {
        return NetworkDataSourceImpl(apiService, dispatcher)
    }

    @Singleton
    @Provides
    fun providesNetworkRepository(networkDataSource: NetworkDataSource): NetworkRepository {
        return NetworkRepositoryImpl(networkDataSource)
    }
}
