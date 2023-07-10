package com.frantun.yummy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.frantun.yummy.data.local.dao.OriginDao
import com.frantun.yummy.data.local.dao.RecipeDao
import com.frantun.yummy.data.local.entity.OriginEntity
import com.frantun.yummy.data.local.entity.RecipeEntity

@Database(
    entities = [RecipeEntity::class, OriginEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun originDao(): OriginDao
}
