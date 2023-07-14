package com.frantun.yummy.data.datasource

import com.frantun.yummy.domain.model.FavoriteModelUi

interface FavoritesLocalDataSource {
    suspend fun insertFavorite(favoriteId: String)
    suspend fun deleteFavorite(favorite: FavoriteModelUi)
}
