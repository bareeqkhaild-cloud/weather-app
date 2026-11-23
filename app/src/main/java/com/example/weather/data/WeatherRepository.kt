package com.example.weather.data

import com.example.weather.model.WeatherData
import com.example.weather.network.WeatherApiService
import com.example.weather.network.WeatherException
import kotlinx.coroutines.CancellationException
import org.json.JSONException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class WeatherRepository(
    private val api: WeatherApiService = WeatherApiService()
) {
    suspend fun getCurrentWeather(): Result<WeatherData> = try {
        Result.success(api.fetchCurrentWeather())
    } catch (e: CancellationException) {
        throw e // never swallow coroutine cancellation
    } catch (e: UnknownHostException) {
        fail("No internet connection. Please check your network and try again.")
    } catch (e: SocketTimeoutException) {
        fail("The request timed out. Please try again.")
    } catch (e: IOException) {
        fail("A network error occurred. Please try again.")
    } catch (e: WeatherException) {
        Result.failure(e)
    } catch (e: JSONException) {
        fail("The server sent an unexpected response.")
    } catch (e: Exception) {
        fail("Something went wrong. Please try again.")
    }

    private fun fail(message: String): Result<WeatherData> =
        Result.failure(WeatherException(message))
}


