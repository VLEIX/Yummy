package com.frantun.yummy.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class RecipeFull(
    @Embedded val recipe: RecipeEntity,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "originRecipeId"
    )
    val origin: OriginEntity
)
