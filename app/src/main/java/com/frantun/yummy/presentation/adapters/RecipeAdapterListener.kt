package com.frantun.yummy.presentation.adapters

import com.frantun.yummy.domain.model.Recipe

class RecipeAdapterListener(val clickListener: (recipe: Recipe) -> Unit) {
    fun onClick(recipe: Recipe) = clickListener(recipe)
}
