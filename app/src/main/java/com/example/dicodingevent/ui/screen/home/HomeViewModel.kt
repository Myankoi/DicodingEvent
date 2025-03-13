package com.example.dicodingevent.ui.screen.home

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

class HomeViewModel : ViewModel() {
    private val apiService = ApiConfig.getApiService()

    var isLoading by mutableStateOf(false)
    var isConnectedToInternet by mutableStateOf(true)

    private val _upcomingEvents = MutableStateFlow<List<EventsItem?>?>(null)
    val upcomingEvents: StateFlow<List<EventsItem?>?> = _upcomingEvents

    private val _finishedEvents = MutableStateFlow<List<EventsItem?>?>(null)
    val finishedEvents: StateFlow<List<EventsItem?>?> = _finishedEvents

    fun getEvents() {
        isLoading = true
        viewModelScope.launch {
            try {
                val upcomingResponse =
                    apiService.getFilteredEvents(active = 1, limit = 5, search = "")
                val finishedResponse =
                    apiService.getFilteredEvents(active = 0, limit = 5, search = "")

                if (upcomingResponse.error == false) {
                    _upcomingEvents.value = upcomingResponse.listEvents
                }

                if (finishedResponse.error == false) {
                    _finishedEvents.value = finishedResponse.listEvents
                }
                isConnectedToInternet = true
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching events", e)
                isConnectedToInternet = false

            } finally {
                isLoading = false
            }
        }
    }
}