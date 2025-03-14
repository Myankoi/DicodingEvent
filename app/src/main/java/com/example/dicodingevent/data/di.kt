package com.example.dicodingevent.data

import android.content.Context
import com.example.dicodingevent.data.local.room.FavoriteDatabase
import com.example.dicodingevent.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(): EventRepository {
        val apiService = ApiConfig.getApiService()
        return EventRepository(apiService)
    }

    fun provideFavoriteRepository(context: Context): FavoriteEventRepository {
        val database = FavoriteDatabase.getDatabase(context)
        val dao = database.favoriteEventDao()
        return FavoriteEventRepository(dao)
    }
}