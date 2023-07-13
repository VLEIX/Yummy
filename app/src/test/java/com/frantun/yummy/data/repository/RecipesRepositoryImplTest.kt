package com.frantun.yummy.data.repository

import com.frantun.yummy.common.BaseCoroutineTest
import com.frantun.yummy.common.Resource
import com.frantun.yummy.data.datasource.RecipesLocalDataSource
import com.frantun.yummy.data.datasource.RecipesRemoteDataSource
import com.frantun.yummy.data.local.entity.RecipeAndRelations
import com.frantun.yummy.data.remote.dto.RecipesDto
import com.frantun.yummy.domain.mappers.RecipeDataMapper
import com.frantun.yummy.domain.model.RecipeModelUi
import com.frantun.yummy.domain.model.RecipesModelUi
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

/**
 * Unit tests for [RecipesRepositoryImpl]
 *
 */
@ExperimentalCoroutinesApi
class RecipesRepositoryImplTest : BaseCoroutineTest() {

    private lateinit var sut: RecipesRepositoryImpl

    @Mock
    private lateinit var recipesRemoteDataSource: RecipesRemoteDataSource

    @Mock
    private lateinit var recipesLocalDataSource: RecipesLocalDataSource

    @Mock
    private lateinit var recipeDataMapper: RecipeDataMapper

    @Mock
    private lateinit var recipesDto: RecipesDto

    @Mock
    private lateinit var recipeAndRelations: RecipeAndRelations

    @Mock
    private lateinit var recipeModelUi: RecipeModelUi

    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)

        sut =
            RecipesRepositoryImpl(recipesRemoteDataSource, recipesLocalDataSource, recipeDataMapper)
    }

    @Test
    fun `Retrieving recipes should return recipes when remote data source responds successfully`() =
        scope.runTest {
            whenever(recipesRemoteDataSource.getRecipes()).thenReturn(Resource.Success(recipesDto))
            whenever(recipesLocalDataSource.getRecipes()).thenReturn(listOf(recipeAndRelations))
            whenever(recipeDataMapper.map(any())).thenReturn(RecipesModelUi(listOf(recipeModelUi)))

            val recipes = sut.getRecipes()

            assertEquals(1, recipes.data?.recipes?.size)
        }

    @Test
    fun `Retrieving recipes should return recipes when remote data source responds successfully with empty content`() =
        scope.runTest {
            whenever(recipesRemoteDataSource.getRecipes()).thenReturn(Resource.Success(recipesDto))
            whenever(recipesLocalDataSource.getRecipes()).thenReturn(listOf(recipeAndRelations))
            whenever(recipeDataMapper.map(any())).thenReturn(RecipesModelUi(emptyList()))

            val recipes = sut.getRecipes()

            assertEquals(0, recipes.data?.recipes?.size)
        }

    @Test
    fun `Retrieving recipes should return recipes when local data source responds successfully`() =
        scope.runTest {
            whenever(recipesRemoteDataSource.getRecipes()).thenReturn(Resource.Error(ERROR_MESSAGE))
            whenever(recipesLocalDataSource.getRecipes()).thenReturn(listOf(recipeAndRelations))
            whenever(recipeDataMapper.map(any())).thenReturn(RecipesModelUi(listOf(recipeModelUi)))

            val recipes = sut.getRecipes()

            assertEquals(1, recipes.data?.recipes?.size)
        }

    @Test
    fun `Retrieving recipes should return recipes when local data source responds successfully with empty content`() =
        scope.runTest {
            whenever(recipesRemoteDataSource.getRecipes()).thenReturn(Resource.Error(ERROR_MESSAGE))
            whenever(recipesLocalDataSource.getRecipes()).thenReturn(listOf(recipeAndRelations))
            whenever(recipeDataMapper.map(any())).thenReturn(RecipesModelUi(emptyList()))

            val recipes = sut.getRecipes()

            assertEquals(0, recipes.data?.recipes?.size)
        }

    @Test(expected = Throwable::class)
    fun `Retrieving recipes should throw an exception when remote data source responds with error`() =
        scope.runTest {
            whenever(recipesRemoteDataSource.getRecipes()).thenThrow(Throwable())

            sut.getRecipes()
        }

    @Test
    fun `Retrieving recipes by text should return recipes when local data source responds successfully`() =
        scope.runTest {
            whenever(recipesLocalDataSource.getRecipesByText(any())).thenReturn(listOf(recipeAndRelations))
            whenever(recipeDataMapper.map(any())).thenReturn(RecipesModelUi(listOf(recipeModelUi)))

            val recipes = sut.getRecipesByText(TEXT_TO_SEARCH)

            assertEquals(1, recipes.data?.recipes?.size)
        }

    @Test(expected = Throwable::class)
    fun `Retrieving recipes by text should throw an exception when local data source responds with error`() =
        scope.runTest {
            whenever(recipesLocalDataSource.getRecipesByText(any())).thenThrow(Throwable())

            sut.getRecipesByText(TEXT_TO_SEARCH)
        }

    private companion object {
        const val ERROR_MESSAGE = "ERROR_MESSAGE"
        const val TEXT_TO_SEARCH = "TEXT_TO_SEARCH"
    }
}
