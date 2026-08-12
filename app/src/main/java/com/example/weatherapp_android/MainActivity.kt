package com.example.weatherapp_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.repository.WeatherRepository
import kotlinx.coroutines.launch
import java.io.IOException

class MainActivity : AppCompatActivity() {

    // ===== YOUR UI ELEMENTS =====
    private lateinit var cityInput: EditText
    private lateinit var searchBtn: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var weatherContainer: LinearLayout
    private lateinit var cityName: TextView
    private lateinit var temperature: TextView
    private lateinit var condition: TextView
    private lateinit var humidity: TextView
    private lateinit var windSpeed: TextView
    private lateinit var errorMessage: TextView
    private lateinit var weatherIcon: ImageView

    private val repository = WeatherRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ===== INITIALIZE ALL UI COMPONENTS =====
        cityInput = findViewById(R.id.cityInput)
        searchBtn = findViewById(R.id.searchBtn)
        progressBar = findViewById(R.id.progressBar)
        weatherContainer = findViewById(R.id.weatherContainer)
        cityName = findViewById(R.id.cityName)
        temperature = findViewById(R.id.temperature)
        condition = findViewById(R.id.condition)
        humidity = findViewById(R.id.humidity)
        windSpeed = findViewById(R.id.windSpeed)
        errorMessage = findViewById(R.id.errorMessage)
        weatherIcon = findViewById(R.id.weatherIcon)

        // ===== SEARCH BUTTON CLICK LISTENER =====
        searchBtn.setOnClickListener {
            handleSearch()
        }
    }

    // ======================================
    // MEMBER 1: UI STATE HANDLING METHODS
    // ======================================

    /**
     * Handle Search Button Click
     * - Validate input (empty check)
     * - Show/hide UI states
     * - Trigger API request (Member 3)
     */
    private fun handleSearch() {
        val city = cityInput.text.toString().trim()

        // ===== CASE 1: Empty City Name =====
        if (city.isEmpty()) {
            showError("Please enter a city name! 🌧️")
            Toast.makeText(this, "City name cannot be empty", Toast.LENGTH_SHORT).show()
            hideWeather()
            return
        }

        // Clear any previous errors
        hideError()

        // ===== SHOW LOADING STATE =====
        showLoading()

        // Hide previous weather
        hideWeather()

        // ===== TRIGGER API REQUEST =====
        lifecycleScope.launch {
            try {
                val response = repository.fetchWeather(city)

                if (response.isSuccessful) {
                    val weather = response.body()
                    if (weather != null) {
                        showWeather(
                            city = weather.cityName,
                            temp = weather.main.temp.toInt().toString(),
                            cond = weather.weather.firstOrNull()?.description ?: "Unknown",
                            hum = weather.main.humidity.toString(),
                            wind = weather.wind.speed.toString()
                        )
                    } else {
                        showError("No data received. Please try again.")
                    }
                } else {
                    // ===== CASE 2: Invalid City (404) =====
                    // ===== CASE 4: Other API Errors =====
                    when (response.code()) {
                        404 -> showError("City not found. Please check the spelling.")
                        401 -> showError("API authentication failed.")
                        in 500..599 -> showError("Weather service is currently unavailable.")
                        else -> showError("Something went wrong (code ${response.code()}).")
                    }
                }
            } catch (e: IOException) {
                // ===== CASE 3: Network Error =====
                showError("No internet connection. Please check your network.")
            } catch (e: Exception) {
                showError("Unexpected error: ${e.localizedMessage}")
            }
        }
    }

    // ======================================
    // UI STATE METHODS (Your Main Work)
    // ======================================

    /**
     * Show loading indicator
     * Hide weather and error while loading
     */
    fun showLoading() {
        progressBar.visibility = View.VISIBLE
        searchBtn.isEnabled = false
        searchBtn.text = "Loading..."
        weatherContainer.visibility = View.GONE
        errorMessage.visibility = View.GONE
    }

    /**
     * Hide loading indicator
     * Re-enable search button
     */
    fun hideLoading() {
        progressBar.visibility = View.GONE
        searchBtn.isEnabled = true
        searchBtn.text = "🔍 Search Weather"
    }

    /**
     * Show weather data in UI
     * Called after parsing API response
     */
    fun showWeather(
        city: String,
        temp: String,
        cond: String,
        hum: String,
        wind: String
    ) {
        hideLoading()
        hideError()

        cityName.text = "📍 $city"
        temperature.text = "🌡️ $temp°C"
        condition.text = "☁️ $cond"
        humidity.text = "💧 $hum%"
        windSpeed.text = "💨 $wind km/h"

        weatherContainer.visibility = View.VISIBLE
    }

    /**
     * Hide weather display area
     */
    fun hideWeather() {
        weatherContainer.visibility = View.GONE
    }

    /**
     * Show error message in TextView
     * Used for all error cases
     */
    fun showError(message: String) {
        hideLoading()
        hideWeather()

        errorMessage.text = "⚠️ $message"
        errorMessage.visibility = View.VISIBLE
    }

    /**
     * Hide error message
     */
    fun hideError() {
        errorMessage.visibility = View.GONE
    }
}