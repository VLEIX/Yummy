package com.frantun.yummy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.frantun.yummy.data.local.entity.IngredientEntity
import com.frantun.yummy.data.local.entity.OriginEntity

@Dao
interface IngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: IngredientEntity)
}
