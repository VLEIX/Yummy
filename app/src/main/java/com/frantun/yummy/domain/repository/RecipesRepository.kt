package com.frantun.yummy.domain.repository

import com.frantun.yummy.common.Resource
import com.frantun.yummy.domain.model.RecipesModelUi

interface RecipesRepository {
    suspend fun getRecipes(): Resource<RecipesModelUi>
}
