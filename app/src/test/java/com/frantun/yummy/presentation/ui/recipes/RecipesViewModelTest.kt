package com.frantun.yummy.presentation.ui.recipes

import com.frantun.yummy.common.BaseCoroutineViewModelStateTest
import com.frantun.yummy.common.Resource
import com.frantun.yummy.domain.model.RecipeModelUi
import com.frantun.yummy.domain.model.RecipesModelUi
import com.frantun.yummy.domain.usecase.GetRecipesUseCase
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
import org.mockito.kotlin.whenever

/**
 * Unit tests for [RecipesViewModel]
 *
 */
@ExperimentalCoroutinesApi
class RecipesViewModelTest : BaseCoroutineViewModelStateTest<RecipesState?>() {

    private lateinit var sut: RecipesViewModel

    @Mock
    private lateinit var getRecipesUseCase: GetRecipesUseCase

    @Mock
    private lateinit var recipe: RecipeModelUi

    override fun before() {
        super.before()

        whenever(recipe.recipeId).thenReturn(ID_RECIPE)
    }

    private suspend fun setupSut() {
        sut = RecipesViewModel(getRecipesUseCase)
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
                assertEquals(ID_RECIPE, recipes.recipes.first().recipeId)
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

    private companion object {
        const val ID_RECIPE = "ID_RECIPE"
        const val ERROR_MESSAGE = "ERROR_MESSAGE"
    }
}
