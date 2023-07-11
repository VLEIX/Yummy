package com.frantun.yummy.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.frantun.yummy.BuildConfig
import com.frantun.yummy.R
import com.frantun.yummy.common.Constants
import com.frantun.yummy.databinding.ViewIngredientBinding
import com.frantun.yummy.domain.model.IngredientModelUi

class IngredientsAdapter :
    ListAdapter<IngredientModelUi, RecyclerView.ViewHolder>(IngredientsAdapterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        IngredientViewHolder(
            ViewIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as IngredientViewHolder).bind(item)
    }

    class IngredientViewHolder constructor(private val binding: ViewIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: IngredientModelUi) {
            binding.apply {
                val imageUrl = BuildConfig.IMAGES_URL + item.name + Constants.EXTENSION_PNG
                Glide.with(thumbImageView.context)
                    .load(imageUrl)
                    .into(thumbImageView)
                nameTextView.text = item.name
                measureTextView.text = item.measure
            }
            itemView.animation = AnimationUtils.loadAnimation(itemView.context, R.anim.alpha)
        }
    }
}

class IngredientsAdapterDiffCallback : DiffUtil.ItemCallback<IngredientModelUi>() {
    override fun areItemsTheSame(
        oldItem: IngredientModelUi,
        newItem: IngredientModelUi
    ): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(
        oldItem: IngredientModelUi,
        newItem: IngredientModelUi
    ): Boolean {
        return oldItem == newItem
    }
}
