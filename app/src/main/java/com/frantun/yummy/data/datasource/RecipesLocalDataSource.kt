package com.frantun.yummy.data.datasource

import com.frantun.yummy.domain.model.Recipe

interface RecipesLocalDataSource {
    suspend fun insertRecipes(recipes: List<Recipe>)
    suspend fun getRecipes(): List<Recipe>
}
