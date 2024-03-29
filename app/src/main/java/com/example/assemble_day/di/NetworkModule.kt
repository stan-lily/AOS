package com.example.assemble_day.di

import com.example.assemble_day.data.remote.dataSource.*
import com.example.assemble_day.data.remote.network.AssembleApi
import com.example.assemble_day.data.remote.network.LabelApi
import com.example.assemble_day.data.remote.network.TargetApi
import com.example.assemble_day.data.remote.repository.AssembleRepositoryImpl
import com.example.assemble_day.data.remote.repository.LabelRepositoryImpl
import com.example.assemble_day.data.remote.repository.TargetRepositoryImpl
import com.example.assemble_day.domain.repository.AssembleRepository
import com.example.assemble_day.domain.repository.LabelRepository
import com.example.assemble_day.domain.repository.TargetRepository
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

    private const val BASE_URL = "http://3.35.229.71:8080/"

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
    fun provideAssembleApi(retrofit: Retrofit): AssembleApi {
        return retrofit.create(AssembleApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTargetApi(retrofit: Retrofit): TargetApi {
        return retrofit.create(TargetApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLabelDataSource(labelApi: LabelApi): LabelDataSource {
        return LabelRemoteDataSource(labelApi)
    }

    @Provides
    @Singleton
    fun provideLabelRepository(labelDataSource: LabelDataSource): LabelRepository {
        return LabelRepositoryImpl(labelDataSource)
    }

    @Provides
    @Singleton
    fun provideAssembleDataSource(assembleApi: AssembleApi): AssembleDataSource {
        return AssembleRemoteDataSource(assembleApi)
    }

    @Provides
    @Singleton
    fun provideAssembleRepository(assembleDataSource: AssembleDataSource): AssembleRepository {
        return AssembleRepositoryImpl(assembleDataSource)
    }

    @Provides
    @Singleton
    fun provideTargetDataSource(targetApi: TargetApi): TargetDataSource {
        return TargetRemoteDataSource(targetApi)
    }

    @Provides
    @Singleton
    fun provideTargetRepository(targetDataSource: TargetDataSource): TargetRepository {
        return TargetRepositoryImpl(targetDataSource)
    }

}