package com.frantun.yummy.data.repository

import com.frantun.yummy.common.BaseCoroutineTest
import com.frantun.yummy.data.datasource.FavoritesLocalDataSource
import com.frantun.yummy.domain.model.FavoriteModelUi
import java.util.Date
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

/**
 * Unit tests for [FavoritesRepositoryImpl]
 *
 */
@ExperimentalCoroutinesApi
class FavoritesRepositoryImplTest : BaseCoroutineTest() {

    private lateinit var sut: FavoritesRepositoryImpl

    @Mock
    private lateinit var favoritesLocalDataSource: FavoritesLocalDataSource

    private val favoriteModelUi by lazy {
        FavoriteModelUi(
            FAVORITE_ID,
            Date()
        )
    }

    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)

        sut = FavoritesRepositoryImpl(favoritesLocalDataSource)
    }

    @Test
    fun `Inserting favorite should call favorite local data source insert`() =
        scope.runTest {
            sut.insertFavorite(FAVORITE_ID)

            verify(favoritesLocalDataSource).insertFavorite(FAVORITE_ID)
        }

    @Test
    fun `Deleting favorite should call favorite local data source delete`() =
        scope.runTest {
            sut.deleteFavorite(favoriteModelUi)

            verify(favoritesLocalDataSource).deleteFavorite(favoriteModelUi)
        }

    private companion object {
        const val FAVORITE_ID = "FAVORITE_ID"
    }
}
