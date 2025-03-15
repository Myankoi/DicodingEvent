package com.example.dicodingevent.ui.screen.upcoming

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
                upcomingResponse?.let {
                    _upcomingEvents.value = Result.Success(it)
                } ?: throw Exception("No Internet Connection.")
            } catch (e: Exception) {
                _upcomingEvents.value = Result.Error(e.message ?: "")
            }
        }
    }
}