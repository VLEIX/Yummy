package com.frantun.yummy.data.datasource

import com.frantun.yummy.data.local.dao.IngredientDao
import com.frantun.yummy.data.local.dao.OriginDao
import com.frantun.yummy.data.local.dao.RecipeDao
import com.frantun.yummy.data.local.entity.IngredientEntity
import com.frantun.yummy.data.local.entity.OriginEntity
import com.frantun.yummy.data.local.entity.RecipeEntity
import com.frantun.yummy.data.local.entity.RecipeAndRelations
import com.frantun.yummy.data.remote.dto.RecipeDto
import javax.inject.Inject

class RecipesLocalDataSourceImpl @Inject constructor(
    private val recipeDao: RecipeDao,
    private val originDao: OriginDao,
    private val ingredientDao: IngredientDao
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
                    description = it.origin.description,
                    latitude = it.origin.latitude,
                    longitude = it.origin.longitude,
                )
            )
            it.ingredients.forEach { ingredient ->
                ingredientDao.insertIngredient(
                    IngredientEntity(
                        name = ingredient.name,
                        ingredientRecipeId = it.id,
                        measure = ingredient.measure
                    )
                )
            }
        }
    }

    override suspend fun getRecipes(): List<RecipeAndRelations> {
        return recipeDao.getRecipes()
    }
}
