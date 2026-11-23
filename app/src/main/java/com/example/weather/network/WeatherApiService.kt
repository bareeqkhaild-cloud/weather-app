package com.example.weather.network

import com.example.weather.model.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


class WeatherException(message: String) : Exception(message)


class WeatherApiService {

    suspend fun fetchCurrentWeather(): WeatherData = withContext(Dispatchers.IO) {
        val connection = URL(ENDPOINT).openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "GET"
            connection.connectTimeout = TIMEOUT_MS
            connection.readTimeout = TIMEOUT_MS

            val code = connection.responseCode
            if (code != HttpURLConnection.HTTP_OK) {
                throw WeatherException("The server returned an error (code $code).")
            }
            val body = connection.inputStream.bufferedReader().use { it.readText() }
            parse(body)
        } finally {
            connection.disconnect()
        }
    }

    private fun parse(body: String): WeatherData {
        val current = JSONObject(body).optJSONObject("current_weather")
            ?: throw WeatherException("Weather data is missing from the response.")

        for (key in REQUIRED_KEYS) {
            if (!current.has(key) || current.isNull(key)) {
                throw WeatherException("The response is missing the field \"$key\".")
            }
        }
        return WeatherData(
            temperature = current.getDouble("temperature"),
            windSpeed = current.getDouble("windspeed"),
            weatherCode = current.getInt("weathercode"),
            time = current.getString("time")
        )
    }

    private companion object {
        const val ENDPOINT =
            "https://api.open-meteo.com/v1/forecast?latitude=31.95&longitude=35.91&current_weather=true"
        const val TIMEOUT_MS = 10_000
        val REQUIRED_KEYS = listOf("temperature", "windspeed", "weathercode", "time")
    }
}


