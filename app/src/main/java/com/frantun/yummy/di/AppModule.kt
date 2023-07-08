package com.frantun.yummy.di

import com.frantun.yummy.BuildConfig
import com.frantun.yummy.data.datasource.RecipesLocalDataSource
import com.frantun.yummy.data.datasource.RecipesLocalDataSourceImpl
import com.frantun.yummy.data.datasource.RecipesRemoteDataSource
import com.frantun.yummy.data.datasource.RecipesRemoteDataSourceImpl
import com.frantun.yummy.data.remote.RecipesApi
import com.frantun.yummy.data.repository.RecipesRepositoryImpl
import com.frantun.yummy.domain.repository.RecipesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object ProvidesModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.API_URL)
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun providesRecipesApi(retrofit: Retrofit): RecipesApi {
        return retrofit.create(RecipesApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class BindsModule {

    @Binds
    abstract fun providesRecipesRepository(recipesRepository: RecipesRepositoryImpl): RecipesRepository

    @Binds
    abstract fun providesRecipesLocalDataSource(
        recipesLocalDataSource: RecipesLocalDataSourceImpl
    ): RecipesLocalDataSource

    @Binds
    abstract fun providesRecipesRemoteDataSource(
        recipesRemoteDataSource: RecipesRemoteDataSourceImpl
    ): RecipesRemoteDataSource
}
