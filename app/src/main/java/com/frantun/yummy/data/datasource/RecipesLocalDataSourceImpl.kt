package com.frantun.yummy.data.datasource

import com.frantun.yummy.data.local.dao.RecipeDao
import com.frantun.yummy.domain.model.Recipe
import javax.inject.Inject

class RecipesLocalDataSourceImpl @Inject constructor(
    private val recipeDao: RecipeDao
) : RecipesLocalDataSource {

    override suspend fun insertRecipes(recipes: List<Recipe>) {
        recipeDao.insertRecipes(recipes)
    }

    override suspend fun getRecipes(): List<Recipe> {
        return recipeDao.getRecipes()
    }
}
