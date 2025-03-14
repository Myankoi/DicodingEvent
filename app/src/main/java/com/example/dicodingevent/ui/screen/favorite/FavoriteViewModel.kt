package com.example.dicodingevent.ui.screen.favorite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.FavoriteEventRepository
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.local.entity.FavoriteEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favEventRepository: FavoriteEventRepository) : ViewModel() {
    private val _favEvents = MutableStateFlow<Result<List<FavoriteEvent>>>(Result.Loading)
    val favEvents: StateFlow<Result<List<FavoriteEvent>>> = _favEvents

    fun getEvents() {
        _favEvents.value = Result.Loading
        viewModelScope.launch {
            try {
                val favResponse = favEventRepository.getFavorites().first()
                _favEvents.value = Result.Success(favResponse)
            } catch (e: Exception) {
                Log.e("FavoriteViewModel", "Error fetching events", e)
                _favEvents.value = Result.Error("No Internet")
            }
        }
    }
}