package com.frantun.yummy.data.datasource

import com.frantun.yummy.common.BaseCoroutineTest
import com.frantun.yummy.data.remote.RecipesApi
import com.frantun.yummy.data.remote.dto.RecipeDto
import com.frantun.yummy.data.remote.dto.RecipesDto
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import retrofit2.Response

/**
 * Unit tests for [RecipesRemoteDataSourceImpl]
 *
 */
@ExperimentalCoroutinesApi
class RecipesRemoteDataSourceImplTest : BaseCoroutineTest() {

    private lateinit var sut: RecipesRemoteDataSourceImpl

    @Mock
    private lateinit var recipesApi: RecipesApi

    @Mock
    private lateinit var recipesDto: RecipesDto

    @Mock
    private lateinit var recipeDto: RecipeDto

    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)

        sut = RecipesRemoteDataSourceImpl(recipesApi)
    }

    @Test
    fun `Retrieving recipes should return recipes when api responds successfully`() =
        scope.runTest {
            whenever(recipesDto.recipes).thenReturn(listOf(recipeDto))
            whenever(recipesApi.getRecipes()).thenReturn(Response.success(recipesDto))

            val recipes = sut.getRecipes()

            assertEquals(1, recipes.data?.recipes?.size)
        }

    @Test
    fun `Retrieving recipes should return error when api responds with error`() =
        scope.runTest {
            whenever(recipesApi.getRecipes()).thenReturn(Response.error(ERROR_CODE, ERROR_BODY))

            val recipes = sut.getRecipes()

            assertEquals(ERROR_MESSAGE, recipes.message)
        }

    private companion object {
        const val ERROR_CODE = 500
        val ERROR_BODY = "{}".toResponseBody("application/json".toMediaTypeOrNull())
        const val ERROR_MESSAGE =
            "Couldn't reach server for the following reason: 500 Response.error()"
    }
}
