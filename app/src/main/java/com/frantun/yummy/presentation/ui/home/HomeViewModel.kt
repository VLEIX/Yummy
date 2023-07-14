package com.frantun.yummy.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor() : ViewModel() {

    private val _isUpdatedFavorite = MutableLiveData<Boolean>()
    val isUpdatedFavorite: LiveData<Boolean>
        get() = _isUpdatedFavorite

    fun updateFavorite(isUpdated: Boolean) {
        _isUpdatedFavorite.value = isUpdated
    }
}
