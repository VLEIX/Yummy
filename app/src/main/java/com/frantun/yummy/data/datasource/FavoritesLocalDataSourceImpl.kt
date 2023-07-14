package com.frantun.yummy.data.datasource

import com.frantun.yummy.data.local.dao.FavoriteDao
import com.frantun.yummy.data.local.entity.FavoriteEntity
import com.frantun.yummy.domain.model.FavoriteModelUi
import javax.inject.Inject

class FavoritesLocalDataSourceImpl @Inject constructor(
    private val favoriteDao: FavoriteDao,
) : FavoritesLocalDataSource {

    override suspend fun insertFavorite(favoriteId: String) {
        favoriteDao.insertFavorite(FavoriteEntity(favoriteId))
    }

    override suspend fun deleteFavorite(favorite: FavoriteModelUi) {
        val favoriteEntity = FavoriteEntity(favorite.favoriteId, favorite.timestamp)
        favoriteDao.deleteFavorite(favoriteEntity)
    }
}
