package com.frantun.yummy.data.remote.dto

import com.google.gson.annotations.SerializedName

data class IngredientDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("measure")
    val measure: String
)
