package com.frantun.yummy.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey
    val recipeId: String,
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val name: String,
    val category: String,
    val instructions: String,
    val thumb: String,
    val video: String,
)
