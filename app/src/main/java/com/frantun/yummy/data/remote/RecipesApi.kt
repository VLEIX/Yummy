package com.frantun.yummy.data.remote

import com.frantun.yummy.data.remote.dto.RecipesDto
import retrofit2.Response
import retrofit2.http.GET

interface RecipesApi {

    @GET("recipes/")
    suspend fun getRecipes(): Response<RecipesDto>
}
