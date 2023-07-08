package com.frantun.yummy.domain.repository

import com.frantun.yummy.common.Resource
import com.frantun.yummy.domain.model.RecipesResult

interface RecipesRepository {
    suspend fun getRecipes(): Resource<RecipesResult>
}
