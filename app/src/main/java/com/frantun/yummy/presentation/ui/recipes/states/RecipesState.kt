package com.frantun.yummy.presentation.ui.recipes.states

import com.frantun.yummy.domain.model.RecipesModelUi

sealed class RecipesState {
    object ShowLoading : RecipesState()
    data class RetrievedRecipes(val recipes: RecipesModelUi) : RecipesState()
    object ShowError : RecipesState()
}
