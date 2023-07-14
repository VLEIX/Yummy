package com.frantun.yummy.domain.repository

import com.frantun.yummy.common.Resource
import com.frantun.yummy.domain.model.RecipesModelUi

interface RecipesRepository {
    suspend fun getRecipes(): Resource<RecipesModelUi>
    suspend fun getRecipesByText(text: String): Resource<RecipesModelUi>
    suspend fun getFavoriteRecipes(): Resource<RecipesModelUi>
    suspend fun getLocalRecipes(): Resource<RecipesModelUi>
}
