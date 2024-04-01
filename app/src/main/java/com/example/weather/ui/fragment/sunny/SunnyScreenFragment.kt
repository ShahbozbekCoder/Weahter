package com.example.weather.ui.fragment.sunny

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.weather.R
import com.example.weather.databinding.FragmentSunnyScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class SunnyScreenFragment : Fragment() {

    private var _binding: FragmentSunnyScreenBinding? = null
    private val binding get() = _binding!!
    private val sunnyScreenViewModel by viewModels<SunnyScreenViewModel>()
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
        setCity(currentCity)
        viewModelListener()
        searchCity()
    }

    private fun setCity(city:String) {
        sunnyScreenViewModel.getWeatherData(city)
    }

    private fun searchCity() {
        val searchCity = binding.searchCity
        searchCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(cityName: String?): Boolean {
                if (cityName != null) {
                    currentCity = cityName
                    sunnyScreenViewModel.getWeatherData(cityName)
                    setCity(cityName)
                    viewModelListener()
                    binding.cityName.text = cityName
                } else {
                    Toast.makeText(requireContext(), "This is city doesn't exist", Toast.LENGTH_SHORT).show()
                }
                return true
            }
            override fun onQueryTextChange(cityName: String?): Boolean {
                return true
            }
        })
    }

    private fun changeImageAccordingToConditions(conditions:String) {
            when (conditions) {
                "Haze", "Clouds", "Foggy", "Partly Clouds", "Overcast", "Mist" -> {
                    binding.root.setBackgroundResource(R.drawable.cloudly)
                    binding.forecast.text = "CLOUD"
                    binding.imageView4.setImageResource(R.drawable.cloud)
                }
                "Rain" -> {
                    binding.root.setBackgroundResource(R.drawable.rainly)
                    binding.forecast.text = "RAIN"
                    binding.imageView4.setImageResource(R.drawable.rain)
                }
                "Snow" -> {
                    binding.root.setBackgroundResource(R.drawable.snowly)
                    binding.forecast.text = "SNOW"
                    binding.imageView4.setBackgroundResource(R.drawable.snow)
                }
                "Sunny", "Clear", "Clear sky" -> {
                    binding.root.setBackgroundResource(R.drawable.unsplash_1)
                    binding.forecast.text = "SUNNY"
                    binding.imageView4.setBackgroundResource(R.drawable.sunny)
                }
            }
    }

    private fun viewModelListener() {
        sunnyScreenViewModel.weatherData.observe(viewLifecycleOwner) { results ->
            binding.cityName.text = results.location.name//results.name
//            binding.sunrise.text = //results.sys.sunrise.toString()
//            binding.sunset.text = results.sys.sunset.toString()
            binding.sea.text = results.current.pressure_in.toString()//results.cod.toString()
//            binding.maxTemperature.text = //results.main.temp_max.toString()
//            binding.minTemperature.text = results.main.temp_min.toString()
            binding.humidityIndex.text = results.current.humidity.toString()//results.main.humidity.toString()
            binding.windSpeed.text = results.current.wind_kph.toString()//results.wind.speed.toString()
            binding.textViewData.text = results.location.localtime//results.dt.toString()
            binding.temperature.text = results.current.temp_c.toString()//results.main.temp.toString()
            binding.weekday.text = getTime(System.currentTimeMillis())
            binding.textViewData.text = date()
            binding.cityName.text = currentCity
            val conditions = results.current.condition.text
            binding.condition.text = conditions
            changeImageAccordingToConditions(conditions)

        }
        sunnyScreenViewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}