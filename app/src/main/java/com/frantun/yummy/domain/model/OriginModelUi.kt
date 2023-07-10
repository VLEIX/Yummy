package com.frantun.yummy.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OriginModelUi(
    val name: String,
    val latitude: Double,
    val longitude: Double,
) : Parcelable
