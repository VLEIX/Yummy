package com.frantun.yummy.data.repository

import com.frantun.yummy.data.datasource.FavoritesLocalDataSource
import com.frantun.yummy.domain.model.FavoriteModelUi
import com.frantun.yummy.domain.repository.FavoritesRepository
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val favoritesLocalDataSource: FavoritesLocalDataSource,
) : FavoritesRepository {

    override suspend fun insertFavorite(favoriteId: String) {
        favoritesLocalDataSource.insertFavorite(favoriteId)
    }

    override suspend fun deleteFavorite(favorite: FavoriteModelUi) {
        favoritesLocalDataSource.deleteFavorite(favorite)
    }
}
