package com.frantun.yummy.presentation.ui.search

import com.frantun.yummy.common.BaseCoroutineViewModelStateTest
import com.frantun.core.common.Resource
import com.frantun.yummy.domain.model.FavoriteModelUi
import com.frantun.yummy.domain.model.RecipeModelUi
import com.frantun.yummy.domain.model.RecipesModelUi
import com.frantun.yummy.domain.usecase.DeleteFavoriteUseCase
import com.frantun.yummy.domain.usecase.GetRecipesByTextUseCase
import com.frantun.yummy.domain.usecase.InsertFavoriteUseCase
import com.frantun.yummy.other.assertStateOrder
import com.frantun.yummy.presentation.ui.search.states.SearchState
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
 * Unit tests for [SearchViewModel]
 *
 */
@ExperimentalCoroutinesApi
class SearchViewModelTest : BaseCoroutineViewModelStateTest<SearchState>() {

    private lateinit var sut: SearchViewModel

    @Mock
    private lateinit var getRecipesByTextUseCase: GetRecipesByTextUseCase

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

        sut = SearchViewModel(
            getRecipesByTextUseCase,
            insertFavoriteUseCase,
            deleteFavoriteUseCase
        )

        whenever(recipe.recipeId).thenReturn(RECIPE_ID)
    }

    @Test
    fun `Searching recipes should emit RetrievedRecipes when text has matches`() =
        scope.runTest {
            val flow = flow { emit(Resource.Success(RecipesModelUi(listOf(recipe)))) }
            whenever(getRecipesByTextUseCase(any())).thenReturn(flow)

            val collectJob = backgroundScope.launch(UnconfinedTestDispatcher()) {
                sut.state.toList(stateList)
            }

            sut.getRecipes(TEXT_TO_SEARCH)

            stateList.assertStateOrder(
                SearchState.Initialized::class,
                SearchState.ShowLoading::class,
                SearchState.RetrievedRecipes::class
            )
            with(stateList[2] as SearchState.RetrievedRecipes) {
                assertEquals(RECIPE_ID, recipes.recipes.first().recipeId)
            }

            collectJob.cancel()
        }

    @Test
    fun `Searching recipes should emit NoRecipes when text has no matches`() =
        scope.runTest {
            val flow = flow { emit(Resource.Success(RecipesModelUi(emptyList()))) }
            whenever(getRecipesByTextUseCase(any())).thenReturn(flow)

            val collectJob = backgroundScope.launch(UnconfinedTestDispatcher()) {
                sut.state.toList(stateList)
            }

            sut.getRecipes(TEXT_TO_SEARCH)

            stateList.assertStateOrder(
                SearchState.Initialized::class,
                SearchState.ShowLoading::class,
                SearchState.NoRecipes::class
            )

            collectJob.cancel()
        }

    @Test
    fun `Searching recipes should emit Initialized when text is empty`() =
        scope.runTest {
            val collectJob = backgroundScope.launch(UnconfinedTestDispatcher()) {
                sut.state.toList(stateList)
            }

            sut.getRecipes(TEXT_TO_SEARCH_EMPTY)

            stateList.assertStateOrder(
                SearchState.Initialized::class
            )

            collectJob.cancel()
        }

    @Test
    fun `Searching recipes should emit ShowError when result failed`() =
        scope.runTest {
            val value: Resource<RecipesModelUi> = Resource.Error(ERROR_MESSAGE)
            val flow = flow { emit(value) }
            whenever(getRecipesByTextUseCase(any())).thenReturn(flow)

            val collectJob = backgroundScope.launch(UnconfinedTestDispatcher()) {
                sut.state.toList(stateList)
            }

            sut.getRecipes(TEXT_TO_SEARCH)

            stateList.assertStateOrder(
                SearchState.Initialized::class,
                SearchState.ShowLoading::class,
                SearchState.ShowError::class
            )

            collectJob.cancel()
        }

    @Test
    fun `Updating favorite should emit RetrievedRecipes when a favorite is inserted`() =
        scope.runTest {
            val flow = flow { emit(Resource.Success(RecipesModelUi(listOf(recipe)))) }
            whenever(getRecipesByTextUseCase(any())).thenReturn(flow)
            val flowInsert = flow { emit(Resource.Success(Unit)) }
            whenever(insertFavoriteUseCase(any())).thenReturn(flowInsert)

            val collectJob = backgroundScope.launch(UnconfinedTestDispatcher()) {
                sut.state.toList(stateList)
            }

            sut.apply {
                getRecipes(TEXT_TO_SEARCH)
                updateFavorite(recipe)
            }

            stateList.assertStateOrder(
                SearchState.Initialized::class,
                SearchState.ShowLoading::class,
                SearchState.RetrievedRecipes::class,
                SearchState.ShowLoading::class,
                SearchState.RetrievedRecipes::class
            )
            with(stateList[4] as SearchState.RetrievedRecipes) {
                assertEquals(RECIPE_ID, recipes.recipes.first().recipeId)
            }

            collectJob.cancel()
        }

    @Test
    fun `Updating favorite should emit RetrievedRecipes when a favorite is deleted`() =
        scope.runTest {
            whenever(recipe.favorite).thenReturn(favorite)
            val flow = flow { emit(Resource.Success(RecipesModelUi(listOf(recipe)))) }
            whenever(getRecipesByTextUseCase(any())).thenReturn(flow)
            val flowDelete = flow { emit(Resource.Success(Unit)) }
            whenever(deleteFavoriteUseCase(any())).thenReturn(flowDelete)

            val collectJob = backgroundScope.launch(UnconfinedTestDispatcher()) {
                sut.state.toList(stateList)
            }

            sut.apply {
                getRecipes(TEXT_TO_SEARCH)
                updateFavorite(recipe)
            }

            stateList.assertStateOrder(
                SearchState.Initialized::class,
                SearchState.ShowLoading::class,
                SearchState.RetrievedRecipes::class,
                SearchState.ShowLoading::class,
                SearchState.RetrievedRecipes::class
            )
            with(stateList[4] as SearchState.RetrievedRecipes) {
                assertEquals(RECIPE_ID, recipes.recipes.first().recipeId)
            }

            collectJob.cancel()
        }

    private companion object {
        const val RECIPE_ID = "RECIPE_ID"
        const val ERROR_MESSAGE = "ERROR_MESSAGE"
        const val TEXT_TO_SEARCH_EMPTY = ""
        const val TEXT_TO_SEARCH = "TEXT_TO_SEARCH"
    }
}
