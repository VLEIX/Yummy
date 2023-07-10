package com.frantun.yummy.presentation.ui.recipes.states

import com.frantun.yummy.data.local.entity.RecipeFull

sealed class RecipesState {
    object ShowLoading : RecipesState()
    data class RetrievedRecipes(val recipes: List<RecipeFull>) : RecipesState()
}
