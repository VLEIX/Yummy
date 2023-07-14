package com.frantun.yummy.presentation.ui.favorites

import com.frantun.yummy.common.BaseCoroutineViewModelStateTest
import com.frantun.yummy.common.Resource
import com.frantun.yummy.domain.model.FavoriteModelUi
import com.frantun.yummy.domain.model.RecipeModelUi
import com.frantun.yummy.domain.model.RecipesModelUi
import com.frantun.yummy.domain.usecase.DeleteFavoriteUseCase
import com.frantun.yummy.domain.usecase.GetFavoriteRecipesUseCase
import com.frantun.yummy.domain.usecase.InsertFavoriteUseCase
import com.frantun.yummy.other.assertStateOrder
import com.frantun.yummy.presentation.ui.favorites.states.FavoritesState
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
 * Unit tests for [FavoritesViewModel]
 *
 */
@ExperimentalCoroutinesApi
class FavoritesViewModelTest : BaseCoroutineViewModelStateTest<FavoritesState>() {

    private lateinit var sut: FavoritesViewModel

    @Mock
    private lateinit var getFavoriteRecipesUseCase: GetFavoriteRecipesUseCase

    @Mock
    private lateinit var insertFavoriteUseCase: InsertFavoriteUseCase

    @Mock
    private lateinit var deleteFavoriteUseCase: DeleteFavoriteUseCase

    @Mock
    private lateinit var recipe: RecipeModelUi

    @Mock
    private lateinit var favorite: FavoriteModelUi

    override fun before() {
        super.before()

        sut = FavoritesViewModel(
            getFavoriteRecipesUseCase,
            insertFavoriteUseCase,
            deleteFavoriteUseCase
        )

        whenever(recipe.recipeId).thenReturn(RECIPE_ID)
    }

    @Test
    fun `Retrieving favorite recipes should emit RetrievedRecipes when there are favorites`() =
        scope.runTest {
            val flow = flow { emit(Resource.Success(RecipesModelUi(listOf(recipe)))) }
            whenever(getFavoriteRecipesUseCase()).thenReturn(flow)

            val collectJob = backgroundScope.launch(UnconfinedTestDispatcher()) {
                sut.state.toList(stateList)
            }

            sut.getFavoriteRecipes()

            stateList.assertStateOrder(
                FavoritesState.ShowLoading::class,
                FavoritesState.RetrievedRecipes::class
            )
            with(stateList[1] as FavoritesState.RetrievedRecipes) {
                assertEquals(RECIPE_ID, recipes.recipes.first().recipeId)
            }

            collectJob.cancel()
        }

    @Test
    fun `Retrieving favorite recipes should emit NoFavorites when there are no favorites`() =
        scope.runTest {
            val flow = flow { emit(Resource.Success(RecipesModelUi(emptyList()))) }
            whenever(getFavoriteRecipesUseCase()).thenReturn(flow)

            val collectJob = backgroundScope.launch(UnconfinedTestDispatcher()) {
                sut.state.toList(stateList)
            }

            sut.getFavoriteRecipes()

            stateList.assertStateOrder(
                FavoritesState.ShowLoading::class,
                FavoritesState.NoFavorites::class
            )

            collectJob.cancel()
        }

    @Test
    fun `Updating favorite recipes should emit RetrievedRecipes when a favorite is inserted and there are favorites`() =
        scope.runTest {
            val flow = flow { emit(Resource.Success(Unit)) }
            whenever(insertFavoriteUseCase(any())).thenReturn(flow)
            val flowFavoriteRecipes =
                flow { emit(Resource.Success(RecipesModelUi(listOf(recipe)))) }
            whenever(getFavoriteRecipesUseCase()).thenReturn(flowFavoriteRecipes)

            val collectJob = backgroundScope.launch(UnconfinedTestDispatcher()) {
                sut.state.toList(stateList)
            }

            sut.updateFavorite(recipe)

            stateList.assertStateOrder(
                FavoritesState.ShowLoading::class,
                FavoritesState.RetrievedRecipes::class
            )
            with(stateList[1] as FavoritesState.RetrievedRecipes) {
                assertEquals(RECIPE_ID, recipes.recipes.first().recipeId)
            }

            collectJob.cancel()
        }

    @Test
    fun `Updating favorite recipes should emit RetrievedRecipes when a favorite is deleted and there are favorites`() =
        scope.runTest {
            whenever(recipe.favorite).thenReturn(favorite)
            val flow = flow { emit(Resource.Success(Unit)) }
            whenever(deleteFavoriteUseCase(any())).thenReturn(flow)
            val flowFavoriteRecipes =
                flow { emit(Resource.Success(RecipesModelUi(emptyList()))) }
            whenever(getFavoriteRecipesUseCase()).thenReturn(flowFavoriteRecipes)

            val collectJob = backgroundScope.launch(UnconfinedTestDispatcher()) {
                sut.state.toList(stateList)
            }

            sut.updateFavorite(recipe)

            stateList.assertStateOrder(
                FavoritesState.ShowLoading::class,
                FavoritesState.NoFavorites::class
            )

            collectJob.cancel()
        }

    private companion object {
        const val RECIPE_ID = "RECIPE_ID"
    }
}
