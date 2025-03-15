package com.example.dicodingevent.data.remote.retrofit

import com.example.dicodingevent.data.remote.response.Response
import com.example.dicodingevent.data.remote.response.ResponseDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events/{id}")
    suspend fun getEventById(
        @Path("id") id: String
    ): ResponseDetail

    @GET("events")
    suspend fun getReminderEvent(
        @Query("active") active: Int = -1,
        @Query("limit") limit: Int = 1
    ) : Response

    @GET("events")
    suspend fun getFilteredEvents(
        @Query("active") active: Int,
        @Query("limit") limit: Int,
        @Query("q") search: String
    ): Response
}