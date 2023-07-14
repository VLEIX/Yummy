package com.frantun.yummy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.frantun.yummy.data.local.entity.RecipeAndRelations
import com.frantun.yummy.data.local.entity.RecipeEntity

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Transaction
    @Query("SELECT * FROM recipes")
    suspend fun getRecipes(): List<RecipeAndRelations>

    @Transaction
    @Query(
        "SELECT recipes.* FROM recipes JOIN ingredients ON recipes.recipeId == ingredients.ingredientRecipeId " +
                "WHERE recipes.name LIKE '%' || :text || '%' OR ingredients.name LIKE '%' || :text || '%' " +
                "GROUP BY recipes.recipeId"
    )
    suspend fun getRecipesByText(text: String): List<RecipeAndRelations>

    @Transaction
    @Query("SELECT recipes.* FROM recipes, favorites WHERE recipes.recipeId == favorites.favoriteId")
    suspend fun getFavoritesRecipes(): List<RecipeAndRelations>
}
