package com.frantun.yummy.presentation.ui.recipes

import com.frantun.yummy.common.BaseCoroutineViewModelStateTest
import com.frantun.yummy.common.Resource
import com.frantun.yummy.domain.model.FavoriteModelUi
import com.frantun.yummy.domain.model.RecipeModelUi
import com.frantun.yummy.domain.model.RecipesModelUi
import com.frantun.yummy.domain.usecase.DeleteFavoriteUseCase
import com.frantun.yummy.domain.usecase.GetLocalRecipesUseCase
import com.frantun.yummy.domain.usecase.GetRecipesUseCase
import com.frantun.yummy.domain.usecase.InsertFavoriteUseCase
import com.frantun.yummy.other.assertStateOrder
import com.frantun.yummy.presentation.ui.recipes.states.RecipesState
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

/**
 * Unit tests for [RecipesViewModel]
 *
 */
@ExperimentalCoroutinesApi
class RecipesViewModelTest : BaseCoroutineViewModelStateTest<RecipesState>() {

    private lateinit var sut: RecipesViewModel

    @Mock
    private lateinit var getRecipesUseCase: GetRecipesUseCase

    @Mock
    private lateinit var insertFavoriteUseCase: InsertFavoriteUseCase

    @Mock
    private lateinit var deleteFavoriteUseCase: DeleteFavoriteUseCase

    @Mock
    private lateinit var getLocalRecipesUseCase: GetLocalRecipesUseCase

    @Mock
    private lateinit var recipe: RecipeModelUi

    @Mock
    private lateinit var favorite: FavoriteModelUi

    override fun before() {
        super.before()

        whenever(recipe.recipeId).thenReturn(RECIPE_ID)
    }

    private suspend fun setupSut() {
        sut = RecipesViewModel(
            getRecipesUseCase,
            insertFavoriteUseCase,
            deleteFavoriteUseCase,
            getLocalRecipesUseCase,
        )
        sut.state.toList(stateList)
    }

    @Test
    fun `Starting flow should emit RetrievedRecipes when recipes service successes`() =
        scope.runTest {
            val flow = flow { emit(Resource.Success(RecipesModelUi(listOf(recipe)))) }
            whenever(getRecipesUseCase()).thenReturn(flow)

            val collectJob = backgroundScope.launch(UnconfinedTestDispatcher()) {
                setupSut()
            }

            stateList.assertStateOrder(
                RecipesState.ShowLoading::class,
                RecipesState.RetrievedRecipes::class
            )
            with(stateList[1] as RecipesState.RetrievedRecipes) {
                assertEquals(RECIPE_ID, recipes.recipes.first().recipeId)
            }

            collectJob.cancel()
        }

    @Test
    fun `Starting flow should emit ShowError when recipes service failed`() =
        scope.runTest {
            val value: Resource<RecipesModelUi> = Resource.Error(ERROR_MESSAGE)
            val flow = flow { emit(value) }
            whenever(getRecipesUseCase()).thenReturn(flow)

            val collectJob = backgroundScope.launch(UnconfinedTestDispatcher()) {
                setupSut()
            }

            stateList.assertStateOrder(
                RecipesState.ShowLoading::class,
                RecipesState.ShowError::class
            )

            collectJob.cancel()
        }

    @Test
    fun `Updating favorite should emit RetrievedRecipes when a favorite is inserted`() =
        scope.runTest {
            val flow = flow { emit(Resource.Success(RecipesModelUi(listOf(recipe)))) }
            whenever(getRecipesUseCase()).thenReturn(flow)
            whenever(getLocalRecipesUseCase()).thenReturn(flow)
            val flowInsert = flow { emit(Resource.Success(Unit)) }
            whenever(insertFavoriteUseCase(any())).thenReturn(flowInsert)

            val collectJob = backgroundScope.launch(UnconfinedTestDispatcher()) {
                setupSut()
            }

            sut.updateFavorite(recipe)

            stateList.assertStateOrder(
                RecipesState.ShowLoading::class,
                RecipesState.RetrievedRecipes::class
            )
            with(stateList[1] as RecipesState.RetrievedRecipes) {
                assertEquals(RECIPE_ID, recipes.recipes.first().recipeId)
            }

            collectJob.cancel()
        }

    @Test
    fun `Updating favorite should emit RetrievedRecipes when a favorite is deleted`() =
        scope.runTest {
            whenever(recipe.favorite).thenReturn(favorite)
            val flow = flow { emit(Resource.Success(RecipesModelUi(listOf(recipe)))) }
            whenever(getRecipesUseCase()).thenReturn(flow)
            whenever(getLocalRecipesUseCase()).thenReturn(flow)
            val flowDelete = flow { emit(Resource.Success(Unit)) }
            whenever(deleteFavoriteUseCase(any())).thenReturn(flowDelete)

            val collectJob = backgroundScope.launch(UnconfinedTestDispatcher()) {
                setupSut()
            }

            sut.updateFavorite(recipe)

            stateList.assertStateOrder(
                RecipesState.ShowLoading::class,
                RecipesState.RetrievedRecipes::class
            )
            with(stateList[1] as RecipesState.RetrievedRecipes) {
                assertEquals(RECIPE_ID, recipes.recipes.first().recipeId)
            }

            collectJob.cancel()
        }

    @Test
    fun `Retrieving recipes should emit RetrievedRecipes when recipes local successes`() =
        scope.runTest {
            val flow = flow { emit(Resource.Success(RecipesModelUi(listOf(recipe)))) }
            whenever(getRecipesUseCase()).thenReturn(flow)
            whenever(getLocalRecipesUseCase()).thenReturn(flow)

            val collectJob = backgroundScope.launch(UnconfinedTestDispatcher()) {
                setupSut()
            }

            sut.getLocalRecipes()

            stateList.assertStateOrder(
                RecipesState.ShowLoading::class,
                RecipesState.RetrievedRecipes::class
            )
            with(stateList[1] as RecipesState.RetrievedRecipes) {
                assertEquals(RECIPE_ID, recipes.recipes.first().recipeId)
            }

            collectJob.cancel()
        }

    private companion object {
        const val RECIPE_ID = "RECIPE_ID"
        const val ERROR_MESSAGE = "ERROR_MESSAGE"
    }
}
