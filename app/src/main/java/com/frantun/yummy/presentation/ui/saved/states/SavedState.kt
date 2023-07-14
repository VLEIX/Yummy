package com.frantun.yummy.presentation.ui.saved.states

import com.frantun.yummy.domain.model.RecipesModelUi

sealed class SavedState {
    object ShowLoading : SavedState()
    data class RetrievedRecipes(val recipes: RecipesModelUi) : SavedState()
    object ShowError : SavedState()
}
