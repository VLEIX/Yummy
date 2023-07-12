package com.frantun.yummy.data.local.entity

import androidx.room.Entity

@Entity(tableName = "origins", primaryKeys = ["name", "originRecipeId"])
data class OriginEntity(
    val name: String,
    val originRecipeId: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
)
