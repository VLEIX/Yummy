package com.frantun.yummy.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.frantun.yummy.R
import com.frantun.yummy.data.local.entity.RecipeFull
import com.frantun.yummy.databinding.ViewRecipeBinding

class RecipesAdapter(private val listener: RecipeAdapterListener) :
    ListAdapter<RecipeFull, RecyclerView.ViewHolder>(RecipesAdapterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        RecipeViewHolder(
            ViewRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as RecipeViewHolder).bind(item, listener)
    }

    class RecipeViewHolder constructor(private val binding: ViewRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RecipeFull, listener: RecipeAdapterListener) {
            binding.apply {
                nameTextView.text = item.recipe.name
                shortInformationTextView.text = itemView.context.getString(
                    R.string.recipes_recipe_short_information,
                    "10",
                    item.origin.name
                )
                Glide.with(thumbImageView.context)
                    .load(item.recipe.thumb)
                    .into(thumbImageView)
            }
            itemView.apply {
                animation = AnimationUtils.loadAnimation(itemView.context, R.anim.alpha)
                setOnClickListener {
                    listener.onClick(item.recipe)
                }
            }
        }
    }
}

class RecipesAdapterDiffCallback : DiffUtil.ItemCallback<RecipeFull>() {
    override fun areItemsTheSame(
        oldItem: RecipeFull,
        newItem: RecipeFull
    ): Boolean {
        return oldItem.recipe.recipeId == newItem.recipe.recipeId && oldItem.origin.name == newItem.origin.name
    }

    override fun areContentsTheSame(
        oldItem: RecipeFull,
        newItem: RecipeFull
    ): Boolean {
        return oldItem == newItem
    }
}
