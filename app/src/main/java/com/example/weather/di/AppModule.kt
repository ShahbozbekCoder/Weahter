package com.example.weather.di

import com.example.weather.data.Repository
import com.example.weather.data.remote.RetrofitBuilder
import com.example.weather.data.remote.WeatherApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRemoteRepository(
        weatherApiInterface: WeatherApiInterface
    ) :Repository {
        return Repository(weatherApiInterface)
    }

    @Provides
    @Singleton
    fun provideRemoteRetrofitGetServer(): WeatherApiInterface{
        return RetrofitBuilder.apiInterFaceBuilder()
    }
}