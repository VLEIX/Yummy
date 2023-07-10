package com.frantun.yummy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey
    val recipeId: String,
    val name: String,
    val category: String,
    val instructions: String,
    val thumb: String,
    val tags: String,
    val video: String,
)
