package com.frantun.yummy.presentation.ui.search

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.frantun.yummy.R
import com.frantun.yummy.databinding.FragmentSearchBinding
import com.frantun.yummy.domain.model.RecipeModelUi
import com.frantun.yummy.domain.model.RecipesModelUi
import com.frantun.core.extensions.hideKeyboard
import com.frantun.core.extensions.navigateTo
import com.frantun.core.extensions.navigateUp
import com.frantun.core.extensions.setAsGone
import com.frantun.core.extensions.setAsVisible
import com.frantun.core.extensions.setSafeOnClickListener
import com.frantun.core.extensions.showKeyboard
import com.frantun.yummy.presentation.adapters.FavoriteAdapterListener
import com.frantun.yummy.presentation.adapters.RecipeAdapterListener
import com.frantun.yummy.presentation.adapters.RecipesAdapter
import com.frantun.core.base.BaseFragment
import com.frantun.yummy.presentation.ui.recipes.RecipesFragmentDirections
import com.frantun.yummy.presentation.ui.search.states.SearchState
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private val viewModel: SearchViewModel by viewModels()

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

        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)

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
    }

    private fun setupUi() {
        binding.apply {
            recipesRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = recipesAdapter
            }
            showKeyboard(searchEditText)
        }
    }

    private fun setupListeners() {
        binding.apply {
            backButton.setSafeOnClickListener {
                navigateUp()
            }
            searchEditText.apply {
                addTextChangedListener { editable ->
                    editable?.let {
                        viewModel.getRecipes(it.toString())
                    }
                }
                setOnEditorActionListener(TextView.OnEditorActionListener { _, _, _ ->
                    hideKeyboard()
                    return@OnEditorActionListener true
                })
            }
            recipesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    hideKeyboard()
                }
            })
        }
    }

    private fun setupAnimations() {
        postponeEnterTransition()
        binding.recipesRecyclerView.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private fun onStateUpdated(state: SearchState) {
        when (state) {
            is SearchState.Initialized -> onInitialized()
            is SearchState.ShowLoading -> onShowLoading()
            is SearchState.NoRecipes -> onNoRecipes()
            is SearchState.RetrievedRecipes -> onRetrievedRecipes(state.recipes)
            is SearchState.ShowError -> onShowError()
        }
    }

    private fun onInitialized() {
        binding.apply {
            searchAnimationView.setAsVisible()
            progressAnimationView.setAsGone()
            messageTextView.setAsGone()
            recipesRecyclerView.setAsGone()
        }
    }

    private fun onShowLoading() {
        binding.apply {
            searchAnimationView.setAsGone()
            progressAnimationView.setAsVisible()
            messageTextView.setAsGone()
            recipesRecyclerView.setAsVisible()
        }
    }

    private fun onNoRecipes() {
        binding.apply {
            searchAnimationView.setAsGone()
            progressAnimationView.setAsGone()
            messageTextView.apply {
                setAsVisible()
                text = getString(R.string.search_no_recipes)
            }
            recipesRecyclerView.setAsGone()
        }
    }

    private fun onRetrievedRecipes(recipes: RecipesModelUi) {
        binding.apply {
            searchAnimationView.setAsGone()
            progressAnimationView.setAsGone()
            messageTextView.setAsGone()
            recipesRecyclerView.setAsVisible()
        }
        recipesAdapter.submitList(recipes.recipes)
    }

    private fun onShowError() {
        binding.apply {
            searchAnimationView.setAsGone()
            progressAnimationView.setAsGone()
            messageTextView.apply {
                setAsVisible()
                text = getString(R.string.common_error)
            }
            recipesRecyclerView.setAsGone()
        }
    }

    private fun navigateToDetail(recipe: RecipeModelUi, thumbImageView: ShapeableImageView) {
        val action = RecipesFragmentDirections.actionToDetail(recipe)
        val extras = FragmentNavigatorExtras(
            thumbImageView to thumbImageView.transitionName
        )
        navigateTo(action, extras)
    }
}
