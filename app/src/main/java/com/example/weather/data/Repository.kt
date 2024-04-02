package com.example.weather.data

import com.example.weather.data.remote.WeatherApiInterface
import javax.inject.Inject

class Repository @Inject constructor(private val weatherApiInterface: WeatherApiInterface) {

    suspend fun getWeather(city: String, appid: String, units: String) =
        weatherApiInterface.getWeatherData(city, appid, units)

    //suspend fun getWeather(city:String) = weatherApiInterface.getWeatherApi(city)

}