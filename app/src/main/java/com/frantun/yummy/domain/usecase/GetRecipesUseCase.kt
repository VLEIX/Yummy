package com.frantun.yummy.domain.usecase

import com.frantun.yummy.common.Constants.ERROR_UNEXPECTED
import com.frantun.yummy.common.Resource
import com.frantun.yummy.domain.model.RecipesResult
import com.frantun.yummy.domain.repository.RecipesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetRecipesUseCase @Inject constructor(
    private val recipesRepository: RecipesRepository
) {
    operator fun invoke(): Flow<Resource<RecipesResult>> = flow {
        try {
            emit(Resource.Loading())
            val recipesResult = recipesRepository.getRecipes()
            recipesResult.data?.let {
                emit(Resource.Success(it))
            } ?: emit(Resource.Error(ERROR_UNEXPECTED))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: ERROR_UNEXPECTED))
        }
    }
}
