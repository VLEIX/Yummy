package com.frantun.yummy.domain.usecase

import com.frantun.yummy.common.Constants.ERROR_UNEXPECTED
import com.frantun.core.common.Resource
import com.frantun.yummy.domain.model.RecipesModelUi
import com.frantun.yummy.domain.repository.RecipesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetFavoriteRecipesUseCase @Inject constructor(
    private val recipesRepository: RecipesRepository
) {
    operator fun invoke(): Flow<Resource<RecipesModelUi>> = flow {
        try {
            val recipesResult = recipesRepository.getFavoriteRecipes()
            recipesResult.data?.let {
                emit(Resource.Success(it))
            } ?: emit(Resource.Error(ERROR_UNEXPECTED))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: ERROR_UNEXPECTED))
        }
    }
}
