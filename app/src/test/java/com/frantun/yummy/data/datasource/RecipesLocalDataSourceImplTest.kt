package com.frantun.yummy.data.datasource

import com.frantun.yummy.common.BaseCoroutineTest
import com.frantun.yummy.data.local.dao.IngredientDao
import com.frantun.yummy.data.local.dao.OriginDao
import com.frantun.yummy.data.local.dao.RecipeDao
import com.frantun.yummy.data.local.entity.RecipeAndRelations
import com.frantun.yummy.data.remote.dto.IngredientDto
import com.frantun.yummy.data.remote.dto.OriginDto
import com.frantun.yummy.data.remote.dto.RecipeDto
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Unit tests for [RecipesLocalDataSourceImpl]
 *
 */
@ExperimentalCoroutinesApi
class RecipesLocalDataSourceImplTest : BaseCoroutineTest() {

    private lateinit var sut: RecipesLocalDataSourceImpl

    @Mock
    private lateinit var recipeDao: RecipeDao

    @Mock
    private lateinit var originDao: OriginDao

    @Mock
    private lateinit var ingredientDao: IngredientDao

    @Mock
    private lateinit var recipeAndRelations: RecipeAndRelations

    private val originDto by lazy {
        OriginDto(
            ORIGIN_NAME,
            ORIGIN_DESCRIPTION,
            ORIGIN_LATITUDE,
            ORIGIN_LONGITUDE
        )
    }

    private val ingredientDto by lazy {
        IngredientDto(
            INGREDIENT_1_NAME,
            INGREDIENT_1_MEASURE
        )
    }

    private val ingredientDto2 by lazy {
        IngredientDto(
            INGREDIENT_2_NAME,
            INGREDIENT_2_MEASURE
        )
    }

    private val recipeDto by lazy {
        RecipeDto(
            RECIPE_ID,
            RECIPE_NAME,
            RECIPE_CATEGORY,
            originDto,
            RECIPE_INSTRUCTIONS,
            RECIPE_THUMB,
            RECIPE_TAGS,
            RECIPE_VIDEO,
            listOf(ingredientDto, ingredientDto2)
        )
    }

    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)

        sut = RecipesLocalDataSourceImpl(recipeDao, originDao, ingredientDao)
    }

    @Test
    fun `Inserting recipes should call recipe dao, origin dao and ingredient dao`() =
        scope.runTest {
            sut.insertRecipes(listOf(recipeDto))

            verify(recipeDao).insertRecipe(any())
            verify(originDao).insertOrigin(any())
            verify(ingredientDao, times(2)).insertIngredient(any())
        }

    @Test
    fun `Retrieving recipes should return recipes when recipe dao responds successfully with recipes`() =
        scope.runTest {
            whenever(recipeDao.getRecipes()).thenReturn(listOf(recipeAndRelations))

            val recipes = sut.getRecipes()

            assertEquals(1, recipes.size)
        }

    @Test
    fun `Retrieving recipes by text should return recipes when recipe dao responds successfully with recipes`() =
        scope.runTest {
            whenever(recipeDao.getRecipesByText(any())).thenReturn(listOf(recipeAndRelations))

            val recipes = sut.getRecipesByText(TEXT_TO_SEARCH)

            assertEquals(1, recipes.size)
        }

    private companion object {
        const val TEXT_TO_SEARCH = "TEXT_TO_SEARCH"
        const val ORIGIN_NAME = "ORIGIN_NAME"
        const val ORIGIN_DESCRIPTION = "ORIGIN_DESCRIPTION"
        const val ORIGIN_LATITUDE = 0.0
        const val ORIGIN_LONGITUDE = 0.0
        const val INGREDIENT_1_NAME = "INGREDIENT_1_NAME"
        const val INGREDIENT_1_MEASURE = "INGREDIENT_1_MEASURE"
        const val INGREDIENT_2_NAME = "INGREDIENT_2_NAME"
        const val INGREDIENT_2_MEASURE = "INGREDIENT_2_MEASURE"
        const val RECIPE_ID = "RECIPE_ID"
        const val RECIPE_NAME = "RECIPE_NAME"
        const val RECIPE_CATEGORY = "RECIPE_CATEGORY"
        const val RECIPE_INSTRUCTIONS = "RECIPE_INSTRUCTIONS"
        const val RECIPE_THUMB = "RECIPE_THUMB"
        const val RECIPE_TAGS = "RECIPE_TAGS"
        const val RECIPE_VIDEO = "RECIPE_VIDEO"
    }
}
