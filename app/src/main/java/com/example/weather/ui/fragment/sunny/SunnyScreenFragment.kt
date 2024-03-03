package com.example.weather.ui.fragment.sunny

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.weather.R
import com.example.weather.databinding.FragmentSunnyScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class SunnyScreenFragment  : Fragment() {

    private var _binding: FragmentSunnyScreenBinding? = null
    private val binding get() = _binding!!
    private val sunnyScreenViewModel: SunnyScreenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSunnyScreenBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ourCity = binding.searchCity.text
        sunnyScreenViewModel.getWeatherData("Tashkent")
        viewModelListener()
    }

    private fun viewModelListener() {
        sunnyScreenViewModel.weatherData.observe(requireActivity()) {results ->
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

        }
        sunnyScreenViewModel.error.observe(requireActivity()) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}