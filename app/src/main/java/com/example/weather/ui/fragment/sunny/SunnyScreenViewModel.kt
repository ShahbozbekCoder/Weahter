package com.example.weather.ui.fragment.sunny

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.Repository
import com.example.weather.data.models.WeatherApiData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MyTag"
@HiltViewModel
class SunnyScreenViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private var _weatherData = MutableLiveData<WeatherApiData>()
    val weatherData:LiveData<WeatherApiData> get() = _weatherData

    private var _error = MutableLiveData<String>()
    val error:LiveData<String> get() = _error

    fun getWeatherData(city:String) = viewModelScope.launch {
        try {
            repository.getWeather(city).let { response ->
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherData.value = it
                    }
                    Log.d(TAG, "getWeatherData: ${response.body()}")
                } else {
                        _error.value = "Xabarni olishda xatolik"
                }
            }
        }
        catch(e:Exception) {
            Log.d(TAG, "getWeatherData: ${e.message}")
        }
    }

}