package com.frantun.yummy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.frantun.yummy.data.local.entity.RecipeFull
import com.frantun.yummy.data.local.entity.RecipeEntity

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Transaction
    @Query("SELECT * FROM recipes")
    suspend fun getRecipes(): List<RecipeFull>
}
