package com.frantun.yummy.presentation.ui.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frantun.yummy.common.Resource
import com.frantun.yummy.domain.model.RecipesModelUi
import com.frantun.yummy.domain.usecase.GetRecipesUseCase
import com.frantun.yummy.presentation.ui.saved.states.SavedState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SavedState>(SavedState.ShowLoading)
    val state: StateFlow<SavedState> get() = _state

    init {
        getRecipes()
    }

    private fun getRecipes() {
        val resource = getRecipesUseCase()
        resource.onEach { result ->
            when (result) {
                is Resource.Loading -> _state.value = SavedState.ShowLoading
                is Resource.Success -> recipesSuccess(result.data)
                is Resource.Error -> _state.value = SavedState.ShowError
            }
        }.launchIn(viewModelScope)
    }

    private fun recipesSuccess(recipes: RecipesModelUi?) {
        _state.value = recipes?.let {
            SavedState.RetrievedRecipes(it)
        } ?: SavedState.ShowError
    }
}
