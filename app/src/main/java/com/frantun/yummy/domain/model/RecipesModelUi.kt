package com.frantun.yummy.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipesModelUi(
    val recipes: List<RecipeModelUi>
) : Parcelable
