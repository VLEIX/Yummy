package com.frantun.yummy.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OriginDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
)
