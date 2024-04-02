package com.example.weather.data.remote

import com.example.weather.data.models.openWeather.WeatherApiData
import com.example.weather.data.models.weatherApi.DataWeather
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val API_ID = "10b8cad8eaba760fafab0969417765e5"

//https://api.weatherapi.com/v1/current.json?key=bb6e6c28b3434bcc8f192036243103&q=London&aqi=no
interface WeatherApiInterface {

    @GET("weather")
     fun getWeatherData(
        @Query("q") city: String,
        @Query("appid") appid: String,
        @Query("units") units: String
    ): Call<WeatherApiData>

//    @GET("current.json?key=bb6e6c28b3434bcc8f192036243103&q=q&aqi=no")
//    suspend fun getWeatherApi(
//        @Path("q") current: String
//    ): Response<DataWeather>
}