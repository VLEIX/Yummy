package com.frantun.yummy.data.remote.dto

import com.frantun.yummy.domain.model.Recipe
import com.google.gson.annotations.SerializedName

data class RecipesDto(
    @SerializedName("recipes")
    val recipes: List<Recipe>
)
