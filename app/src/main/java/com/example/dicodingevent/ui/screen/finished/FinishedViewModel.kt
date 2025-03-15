package com.example.dicodingevent.ui.screen.finished

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.remote.response.EventsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FinishedViewModel(
    private val repository: EventRepository
) : ViewModel() {
    private val _finishedEvents = MutableStateFlow<Result<List<EventsItem?>>>(Result.Loading)
    val finishedEvents: StateFlow<Result<List<EventsItem?>>> = _finishedEvents

    fun getEvents(search: String = "") {
        _finishedEvents.value = Result.Loading
        viewModelScope.launch {
            try {
                val finishedResponse =
                    repository.getAllEvents(active = 0, limit = 40, search = search)
                finishedResponse?.let {
                    _finishedEvents.value = Result.Success(it)
                } ?: throw Exception("No Internet Connection.")
            } catch (e: Exception) {
                _finishedEvents.value = Result.Error(e.message ?: "")
            }
        }
    }
}