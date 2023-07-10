package com.frantun.yummy.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity(tableName = "ingredients")
@Parcelize
data class IngredientEntity(
    val name: String,
    val measure: String
) : Parcelable
