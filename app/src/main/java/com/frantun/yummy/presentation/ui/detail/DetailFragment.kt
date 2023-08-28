package com.frantun.yummy.presentation.ui.detail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.frantun.yummy.R
import com.frantun.yummy.databinding.FragmentDetailBinding
import com.frantun.yummy.domain.model.OriginModelUi
import com.frantun.core.extensions.navigateTo
import com.frantun.core.extensions.navigateUp
import com.frantun.core.extensions.setAsGone
import com.frantun.core.extensions.setAsInvisible
import com.frantun.core.extensions.setAsVisible
import com.frantun.core.extensions.setSafeOnClickListener
import com.frantun.yummy.presentation.adapters.IngredientsAdapter
import com.frantun.core.base.BaseFragment
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    private val args by navArgs<DetailFragmentArgs>()
    private val recipe by lazy { args.recipe }

    private val ingredientsAdapter by lazy { IngredientsAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)

        setupTransitions()
        setupUi()
        setupListeners()
    }

    private fun setupTransitions() {
        binding.thumbImageView.transitionName = recipe.thumb
    }

    private fun setupUi() {
        binding.apply {
            videoPlayerView.clipToOutline = true
            titleTextView.text = recipe.name
            Glide.with(requireContext())
                .load(recipe.thumb)
                .into(thumbImageView)
            ingredientsTextView.text =
                getString(R.string.detail_ingredients, recipe.ingredients.size)
            ingredientsRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = ingredientsAdapter
                itemAnimator = null
            }
            ingredientsAdapter.submitList(recipe.ingredients)
            preparationTextView.text = recipe.instructions
        }
    }

    private fun setupListeners() {
        binding.apply {
            backButton.setSafeOnClickListener {
                navigateUp()
            }
            mapButton.setSafeOnClickListener {
                navigateToMap(recipe.origin)
            }
            playButton.setSafeOnClickListener {
                showVideoComponents()
                playYoutubeVideo(recipe.video)
            }
        }
    }

    private fun showVideoComponents() {
        binding.apply {
            thumbImageView.setAsInvisible()
            playButton.setAsGone()
            videoPlayerView.setAsVisible()
        }
    }

    private fun playYoutubeVideo(videoKey: String) {
        binding.videoPlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoKey, 0F)
            }
        })
    }

    private fun navigateToMap(origin: OriginModelUi) {
        val action = DetailFragmentDirections.actionToMap(origin)
        navigateTo(action)
    }
}
