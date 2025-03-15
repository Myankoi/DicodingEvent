package com.example.dicodingevent.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.remote.response.EventsItem
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

                upcomingResponse?.let {
                    _upcomingEvents.value = Result.Success(it)
                } ?: throw Exception("No Internet Connection.")

                finishedResponse?.let {
                    _finishedEvents.value = Result.Success(it)
                } ?: throw Exception("No Internet Connection.")
            } catch (e: Exception) {
                _upcomingEvents.value = Result.Error(e.message ?: "")
                _finishedEvents.value = Result.Error(e.message ?: "")
            }
        }
    }
}