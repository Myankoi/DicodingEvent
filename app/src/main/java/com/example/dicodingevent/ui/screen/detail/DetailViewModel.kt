package com.example.dicodingevent.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.FavoriteEventRepository
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.remote.response.EventsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val eventRepository: EventRepository,
    private val favRepository: FavoriteEventRepository
) : ViewModel() {
    private val _event = MutableStateFlow<Result<EventsItem>>(Result.Loading)
    val event: StateFlow<Result<EventsItem>> = _event

    private val _isExist = MutableStateFlow(false)
    val isExist: StateFlow<Boolean> = _isExist

    fun getEventById(eventId: String) {
        _event.value = Result.Loading
        viewModelScope.launch {
            try {
                val response = eventRepository.getEventById(id = eventId)
                response?.let {
                    _event.value = Result.Success(it)
                } ?: throw Exception("No Internet Connection.")
            } catch (e: Exception) {
                _event.value = Result.Error(e.message ?: "")
            }
        }
    }

    suspend fun checkIfExist(eventId: String) {
        _isExist.value = favRepository.isEventFavorite(eventId) == true
    }

    fun addToFavRepository(eventId: String, eventName: String, mediaCover: String) {
        viewModelScope.launch {
            if (_isExist.value) {
                favRepository.removeFromFavorites(eventId)
                _isExist.value = false
            } else {
                favRepository.addToFavorites(eventId, eventName, mediaCover)
                _isExist.value = true
            }
        }
    }
}