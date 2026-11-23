package com.example.weather.model

data class WeatherData(
    val temperature: Double,   // degrees Celsius
    val windSpeed: Double,     // km/h
    val weatherCode: Int,      // WMO code, e.g. 0 = clear sky
    val time: String           // e.g. "2026-11-19T14:30"
)


