package com.frantun.yummy.data.datasource

import com.frantun.yummy.data.local.entity.RecipeAndRelations
import com.frantun.yummy.data.remote.dto.RecipeDto

interface RecipesLocalDataSource {
    suspend fun insertRecipes(recipes: List<RecipeDto>)
    suspend fun getRecipes(): List<RecipeAndRelations>
}
