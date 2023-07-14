package com.frantun.yummy.domain.mappers

import android.util.Log
import com.frantun.yummy.common.Mapper
import com.frantun.yummy.data.local.entity.RecipeAndRelations
import com.frantun.yummy.domain.model.FavoriteModelUi
import com.frantun.yummy.domain.model.IngredientModelUi
import com.frantun.yummy.domain.model.OriginModelUi
import com.frantun.yummy.domain.model.RecipeModelUi
import com.frantun.yummy.domain.model.RecipesModelUi
import javax.inject.Inject

class RecipeDataMapper @Inject constructor() : Mapper<List<RecipeAndRelations>, RecipesModelUi> {

    override fun map(input: List<RecipeAndRelations>): RecipesModelUi {
        return input.runCatching {
            val recipes = this.map {
                val origin = OriginModelUi(
                    name = it.origin.name,
                    description = it.origin.description,
                    latitude = it.origin.latitude,
                    longitude = it.origin.longitude,
                )
                val ingredients = it.ingredients.map { ingredient ->
                    IngredientModelUi(
                        ingredient.name,
                        ingredient.measure,
                    )
                }.toList()
                val favorite = it.favorite?.let { fav ->
                    FavoriteModelUi(
                        favoriteId = fav.favoriteId,
                        timestamp = fav.timestamp,
                    )
                }

                RecipeModelUi(
                    recipeId = it.recipe.recipeId,
                    name = it.recipe.name,
                    category = it.recipe.category,
                    instructions = it.recipe.instructions,
                    thumb = it.recipe.thumb,
                    video = it.recipe.video,
                    origin = origin,
                    ingredients = ingredients,
                    favorite = favorite
                )
            }.toList()

            RecipesModelUi(recipes)
        }.getOrElse {
            Log.e(TAG, "Failed mapping List<RecipeAndRelations> to RecipesModelUi: ${it.message}")
            throw it
        }
    }

    private companion object {
        const val TAG = "RecipeDataMapper"
    }
}
