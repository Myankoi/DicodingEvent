package com.example.dicodingevent.ui.screen.upcoming

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

class UpcomingViewModel : ViewModel() {
    private val apiService = ApiConfig.getApiService()

    var isLoading by mutableStateOf(false)
    var isConnectedToInternet by mutableStateOf(true)

    private val _upcomingEvents = MutableStateFlow<List<EventsItem?>?>(null)
    val upcomingEvents: StateFlow<List<EventsItem?>?> = _upcomingEvents

    fun getEvents(search: String = "") {
        isLoading = true
        viewModelScope.launch {
            try {
                val finishedResponse =
                    apiService.getFilteredEvents(active = 1, limit = 200, search = search)

                if (finishedResponse.error == false) {
                    _upcomingEvents.value = finishedResponse.listEvents
                }
                isConnectedToInternet = true
            } catch (e: Exception) {
                Log.e("UpcomingViewModel", "Error fetching events", e)
                isConnectedToInternet = false

            } finally {
                isLoading = false
            }
        }
    }
}