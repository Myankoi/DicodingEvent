package com.example.dicodingevent.ui.screen.upcoming

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.remote.response.EventsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UpcomingViewModel(
    private val repository: EventRepository
) : ViewModel() {
    private val _upcomingEvents = MutableStateFlow<Result<List<EventsItem?>>>(Result.Loading)
    val upcomingEvents: StateFlow<Result<List<EventsItem?>>> = _upcomingEvents

    fun getEvents(search: String = "") {
        _upcomingEvents.value = Result.Loading
        viewModelScope.launch {
            try {
                val upcomingResponse =
                    repository.getAllEvents(active = 1, limit = 5, search = search)

                if (upcomingResponse != null) {
                    _upcomingEvents.value = Result.Success(upcomingResponse)
                } else {
                    _upcomingEvents.value = Result.Error("Event not found")
                }
            } catch (e: Exception) {
                Log.e("UpcomingViewModel", "Error fetching events", e)
                _upcomingEvents.value = Result.Error("No Internet")
            }
        }
    }
}