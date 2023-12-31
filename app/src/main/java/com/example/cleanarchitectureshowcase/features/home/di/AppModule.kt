package com.example.cleanarchitectureshowcase.features.home.di

import com.example.cleanarchitectureshowcase.features.home.data.DataRepositoryImpl
import com.example.cleanarchitectureshowcase.features.home.data.ServerDataApi
import com.example.cleanarchitectureshowcase.features.home.domain.CalculatingHelper
import com.example.cleanarchitectureshowcase.features.home.domain.CalculatingHelperImpl
import com.example.cleanarchitectureshowcase.features.home.domain.DataRepository
import com.example.cleanarchitectureshowcase.features.home.domain.UserSearchHistory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideServerDataApi(): ServerDataApi {
        return Retrofit.Builder()
            .baseUrl(ServerDataApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServerDataApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDataRepository(api: ServerDataApi): DataRepository {
        return DataRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideCalculatingObject(): CalculatingHelper {
        return CalculatingHelperImpl()
    }
    @Provides
    @Singleton
    fun provideUserSearchHistory(): UserSearchHistory{
        return UserSearchHistory()
    }
}
