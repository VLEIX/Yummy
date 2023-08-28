package com.frantun.yummy.data.datasource

import com.frantun.core.common.Resource
import com.frantun.yummy.data.remote.dto.RecipesDto

interface RecipesRemoteDataSource {
    suspend fun getRecipes(): Resource<RecipesDto>
}
