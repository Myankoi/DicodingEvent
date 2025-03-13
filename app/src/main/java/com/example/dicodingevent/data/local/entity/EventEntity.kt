package com.example.dicodingevent.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_events")
data class FavoriteEvent(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey(autoGenerate = false)
    val id: String = "",

    @field:ColumnInfo(name = "name")
    val eventName: String = "",

    @field:ColumnInfo(name = "cover")
    val mediaCover: String? = null,
)