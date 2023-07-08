package com.frantun.yummy.data.repository

import com.frantun.yummy.common.Constants.ERROR_UNEXPECTED
import com.frantun.yummy.common.Resource
import com.frantun.yummy.data.datasource.RecipesLocalDataSource
import com.frantun.yummy.data.datasource.RecipesRemoteDataSource
import com.frantun.yummy.data.remote.dto.RecipesDto
import com.frantun.yummy.domain.model.RecipesResult
import com.frantun.yummy.domain.repository.RecipesRepository
import javax.inject.Inject

class RecipesRepositoryImpl @Inject constructor(
    private val recipesLocalDataSource: RecipesLocalDataSource,
    private val recipesRemoteDataSource: RecipesRemoteDataSource
) : RecipesRepository {

    override suspend fun getRecipes(): Resource<RecipesResult> {
        return try {
            when (val result = recipesRemoteDataSource.getRecipes()) {
                is Resource.Success -> {
                    getRecipesResultSuccess(result.data)?.let { recipesResult ->
                        Resource.Success(recipesResult)
                    } ?: Resource.Error(ERROR_UNEXPECTED)
                }

                is Resource.Error -> {
                    val recipesLocal = recipesLocalDataSource.getRecipes()
                    val recipesResult = RecipesResult(recipesLocal)
                    Resource.Success(recipesResult)
                }

                else -> Resource.Error(ERROR_UNEXPECTED)
            }
        } catch (exception: Exception) {
            Resource.Error(exception.message ?: exception.toString())
        }
    }

    private suspend fun getRecipesResultSuccess(recipesDto: RecipesDto?): RecipesResult? {
        return recipesDto?.let {
            recipesLocalDataSource.insertRecipes(recipesDto.recipes)
            val recipesLocal = recipesLocalDataSource.getRecipes()
            RecipesResult(recipesLocal)
        }
    }
}
