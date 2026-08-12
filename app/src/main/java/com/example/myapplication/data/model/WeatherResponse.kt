package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

/**
 * Root response object from OpenWeatherMap API
 */
data class WeatherResponse(
    @SerializedName("name")
    val cityName: String,

    @SerializedName("main")
    val main: MainData,

    @SerializedName("weather")
    val weather: List<WeatherDescription>,

    @SerializedName("wind")
    val wind: WindData
)

data class MainData(
    @SerializedName("temp")
    val temp: Double,

    @SerializedName("humidity")
    val humidity: Int
)

data class WeatherDescription(
    @SerializedName("description")
    val description: String,

    @SerializedName("icon")
    val icon: String
)

data class WindData(
    @SerializedName("speed")
    val speed: Double
)