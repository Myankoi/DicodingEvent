package com.example.dicodingevent.ui.screen.detail

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.remote.response.EventsItem
import com.example.dicodingevent.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class DetailViewModel : ViewModel() {
    private val apiService = ApiConfig.getApiService()

    var isLoading by mutableStateOf(false)
    var isConnectedToInternet by mutableStateOf(true)

    private val _event = MutableStateFlow<EventsItem?>(null)
    val event: StateFlow<EventsItem?> = _event

    fun getEventById(eventId: String) {
        isLoading = true
        viewModelScope.launch {
            try {
                val response =
                    apiService.getEventById(id = eventId).awaitResponse()

                if (response.isSuccessful) {
                    _event.value = response.body()?.event
                }
                isConnectedToInternet = true
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error fetching event", e)
                isConnectedToInternet = false

            } finally {
                isLoading = false
            }
        }
    }
}