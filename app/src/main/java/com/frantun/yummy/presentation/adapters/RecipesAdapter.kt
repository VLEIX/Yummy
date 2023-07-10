package com.frantun.yummy.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.frantun.yummy.R
import com.frantun.yummy.databinding.ViewRecipeBinding
import com.frantun.yummy.domain.model.RecipeModelUi

class RecipesAdapter(private val listener: RecipeAdapterListener) :
    ListAdapter<RecipeModelUi, RecyclerView.ViewHolder>(RecipesAdapterDiffCallback()) {

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

        fun bind(item: RecipeModelUi, listener: RecipeAdapterListener) {
            binding.apply {
                nameTextView.text = item.name
                shortInformationTextView.text = itemView.context.getString(
                    R.string.recipes_recipe_short_information,
                    item.ingredients.size,
                    item.origin.name
                )
                Glide.with(thumbImageView.context)
                    .load(item.thumb)
                    .into(thumbImageView)
            }
            itemView.apply {
                animation = AnimationUtils.loadAnimation(itemView.context, R.anim.alpha)
                setOnClickListener {
                    listener.onClick(item)
                }
            }
        }
    }
}

class RecipesAdapterDiffCallback : DiffUtil.ItemCallback<RecipeModelUi>() {
    override fun areItemsTheSame(
        oldItem: RecipeModelUi,
        newItem: RecipeModelUi
    ): Boolean {
        return oldItem.recipeId == newItem.recipeId && oldItem.origin.name == newItem.origin.name
    }

    override fun areContentsTheSame(
        oldItem: RecipeModelUi,
        newItem: RecipeModelUi
    ): Boolean {
        return oldItem == newItem
    }
}
