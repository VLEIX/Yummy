package com.frantun.yummy.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RecipesDto(
    @SerializedName("recipes")
    val recipes: List<RecipeDto>
)
