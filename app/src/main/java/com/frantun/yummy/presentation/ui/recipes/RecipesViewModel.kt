package com.frantun.yummy.presentation.ui.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frantun.yummy.common.Resource
import com.frantun.yummy.domain.model.FavoriteModelUi
import com.frantun.yummy.domain.model.RecipeModelUi
import com.frantun.yummy.domain.model.RecipesModelUi
import com.frantun.yummy.domain.usecase.DeleteFavoriteUseCase
import com.frantun.yummy.domain.usecase.GetLocalRecipesUseCase
import com.frantun.yummy.domain.usecase.GetRecipesUseCase
import com.frantun.yummy.domain.usecase.InsertFavoriteUseCase
import com.frantun.yummy.presentation.ui.recipes.states.RecipesState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val insertFavoriteUseCase: InsertFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
    private val getLocalRecipesUseCase: GetLocalRecipesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<RecipesState>(RecipesState.ShowLoading)
    val state: StateFlow<RecipesState> get() = _state

    init {
        getRecipes()
    }

    private fun getRecipes() {
        val resource = getRecipesUseCase()
        executeRecipesFlow(resource)
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
                is Resource.Success -> getLocalRecipes()
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    private fun deleteFavorite(favorite: FavoriteModelUi) {
        val insertResult = deleteFavoriteUseCase(favorite)
        insertResult.onEach { result ->
            when (result) {
                is Resource.Success -> getLocalRecipes()
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    fun getLocalRecipes() {
        val resource = getLocalRecipesUseCase()
        executeRecipesFlow(resource)
    }

    private fun executeRecipesFlow(flow: Flow<Resource<RecipesModelUi>>) {
        flow.onEach { result ->
            when (result) {
                is Resource.Loading -> _state.value = RecipesState.ShowLoading
                is Resource.Success -> recipesSuccess(result.data)
                is Resource.Error -> _state.value = RecipesState.ShowError
            }
        }.launchIn(viewModelScope)
    }

    private fun recipesSuccess(recipes: RecipesModelUi?) {
        _state.value = recipes?.let {
            RecipesState.RetrievedRecipes(it)
        } ?: RecipesState.ShowError
    }
}
