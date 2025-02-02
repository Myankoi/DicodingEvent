package com.example.dicodingevent.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
class FavoriteEvent(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey(autoGenerate = false)
    var id: String = "",

    @field:ColumnInfo(name = "name")
    var name: String = "",

    @field:ColumnInfo(name = "cover")
    var mediaCover: String? = null,
)