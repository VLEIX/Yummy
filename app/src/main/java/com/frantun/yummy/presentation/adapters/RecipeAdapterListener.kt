package com.frantun.yummy.presentation.adapters

import com.frantun.yummy.data.local.entity.RecipeEntity

class RecipeAdapterListener(val clickListener: (recipe: RecipeEntity) -> Unit) {
    fun onClick(recipe: RecipeEntity) = clickListener(recipe)
}
