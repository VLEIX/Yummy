package com.frantun.yummy.presentation.ui.recipes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.frantun.yummy.databinding.FragmentRecipesBinding
import com.frantun.yummy.domain.model.RecipeModelUi
import com.frantun.yummy.domain.model.RecipesModelUi
import com.frantun.yummy.other.navigateTo
import com.frantun.yummy.other.setAsGone
import com.frantun.yummy.other.setAsVisible
import com.frantun.yummy.presentation.adapters.RecipeAdapterListener
import com.frantun.yummy.presentation.adapters.RecipesAdapter
import com.frantun.yummy.presentation.common.BaseFragment
import com.frantun.yummy.presentation.ui.recipes.states.RecipesState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : BaseFragment<FragmentRecipesBinding>(FragmentRecipesBinding::inflate) {

    private val viewModel: RecipesViewModel by viewModels()

    private val recipesAdapter by lazy {
        RecipesAdapter(RecipeAdapterListener {
            navigateToDetail(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    onStateUpdated(state)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        viewModel.getRecipes()
    }

    private fun setupUi() {
        binding.recipesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recipesAdapter
            itemAnimator = null
        }
    }

    private fun onStateUpdated(state: RecipesState?) {
        when (state) {
            is RecipesState.ShowLoading -> onShowLoading()
            is RecipesState.RetrievedRecipes -> onRetrievedRecipes(state.recipes)
            else -> Unit
        }
    }

    private fun onShowLoading() {
        binding.progressAnimationView.setAsVisible()
    }

    private fun onRetrievedRecipes(recipes: RecipesModelUi) {
        binding.progressAnimationView.setAsGone()
        recipesAdapter.submitList(recipes.recipes)
    }

    private fun navigateToDetail(recipe: RecipeModelUi) {
        val action = RecipesFragmentDirections.actionToDetail(recipe)
        navigateTo(action)
    }
}
