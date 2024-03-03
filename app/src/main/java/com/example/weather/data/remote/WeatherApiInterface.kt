package com.example.weather.data.remote

import com.example.weather.data.models.WeatherApiData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_ID = "10b8cad8eaba760fafab0969417765e5"

interface WeatherApiInterface {

    @GET("weather?q=city&appid=$API_ID")
    suspend fun getWeatherData(
        @Query("city") city:String
    ): Response<WeatherApiData>

}