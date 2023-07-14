package com.frantun.yummy.domain.repository

import com.frantun.yummy.domain.model.FavoriteModelUi

interface FavoritesRepository {
    suspend fun insertFavorite(favoriteId: String)
    suspend fun deleteFavorite(favorite: FavoriteModelUi)
}
