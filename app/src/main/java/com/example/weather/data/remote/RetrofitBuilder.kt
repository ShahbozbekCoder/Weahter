package com.example.weather.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//https://api.weatherapi.com/v1/
//https://api.openweathermap.org/data/2.5/
object RetrofitBuilder {
    private const val BASE_URL = "https://api.weatherapi.com/v1/"

    fun apiInterFaceBuilder():WeatherApiInterface {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(WeatherApiInterface::class.java)
    }
}