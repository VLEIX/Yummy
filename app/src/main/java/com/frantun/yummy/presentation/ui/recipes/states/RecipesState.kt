package com.frantun.yummy.presentation.ui.recipes.states

import com.frantun.yummy.domain.model.Recipe

sealed class RecipesState {
    object ShowLoading : RecipesState()
    data class RetrievedRecipes(val recipes: List<Recipe>) : RecipesState()
}
