package com.frantun.yummy.domain.model

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "ingredients")
@Parcelize
data class Ingredient(
    @SerializedName("name")
    val name: String,
    @SerializedName("measure")
    val measure: String
) : Parcelable
