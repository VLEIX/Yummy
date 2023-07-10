package com.frantun.yummy.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class RecipeAndRelations(
    @Embedded
    val recipe: RecipeEntity,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "originRecipeId"
    )
    val origin: OriginEntity,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "ingredientRecipeId"
    )
    val ingredients: List<IngredientEntity>
)
