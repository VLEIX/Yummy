package com.frantun.yummy.data.repository

import com.frantun.yummy.common.Resource
import com.frantun.yummy.domain.model.Recipe
import com.frantun.yummy.domain.model.RecipesResult
import com.frantun.yummy.domain.repository.RecipesRepository

class FakeRecipesRepositoryImpl(private val recipes: List<Recipe>) : RecipesRepository {
    override suspend fun getRecipes(): Resource<RecipesResult> {
        return Resource.Success(RecipesResult(recipes))
    }
}
