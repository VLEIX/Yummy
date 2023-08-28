package com.frantun.yummy.domain.usecase

import com.frantun.yummy.common.Constants.ERROR_UNEXPECTED
import com.frantun.core.common.Resource
import com.frantun.yummy.domain.model.FavoriteModelUi
import com.frantun.yummy.domain.repository.FavoritesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteFavoriteUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) {
    operator fun invoke(favorite: FavoriteModelUi): Flow<Resource<Unit>> = flow {
        try {
            val favoriteResult = favoritesRepository.deleteFavorite(favorite)
            emit(Resource.Success(favoriteResult))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: ERROR_UNEXPECTED))
        }
    }
}
