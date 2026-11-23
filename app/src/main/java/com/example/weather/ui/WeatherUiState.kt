package com.example.weather.ui

import com.example.weather.model.WeatherData


sealed interface WeatherUiState {
    data object Loading : WeatherUiState
    data class Success(val data: WeatherData) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}

