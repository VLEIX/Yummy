package com.frantun.yummy.presentation.adapters

import com.frantun.yummy.domain.model.RecipeModelUi

class FavoriteAdapterListener(val clickListener: (recipe: RecipeModelUi) -> Unit) {
    fun onClick(recipe: RecipeModelUi) = clickListener(recipe)
}
