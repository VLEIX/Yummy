package com.frantun.yummy.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity(tableName = "origins", primaryKeys = ["name", "originRecipeId"])
@Parcelize
data class OriginEntity(
    val name: String,
    val originRecipeId: String,
    val latitude: Double,
    val longitude: Double,
) : Parcelable
