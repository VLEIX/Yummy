package com.frantun.yummy.data.datasource

import com.frantun.yummy.data.local.dao.OriginDao
import com.frantun.yummy.data.local.dao.RecipeDao
import com.frantun.yummy.data.local.entity.OriginEntity
import com.frantun.yummy.data.local.entity.RecipeEntity
import com.frantun.yummy.data.local.entity.RecipeFull
import com.frantun.yummy.data.remote.dto.RecipeDto
import com.frantun.yummy.data.remote.dto.RecipesDto
import javax.inject.Inject

class RecipesLocalDataSourceImpl @Inject constructor(
    private val recipeDao: RecipeDao,
    private val originDao: OriginDao
) : RecipesLocalDataSource {

    override suspend fun insertRecipes(recipes: List<RecipeDto>) {
        recipes.forEach {
            recipeDao.insertRecipe(
                RecipeEntity(
                    recipeId = it.id,
                    it.name,
                    it.category,
                    it.instructions,
                    it.thumb,
                    it.tags,
                    it.video
                )
            )
            originDao.insertOrigin(
                OriginEntity(
                    name = it.origin.name,
                    originRecipeId = it.id,
                    latitude = it.origin.latitude,
                    longitude = it.origin.longitude,
                )
            )
        }
    }

    override suspend fun getRecipes(): List<RecipeFull> {
        return recipeDao.getRecipes()
    }
}
