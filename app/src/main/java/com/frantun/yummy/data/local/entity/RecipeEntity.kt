package com.frantun.yummy.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "recipes")
@Parcelize
data class RecipeEntity(
    @PrimaryKey
    val recipeId: String,
    val name: String,
    val category: String,
    val instructions: String,
    val thumb: String,
    val tags: String,
    val video: String,
//    @SerializedName("ingredients")
//    val ingredients: List<Ingredient>,
) : Parcelable
