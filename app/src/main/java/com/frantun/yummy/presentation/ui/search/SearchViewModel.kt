package com.frantun.yummy.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frantun.yummy.common.Resource
import com.frantun.yummy.domain.model.FavoriteModelUi
import com.frantun.yummy.domain.model.RecipeModelUi
import com.frantun.yummy.domain.model.RecipesModelUi
import com.frantun.yummy.domain.usecase.DeleteFavoriteUseCase
import com.frantun.yummy.domain.usecase.GetRecipesByTextUseCase
import com.frantun.yummy.domain.usecase.InsertFavoriteUseCase
import com.frantun.yummy.presentation.ui.search.states.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getRecipesByTextUseCase: GetRecipesByTextUseCase,
    private val insertFavoriteUseCase: InsertFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Initialized)
    val state: StateFlow<SearchState> get() = _state

    private var textToSearch = ""

    fun getRecipes(text: String) {
        textToSearch = text
        if (textToSearch.isNotEmpty()) {
            searchRecipesByText(textToSearch)
        } else {
            _state.value = SearchState.Initialized
        }
    }

    private fun searchRecipesByText(text: String) {
        viewModelScope.launch {
            _state.value = SearchState.ShowLoading

            val resource = getRecipesByTextUseCase(text)
            resource.onEach { result ->
                when (result) {
                    is Resource.Loading -> _state.value = SearchState.ShowLoading
                    is Resource.Success -> recipesSuccess(result.data)
                    is Resource.Error -> _state.value = SearchState.ShowError
                }
            }.launchIn(this)
        }
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
                is Resource.Success -> getRecipes(textToSearch)
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    private fun deleteFavorite(favorite: FavoriteModelUi) {
        val insertResult = deleteFavoriteUseCase(favorite)
        insertResult.onEach { result ->
            when (result) {
                is Resource.Success -> getRecipes(textToSearch)
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    private fun recipesSuccess(recipes: RecipesModelUi?) {
        recipes?.let {
            if (it.recipes.isEmpty()) {
                _state.value = SearchState.NoRecipes
            } else {
                _state.value = SearchState.RetrievedRecipes(it)
            }
        } ?: run {
            _state.value = SearchState.ShowError
        }
    }
}
