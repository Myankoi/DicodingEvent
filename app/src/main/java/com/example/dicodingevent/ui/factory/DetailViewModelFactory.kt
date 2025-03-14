package com.example.dicodingevent.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.FavoriteEventRepository
import com.example.dicodingevent.ui.screen.detail.DetailViewModel

class DetailViewModelFactory(
    private val repository: EventRepository,
    private val favRepository: FavoriteEventRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository, favRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}