package com.frantun.yummy.presentation.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frantun.yummy.common.Resource
import com.frantun.yummy.domain.model.RecipesModelUi
import com.frantun.yummy.domain.usecase.GetFavoriteRecipesUseCase
import com.frantun.yummy.presentation.ui.favorites.states.FavoritesState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteRecipesUseCase: GetFavoriteRecipesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<FavoritesState>(FavoritesState.ShowLoading)
    val state: StateFlow<FavoritesState> get() = _state

    init {
        getFavoriteRecipes()
    }

    private fun getFavoriteRecipes() {
        val resource = getFavoriteRecipesUseCase()
        resource.onEach { result ->
            when (result) {
                is Resource.Loading -> _state.value = FavoritesState.ShowLoading
                is Resource.Success -> recipesSuccess(result.data)
                is Resource.Error -> _state.value = FavoritesState.ShowError
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
