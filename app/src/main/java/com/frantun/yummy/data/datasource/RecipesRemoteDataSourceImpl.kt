package com.frantun.yummy.data.datasource

import com.frantun.yummy.common.BaseRemoteDataSource
import com.frantun.yummy.common.Resource
import com.frantun.yummy.data.remote.RecipesApi
import com.frantun.yummy.data.remote.dto.RecipesDto
import javax.inject.Inject

class RecipesRemoteDataSourceImpl @Inject constructor(
    private val recipesApi: RecipesApi
) : BaseRemoteDataSource(), RecipesRemoteDataSource {

    override suspend fun getRecipes(): Resource<RecipesDto> = getResult {
        recipesApi.getRecipes()
    }
}
