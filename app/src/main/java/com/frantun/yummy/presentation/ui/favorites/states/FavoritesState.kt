package com.frantun.yummy.presentation.ui.favorites.states

import com.frantun.yummy.domain.model.RecipesModelUi

sealed class FavoritesState {
    object ShowLoading : FavoritesState()
    object NoFavorites : FavoritesState()
    data class RetrievedRecipes(val recipes: RecipesModelUi) : FavoritesState()
    object ShowError : FavoritesState()
}
