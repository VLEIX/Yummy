package com.frantun.yummy.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class RecipeModelUi(
    val recipeId: String,
    val name: String,
    val category: String,
    val instructions: String,
    val thumb: String,
    val tags: String,
    val video: String,
    val origin: OriginModelUi,
    val ingredients: @RawValue List<IngredientModelUi>,
) : Parcelable
