package com.example.weather.util


data class WeatherCondition(val description: String, val icon: String)

object WeatherCodeMapper {
    fun map(code: Int): WeatherCondition = when (code) {
        0 -> WeatherCondition("Clear sky", "☀️")
        1 -> WeatherCondition("Mainly clear", "🌤️")
        2 -> WeatherCondition("Partly cloudy", "⛅")
        3 -> WeatherCondition("Overcast", "☁️")
        45, 48 -> WeatherCondition("Fog", "🌫️")
        51, 53, 55 -> WeatherCondition("Drizzle", "🌦️")
        56, 57 -> WeatherCondition("Freezing drizzle", "🌧️")
        61, 63, 65 -> WeatherCondition("Rain", "🌧️")
        66, 67 -> WeatherCondition("Freezing rain", "🌧️")
        71, 73, 75 -> WeatherCondition("Snowfall", "❄️")
        77 -> WeatherCondition("Snow grains", "❄️")
        80, 81, 82 -> WeatherCondition("Rain showers", "🌦️")
        85, 86 -> WeatherCondition("Snow showers", "🌨️")
        95 -> WeatherCondition("Thunderstorm", "⛈️")
        96, 99 -> WeatherCondition("Thunderstorm with hail", "⛈️")
        else -> WeatherCondition("Unknown", "🌡️")
    }
}


