package com.frantun.yummy.di

import android.content.Context
import androidx.room.Room
import com.frantun.yummy.common.Constants.DATABASE_NAME
import com.frantun.yummy.data.local.AppDatabase
import com.frantun.yummy.data.local.dao.FavoriteDao
import com.frantun.yummy.data.local.dao.IngredientDao
import com.frantun.yummy.data.local.dao.OriginDao
import com.frantun.yummy.data.local.dao.RecipeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun providesRecipeDao(database: AppDatabase): RecipeDao {
        return database.recipeDao()
    }

    @Provides
    fun providesOriginDao(database: AppDatabase): OriginDao {
        return database.originDao()
    }

    @Provides
    fun providesIngredientDao(database: AppDatabase): IngredientDao {
        return database.ingredientDao()
    }

    @Provides
    fun providesFavoriteDao(database: AppDatabase): FavoriteDao {
        return database.favoriteDao()
    }

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}
