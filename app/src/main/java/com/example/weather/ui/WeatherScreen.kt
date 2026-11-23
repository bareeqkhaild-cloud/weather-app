package com.example.weather.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather.model.WeatherData
import com.example.weather.util.WeatherCodeMapper
import kotlin.math.roundToInt

private val CardColor = Color.White.copy(alpha = 0.2f)

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    WeatherContent(state = state, onRefresh = viewModel::loadWeather)
}

@Composable
private fun WeatherContent(state: WeatherUiState, onRefresh: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF4FC3F7), Color(0xFF1565C0))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Amman", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)

            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                when (state) {
                    WeatherUiState.Loading -> CircularProgressIndicator(color = Color.White)
                    is WeatherUiState.Success -> SuccessView(state.data)
                    is WeatherUiState.Error -> ErrorView(state.message, onRefresh)
                }
            }

            Button(
                onClick = onRefresh,
                enabled = state !is WeatherUiState.Loading,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("Refresh", fontSize = 18.sp)
            }
        }
    }
}

@Composable
private fun SuccessView(data: WeatherData) {
    val condition = WeatherCodeMapper.map(data.weatherCode)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(condition.icon, fontSize = 96.sp)
        Text(
            "${data.temperature.roundToInt()}°C",
            fontSize = 80.sp,
            fontWeight = FontWeight.Light,
            color = Color.White
        )
        Text(condition.description, fontSize = 24.sp, color = Color.White)
        Spacer(Modifier.height(32.dp))
        DetailsCard(windSpeed = data.windSpeed, time = data.time)
    }
}

@Composable
private fun DetailsCard(windSpeed: Double, time: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardColor, RoundedCornerShape(20.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DetailRow(label = "Wind speed", value = "$windSpeed km/h")
        DetailRow(label = "Last updated", value = formatTime(time))
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp)
        Text(value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("⚠️", fontSize = 64.sp)
        Spacer(Modifier.height(12.dp))
        Text(message, color = Color.White, fontSize = 18.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}


private fun formatTime(raw: String): String = raw.replace('T', ' ') + " (GMT)"


