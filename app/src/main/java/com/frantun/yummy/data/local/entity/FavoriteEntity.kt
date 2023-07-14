package com.frantun.yummy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    val favoriteId: String,
    val timestamp: Date = Date(),
)
