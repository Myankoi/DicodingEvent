package com.example.dicodingevent.data.remote.retrofit

import com.example.dicodingevent.data.remote.response.Response
import com.example.dicodingevent.data.remote.response.ResponseDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events/{id}")
    fun getEventById(
        @Path("id") id: String
    ): Call<ResponseDetail>

    @GET("events")
    fun getFilteredEvents(
        @Query("active") active: Int,
        @Query("limit") limit: Int,
        @Query("q") search: String
    ): Call<Response>
}