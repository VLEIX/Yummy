package com.frantun.yummy.data.repository

import com.frantun.yummy.common.Constants.ERROR_UNEXPECTED
import com.frantun.core.common.Resource
import com.frantun.yummy.data.datasource.RecipesLocalDataSource
import com.frantun.yummy.data.datasource.RecipesRemoteDataSource
import com.frantun.yummy.data.remote.dto.RecipesDto
import com.frantun.yummy.domain.mappers.RecipeDataMapper
import com.frantun.yummy.domain.model.RecipesModelUi
import com.frantun.yummy.domain.repository.RecipesRepository
import javax.inject.Inject

class RecipesRepositoryImpl @Inject constructor(
    private val recipesRemoteDataSource: RecipesRemoteDataSource,
    private val recipesLocalDataSource: RecipesLocalDataSource,
    private val recipeDataMapper: RecipeDataMapper,
) : RecipesRepository {

    override suspend fun getRecipes(): Resource<RecipesModelUi> {
        return try {
            when (val result = recipesRemoteDataSource.getRecipes()) {
                is Resource.Success -> {
                    getRecipesResultSuccess(result.data)?.let { recipesDto ->
                        Resource.Success(recipesDto)
                    } ?: Resource.Error(ERROR_UNEXPECTED)
                }

                is Resource.Error -> {
                    val recipesLocal = recipesLocalDataSource.getRecipes()
                    val recipesModelUi = recipeDataMapper.map(recipesLocal)
                    Resource.Success(recipesModelUi)
                }

                else -> Resource.Error(ERROR_UNEXPECTED)
            }
        } catch (exception: Exception) {
            Resource.Error(exception.message ?: exception.toString())
        }
    }

    override suspend fun getRecipesByText(text: String): Resource<RecipesModelUi> {
        return try {
            val recipesLocal = recipesLocalDataSource.getRecipesByText(text)
            val recipesModelUi = recipeDataMapper.map(recipesLocal)
            Resource.Success(recipesModelUi)
        } catch (exception: Exception) {
            Resource.Error(exception.message ?: exception.toString())
        }
    }

    override suspend fun getFavoriteRecipes(): Resource<RecipesModelUi> {
        return try {
            val recipesLocal = recipesLocalDataSource.getFavoritesRecipes()
            val recipesModelUi = recipeDataMapper.map(recipesLocal)
            Resource.Success(recipesModelUi)
        } catch (exception: Exception) {
            Resource.Error(exception.message ?: exception.toString())
        }
    }

    override suspend fun getLocalRecipes(): Resource<RecipesModelUi> {
        return try {
            val recipesLocal = recipesLocalDataSource.getRecipes()
            val recipesModelUi = recipeDataMapper.map(recipesLocal)
            Resource.Success(recipesModelUi)
        } catch (exception: Exception) {
            Resource.Error(exception.message ?: exception.toString())
        }
    }

    private suspend fun getRecipesResultSuccess(recipesDto: RecipesDto?): RecipesModelUi? {
        return recipesDto?.let {
            recipesLocalDataSource.insertRecipes(recipesDto.recipes)
            val recipesLocal = recipesLocalDataSource.getRecipes()
            recipeDataMapper.map(recipesLocal)
        }
    }
}
