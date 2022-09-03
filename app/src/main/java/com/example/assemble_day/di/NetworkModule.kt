package com.example.assemble_day.di

import com.example.assemble_day.data.remote.dataSource.LabelDataSource
import com.example.assemble_day.data.remote.dataSource.LabelRemoteDataSource
import com.example.assemble_day.data.remote.network.LabelApi
import com.example.assemble_day.data.remote.repository.LabelFilterRepositoryImpl
import com.example.assemble_day.domain.repository.LabelFilterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://cfd1d0a7-b24d-4933-a688-15fa8308ce12.mock.pstmn.io/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideLabelApi(retrofit: Retrofit): LabelApi {
        return retrofit.create(LabelApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLabelDataSource(labelApi: LabelApi): LabelDataSource {
        return LabelRemoteDataSource(labelApi)
    }

    @Provides
    @Singleton
    fun provideLabelFilterRepository(labelDataSource: LabelDataSource): LabelFilterRepository {
        return LabelFilterRepositoryImpl(labelDataSource)
    }

}