package com.frantun.yummy.presentation.ui.recipes

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import com.frantun.yummy.R
import com.frantun.yummy.databinding.FragmentRecipesBinding
import com.frantun.yummy.domain.model.RecipeModelUi
import com.frantun.yummy.domain.model.RecipesModelUi
import com.frantun.core.extensions.navigateTo
import com.frantun.core.extensions.setAsGone
import com.frantun.core.extensions.setAsVisible
import com.frantun.yummy.presentation.adapters.FavoriteAdapterListener
import com.frantun.yummy.presentation.adapters.RecipeAdapterListener
import com.frantun.yummy.presentation.adapters.RecipesAdapter
import com.frantun.core.base.BaseFragment
import com.frantun.yummy.presentation.ui.home.HomeViewModel
import com.frantun.yummy.presentation.ui.recipes.states.RecipesState
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : BaseFragment<FragmentRecipesBinding>(FragmentRecipesBinding::inflate) {

    private val viewModel: RecipesViewModel by viewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()

    private val recipesAdapter by lazy {
        RecipesAdapter(
            RecipeAdapterListener { recipe, thumbImageView ->
                navigateToDetail(recipe, thumbImageView)
            },
            FavoriteAdapterListener {
                viewModel.updateFavorite(it)
            },
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        setupListeners()
        setupAnimations()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    onStateUpdated(state)
                }
            }
        }
        homeViewModel.isUpdatedFavorite.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.getLocalRecipes()
            }
        })
    }

    private fun setupUi() {
        binding.recipesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recipesAdapter
        }
    }

    private fun setupAnimations() {
        postponeEnterTransition()
        binding.recipesRecyclerView.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private fun setupListeners() {
        binding.searchEditText.setOnClickListener {
            navigateToSearch()
        }
    }

    private fun onStateUpdated(state: RecipesState) {
        when (state) {
            is RecipesState.ShowLoading -> onShowLoading()
            is RecipesState.RetrievedRecipes -> onRetrievedRecipes(state.recipes)
            is RecipesState.ShowError -> onShowError()
        }
    }

    private fun onShowLoading() {
        binding.apply {
            progressAnimationView.setAsVisible()
            messageTextView.setAsGone()
            recipesRecyclerView.setAsGone()
        }
    }

    private fun onShowError() {
        binding.apply {
            progressAnimationView.setAsGone()
            messageTextView.apply {
                setAsVisible()
                text = getString(R.string.common_error)
            }
            recipesRecyclerView.setAsGone()
        }
    }

    private fun onRetrievedRecipes(recipes: RecipesModelUi) {
        binding.apply {
            progressAnimationView.setAsGone()
            messageTextView.setAsGone()
            recipesRecyclerView.setAsVisible()
        }
        recipesAdapter.submitList(recipes.recipes)
    }

    private fun navigateToSearch() {
        val extras = FragmentNavigatorExtras(
            binding.searchEditText to getString(R.string.search_edit_text)
        )
        navigateTo(RecipesFragmentDirections.actionToSearch(), extras)
    }

    private fun navigateToDetail(recipe: RecipeModelUi, thumbImageView: ShapeableImageView) {
        val action = RecipesFragmentDirections.actionToDetail(recipe)
        val extras = FragmentNavigatorExtras(
            thumbImageView to thumbImageView.transitionName
        )
        navigateTo(action, extras)
    }
}
