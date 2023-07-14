package com.frantun.yummy.data.datasource

import com.frantun.yummy.common.BaseCoroutineTest
import com.frantun.yummy.data.local.dao.FavoriteDao
import com.frantun.yummy.domain.model.FavoriteModelUi
import java.util.Date
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify

/**
 * Unit tests for [FavoritesLocalDataSourceImpl]
 *
 */
@ExperimentalCoroutinesApi
class FavoritesLocalDataSourceImplTest : BaseCoroutineTest() {

    private lateinit var sut: FavoritesLocalDataSourceImpl

    @Mock
    private lateinit var favoriteDao: FavoriteDao

    private val favoriteModelUi by lazy {
        FavoriteModelUi(
            FAVORITE_ID,
            Date()
        )
    }

    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)

        sut = FavoritesLocalDataSourceImpl(favoriteDao)
    }

    @Test
    fun `Inserting favorite should call favorite dao insert`() = scope.runTest {
        sut.insertFavorite(FAVORITE_ID)

        verify(favoriteDao).insertFavorite(any())
    }

    @Test
    fun `Deleting favorite should call favorite dao delete`() = scope.runTest {
        sut.deleteFavorite(favoriteModelUi)

        verify(favoriteDao).deleteFavorite(any())
    }

    private companion object {
        const val FAVORITE_ID = "FAVORITE_ID"
    }
}
