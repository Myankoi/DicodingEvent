package com.example.dicodingevent.data

import com.example.dicodingevent.data.local.entity.FavoriteEvent
import com.example.dicodingevent.data.local.room.FavoriteEventDao
import com.example.dicodingevent.data.remote.response.EventsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class FavoriteEventRepository(
    private val eventDao: FavoriteEventDao
) {
    suspend fun addToFavorites(eventId: String, eventName: String, mediaCover: String) {
        eventDao.insertFavorite(
            FavoriteEvent(
                id = eventId,
                eventName = eventName,
                mediaCover = mediaCover
            )
        )
    }

    suspend fun removeFromFavorites(eventId: String) {
        eventDao.deleteFavorite(FavoriteEvent(id = eventId))
    }

    suspend fun isEventFavorite(eventId: String): Boolean {
        return eventDao.getFavoriteEventById(eventId).firstOrNull() != null
    }

    fun getFavorites(): Flow<List<FavoriteEvent>> {
        return eventDao.getAllFavorites()
    }
}