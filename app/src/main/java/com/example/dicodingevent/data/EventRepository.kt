package com.example.dicodingevent.data

import com.example.dicodingevent.data.remote.response.EventsItem
import com.example.dicodingevent.data.remote.retrofit.ApiService

class EventRepository(private val apiService: ApiService) {
    suspend fun getAllEvents(active: Int, limit: Int, search: String): List<EventsItem?>? {
        return try {
            val response = apiService.getFilteredEvents(active, limit, search)
            response.listEvents ?: emptyList()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getEventById(id: String): EventsItem? {
        return try {
            apiService.getEventById(id).event
        } catch (e: Exception) {
            null
        }
    }
}