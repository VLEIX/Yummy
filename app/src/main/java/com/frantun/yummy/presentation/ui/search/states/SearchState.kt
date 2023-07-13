package com.frantun.yummy.presentation.ui.search.states

import com.frantun.yummy.domain.model.RecipesModelUi

sealed class SearchState {
    object Initialized : SearchState()
    object ShowLoading : SearchState()
    object NoRecipes : SearchState()
    data class RetrievedRecipes(val recipes: RecipesModelUi) : SearchState()
    object ShowError : SearchState()
}
