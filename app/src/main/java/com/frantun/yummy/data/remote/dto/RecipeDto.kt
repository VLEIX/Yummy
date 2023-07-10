package com.frantun.yummy.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RecipeDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("origin")
    val origin: OriginDto,
    @SerializedName("instructions")
    val instructions: String,
    @SerializedName("thumb")
    val thumb: String,
    @SerializedName("tags")
    val tags: String,
    @SerializedName("video")
    val video: String,
    @SerializedName("ingredients")
    val ingredients: List<IngredientDto>,
)
