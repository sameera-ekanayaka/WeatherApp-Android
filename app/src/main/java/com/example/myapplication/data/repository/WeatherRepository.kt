package com.example.weatherapp.data.repository

import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.network.RetrofitClient
import retrofit2.Response

class WeatherRepository {

    // Paste your active OpenWeatherMap API key here
    private val apiKey = "ab857b5b1b3d720fc887647f498b2608"

    suspend fun fetchWeather(cityName: String): Response<WeatherResponse> {
        return RetrofitClient.apiService.getCurrentWeather(
            cityName = cityName,
            units = "metric",
            apiKey = apiKey
        )
    }
}