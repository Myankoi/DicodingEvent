package com.example.dicodingevent.ui.screen.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.remote.response.EventsItem
import com.example.dicodingevent.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: EventRepository) : ViewModel() {
    private val _upcomingEvents = MutableStateFlow<Result<List<EventsItem?>>>(Result.Loading)
    val upcomingEvents: StateFlow<Result<List<EventsItem?>>> = _upcomingEvents

    private val _finishedEvents = MutableStateFlow<Result<List<EventsItem?>>>(Result.Loading)
    val finishedEvents: StateFlow<Result<List<EventsItem?>>> = _finishedEvents

    fun getEvents() {
        _upcomingEvents.value = Result.Loading
        _finishedEvents.value = Result.Loading
        viewModelScope.launch {
            try {
                val upcomingResponse = repository.getAllEvents(active = 1, limit = 5, search = "")
                val finishedResponse = repository.getAllEvents(active = 0, limit = 5, search = "")

                if (upcomingResponse.isNullOrEmpty()) {
                    _upcomingEvents.value = Result.Error("Event not found")
                } else {
                    _upcomingEvents.value = Result.Success(upcomingResponse)
                }

                if (finishedResponse.isNullOrEmpty()) {
                    _finishedEvents.value = Result.Error("Event not found")
                } else {
                    _finishedEvents.value = Result.Success(finishedResponse)
                }

            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching events", e)
                _upcomingEvents.value = Result.Error("No Internet")
                _finishedEvents.value = Result.Error("No Internet")
            }
        }
    }
}