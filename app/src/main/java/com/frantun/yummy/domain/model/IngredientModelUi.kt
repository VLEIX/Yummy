package com.frantun.yummy.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IngredientModelUi(
    val name: String,
    val measure: String
) : Parcelable
