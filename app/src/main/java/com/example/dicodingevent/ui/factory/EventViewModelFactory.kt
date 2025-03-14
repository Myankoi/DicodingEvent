package com.example.dicodingevent.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.ui.screen.finished.FinishedViewModel
import com.example.dicodingevent.ui.screen.home.HomeViewModel
import com.example.dicodingevent.ui.screen.upcoming.UpcomingViewModel

class EventViewModelFactory(
    private val repository: EventRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository) as T
            modelClass.isAssignableFrom(UpcomingViewModel::class.java) -> UpcomingViewModel(repository) as T
            modelClass.isAssignableFrom(FinishedViewModel::class.java) -> FinishedViewModel(repository) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}