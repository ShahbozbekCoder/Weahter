package com.example.weather.ui.fragment.sunny

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.weather.R
import com.example.weather.data.models.openWeather.WeatherApiData
import com.example.weather.data.remote.WeatherApiInterface
import com.example.weather.databinding.FragmentSunnyScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max

private const val API_ID = "10b8cad8eaba760fafab0969417765e5"

@AndroidEntryPoint
class SunnyScreenFragment : Fragment() {

    private var _binding: FragmentSunnyScreenBinding? = null
    private val binding get() = _binding!!

    //private val sunnyScreenViewModel by viewModels<SunnyScreenViewModel>()
    private var currentCity = "Tashkent"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSunnyScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //sunnyScreenViewModel.getWeatherData("Madrid", API_ID,"metric")
        fetchWeatherData(currentCity)
        //viewModelListener()
        searchCity()
    }

    private fun fetchWeatherData(cityName: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(WeatherApiInterface::class.java)
        val response = retrofit.getWeatherData(cityName, API_ID, "metric")
        response.enqueue(object : Callback<WeatherApiData> {
            override fun onResponse(
                call: Call<WeatherApiData>,
                response: Response<WeatherApiData>
            ) {
                Log.d("TAG", "onResponse: ${response.body()}")
                binding.cityName.text = response.body()?.name ?: "Unknown"
                val sunrise = response.body()?.sys?.sunrise.toString().toLong()
                binding.sunrise.text = "${time(sunrise)}"
                val sunset = response.body()?.sys?.sunset.toString().toLong()
                binding.sunset.text = "${time(sunset)}"
                binding.sea.text = response.body()?.main?.pressure.toString()
                val maxTemp = response.body()?.main?.temp_max.toString()
                binding.maxTemperature.text = "Max Temp: $maxTemp"
                val minTemp = response.body()?.main?.temp_min.toString()
                binding.minTemperature.text = "Min Temp: $minTemp"
                binding.humidityIndex.text = response.body()?.main?.humidity.toString() + " %"
                binding.windSpeed.text = response.body()?.wind?.speed.toString() + " m/s"
                binding.textViewData.text = response.body()?.dt.toString()
                binding.temperature.text = response.body()?.main?.temp.toString()
                binding.weekday.text = getTime(System.currentTimeMillis())
                binding.textViewData.text = date()
                val conditions = response.body()?.weather?.firstOrNull()?.description ?: "Unknown"
                binding.condition.text = conditions
                changeImageAccordingToConditions(response.body()?.weather?.firstOrNull()?.main ?: "Unknown")
            }

            override fun onFailure(call: Call<WeatherApiData>, t: Throwable) {
                Toast.makeText(requireContext(), t.toString(), Toast.LENGTH_SHORT).show()
            }

        })
    }

//    private fun setCity(city:String) {
//        sunnyScreenViewModel.getWeatherData(city, API_ID,"metric")
//    }

    private fun searchCity() {
        val searchCity = binding.searchCity
        searchCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(cityName: String?): Boolean {
                if (cityName != null) {
//                    sunnyScreenViewModel.getWeatherData(cityName, API_ID,"metric")
//                    setCity(cityName)
//                    viewModelListener()
                    fetchWeatherData(cityName)
                    binding.cityName.text = cityName
                } else {
                    Toast.makeText(
                        requireContext(),
                        "This is city doesn't exist",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return true
            }

            override fun onQueryTextChange(cityName: String?): Boolean {
                return true
            }
        })
    }

    private fun changeImageAccordingToConditions(conditions: String) {
        when (conditions) {
            "Haze", "Clouds", "Foggy", "Partly Clouds", "Overcast", "Mist" -> {
                binding.root.setBackgroundResource(R.drawable.cloudly)
                binding.forecast.text = "CLOUD"
                binding.imageView4.setImageResource(R.drawable.cloud)
            }

            "Rain", "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain" -> {
                binding.root.setBackgroundResource(R.drawable.rainly)
                binding.forecast.text = "RAIN"
                binding.imageView4.setImageResource(R.drawable.rain)
            }

            "Snow", "Light Snow", "Moderate Snow", "Heaby Snow", "Blizzard" -> {
                binding.root.setBackgroundResource(R.drawable.snowly)
                binding.forecast.text = "SNOW"
                binding.imageView4.setImageResource(R.drawable.snow)
            }

            "Sunny", "Clear", "Clear sky" -> {
                binding.root.setBackgroundResource(R.drawable.unsplash_1)
                binding.forecast.text = "SUNNY"
                binding.imageView4.setImageResource(R.drawable.sunny)
            }

            else -> {
                binding.root.setBackgroundResource(R.drawable.unsplash_1)
                binding.imageView4.setImageResource(R.drawable.sunny)
            }
        }
    }

    /*    private fun viewModelListener() {
            sunnyScreenViewModel.weatherData.observe(viewLifecycleOwner) { results ->
                binding.cityName.text = results.name
                binding.sunrise.text = results.sys.sunrise.toString()
                binding.sunset.text = results.sys.sunset.toString()
                binding.sea.text = results.cod.toString()
                binding.maxTemperature.text = results.main.temp_max.toString()
                binding.minTemperature.text = results.main.temp_min.toString()
                binding.humidityIndex.text = results.main.humidity.toString()
                binding.windSpeed.text = results.wind.speed.toString()
                binding.textViewData.text = results.dt.toString()
                binding.temperature.text = results.main.temp.toString()
                binding.weekday.text = getTime(System.currentTimeMillis())
                binding.textViewData.text = date()
                binding.cityName.text = currentCity
                val conditions = results.weather[3].description
                binding.condition.text = conditions
                changeImageAccordingToConditions(results.weather[2].main)

            }
            sunnyScreenViewModel.error.observe(viewLifecycleOwner) { error ->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    */
    private fun getTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
    private fun time(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH : mm", Locale.getDefault())
        return sdf.format(Date(timestamp + 1000))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}