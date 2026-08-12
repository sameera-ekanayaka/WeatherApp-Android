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
     */
    private fun handleSearch() {
        val city = cityInput.text.toString().trim()

        // ===== CASE 1: Empty City Name =====
        if (city.isEmpty()) {
            // Show error in TextView
            showError("Please enter a city name! 🌧️")

            // Also show Toast
            Toast.makeText(this, "City name cannot be empty", Toast.LENGTH_SHORT).show()

            // Hide weather display
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
        // Member 2 & 3 will implement this method
        // For now, show a message
        Toast.makeText(this, "Searching for: $city", Toast.LENGTH_SHORT).show()

        // For testing UI states, you can simulate a response:
        // testWeatherDisplay()
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
     * Called by Member 3 after parsing API response
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

        // Populate data
        cityName.text = "📍 $city"
        temperature.text = "🌡️ $temp°C"
        condition.text = "☁️ $cond"
        humidity.text = "💧 $hum%"
        windSpeed.text = "💨 $wind km/h"

        // Show weather container
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

    // ======================================
    // FOR TESTING UI (Remove later)
    // ======================================

    /**
     * Test method to preview weather display
     * Can be called from handleSearch() for testing
     */
    private fun testWeatherDisplay() {
        showWeather(
            city = "Colombo",
            temp = "29",
            cond = "Cloudy",
            hum = "78",
            wind = "12"
        )
    }
}