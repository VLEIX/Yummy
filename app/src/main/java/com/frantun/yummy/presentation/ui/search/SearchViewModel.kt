package com.frantun.yummy.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frantun.yummy.common.Resource
import com.frantun.yummy.domain.model.RecipesModelUi
import com.frantun.yummy.domain.usecase.GetRecipesByTextUseCase
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
    private val getRecipesByTextUseCase: GetRecipesByTextUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Initialized)
    val state: StateFlow<SearchState> get() = _state

    fun getRecipes(text: String) {
        if (text.isNotEmpty()) {
            searchRecipesByText(text)
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

    private fun recipesSuccess(recipes: RecipesModelUi?) {
        recipes?.let {
            if (it.recipes.isEmpty()) {
                _state.value = SearchState.NoRecipes
            } else {
                _state.value = SearchState.RetrievedRecipes(it)
            }
        }
    }
}
