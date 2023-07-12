package com.frantun.yummy.presentation.ui.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frantun.yummy.common.Resource
import com.frantun.yummy.domain.model.RecipesModelUi
import com.frantun.yummy.domain.usecase.GetRecipesUseCase
import com.frantun.yummy.presentation.ui.recipes.states.RecipesState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<RecipesState>(RecipesState.ShowLoading)
    val state: StateFlow<RecipesState> get() = _state

    init {
        getRecipes()
    }

    fun getRecipes() {
        val resource = getRecipesUseCase()
        resource.onEach { result ->
            when (result) {
                is Resource.Loading -> _state.value = RecipesState.ShowLoading
                is Resource.Success -> recipesSuccess(result.data)
                is Resource.Error -> Unit
            }
        }.launchIn(viewModelScope)
    }

    private fun recipesSuccess(recipes: RecipesModelUi?) {
        recipes?.let {
            _state.value = RecipesState.RetrievedRecipes(it)
        }
    }
}
