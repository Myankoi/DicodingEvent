package com.example.dicodingevent.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dicodingevent.data.local.entity.FavoriteEvent
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteEventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(event: FavoriteEvent)

    @Delete
    suspend fun deleteFavorite(event: FavoriteEvent)

    @Query("SELECT * FROM favorite_events")
    fun getAllFavorites(): Flow<List<FavoriteEvent>>

    @Query("SELECT * FROM favorite_events WHERE id = :id")
    fun getFavoriteEventById(id: String): Flow<FavoriteEvent>
}