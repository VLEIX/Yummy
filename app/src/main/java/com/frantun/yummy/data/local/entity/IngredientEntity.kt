package com.frantun.yummy.data.local.entity

import androidx.room.Entity

@Entity(tableName = "ingredients", primaryKeys = ["name", "ingredientRecipeId"])
data class IngredientEntity(
    val name: String,
    val ingredientRecipeId: String,
    val measure: String
)
