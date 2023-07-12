package com.frantun.yummy.presentation.adapters

import com.frantun.yummy.domain.model.RecipeModelUi
import com.google.android.material.imageview.ShapeableImageView

class RecipeAdapterListener(val clickListener: (recipe: RecipeModelUi, thumbImageView: ShapeableImageView) -> Unit) {
    fun onClick(recipe: RecipeModelUi, thumbImageView: ShapeableImageView) =
        clickListener(recipe, thumbImageView)
}
