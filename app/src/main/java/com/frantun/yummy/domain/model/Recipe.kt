package com.frantun.yummy.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "recipes")
@Parcelize
data class Recipe(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("category")
    val category: String,
//    @SerializedName("origin")
//    val origin: Origin,
    @SerializedName("instructions")
    val instructions: String,
    @SerializedName("thumb")
    val thumb: String,
    @SerializedName("tags")
    val tags: String,
    @SerializedName("video")
    val video: String,
//    @SerializedName("ingredients")
//    val ingredients: List<Ingredient>,
) : Parcelable
