package com.frantun.yummy.presentation.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frantun.core.common.Resource
import com.frantun.yummy.domain.model.FavoriteModelUi
import com.frantun.yummy.domain.model.RecipeModelUi
import com.frantun.yummy.domain.model.RecipesModelUi
import com.frantun.yummy.domain.usecase.DeleteFavoriteUseCase
import com.frantun.yummy.domain.usecase.GetFavoriteRecipesUseCase
import com.frantun.yummy.domain.usecase.InsertFavoriteUseCase
import com.frantun.yummy.presentation.ui.favorites.states.FavoritesState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteRecipesUseCase: GetFavoriteRecipesUseCase,
    private val insertFavoriteUseCase: InsertFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<FavoritesState>(FavoritesState.ShowLoading)
    val state: StateFlow<FavoritesState> get() = _state

    fun getFavoriteRecipes() {
        val resource = getFavoriteRecipesUseCase()
        resource.onEach { result ->
            when (result) {
                is Resource.Loading -> _state.value = FavoritesState.ShowLoading
                is Resource.Success -> recipesSuccess(result.data)
                is Resource.Error -> _state.value = FavoritesState.ShowError
            }
        }.launchIn(viewModelScope)
    }

    fun updateFavorite(recipe: RecipeModelUi) {
        recipe.favorite?.let {
            deleteFavorite(it)
        } ?: insertFavorite(recipe.recipeId)
    }

    private fun insertFavorite(favoriteId: String) {
        val insertResult = insertFavoriteUseCase(favoriteId)
        insertResult.onEach { result ->
            when (result) {
                is Resource.Success -> getFavoriteRecipes()
                else -> _state.value = FavoritesState.ShowError
            }
        }.launchIn(viewModelScope)
    }

    private fun deleteFavorite(favorite: FavoriteModelUi) {
        val deleteResult = deleteFavoriteUseCase(favorite)
        deleteResult.onEach { result ->
            when (result) {
                is Resource.Success -> getFavoriteRecipes()
                else -> _state.value = FavoritesState.ShowError
            }
        }.launchIn(viewModelScope)
    }

    private fun recipesSuccess(recipes: RecipesModelUi?) {
        recipes?.let {
            if (it.recipes.isEmpty()) {
                _state.value = FavoritesState.NoFavorites
            } else {
                _state.value = FavoritesState.RetrievedRecipes(it)
            }
        } ?: run {
            _state.value = FavoritesState.ShowError
        }
    }
}
