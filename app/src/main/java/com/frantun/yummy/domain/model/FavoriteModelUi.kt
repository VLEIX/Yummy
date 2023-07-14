package com.frantun.yummy.domain.model

import android.os.Parcelable
import java.util.Date
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteModelUi(
    val favoriteId: String,
    val timestamp: Date,
) : Parcelable
