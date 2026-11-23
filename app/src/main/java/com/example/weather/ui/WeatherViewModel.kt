package com.example.weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.WeatherRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class WeatherViewModel(
    private val repository: WeatherRepository = WeatherRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadWeather()
    }


    fun loadWeather() {
        loadJob?.cancel()
        _uiState.value = WeatherUiState.Loading
        loadJob = viewModelScope.launch {
            repository.getCurrentWeather()
                .onSuccess { _uiState.value = WeatherUiState.Success(it) }
                .onFailure {
                    _uiState.value = WeatherUiState.Error(
                        it.message ?: "Something went wrong. Please try again."
                    )
                }
        }
    }
}


