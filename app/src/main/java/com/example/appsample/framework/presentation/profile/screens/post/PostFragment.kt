package com.example.appsample.framework.presentation.profile.screens.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.appsample.R
import com.example.appsample.databinding.FragmentPostBinding
import com.example.appsample.framework.base.presentation.BaseFragment
import com.example.appsample.framework.presentation.profile.di.factories.viewmodels.GenericSavedStateViewModelFactory
import com.example.appsample.framework.presentation.profile.di.factories.viewmodels.implementations.PostViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@FlowPreview
class PostFragment @Inject
constructor(
    private val postViewModelFactory: PostViewModelFactory
) : BaseFragment(R.layout.fragment_post) {

    private val args: PostFragmentArgs by navArgs()

    private var _binding: FragmentPostBinding? = null
    private val binding: FragmentPostBinding get() = _binding!!

    private val viewModel: PostViewModel by viewModels {
        GenericSavedStateViewModelFactory(postViewModelFactory, this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val postId = args.postId

        _binding = FragmentPostBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
        }
        _binding!!.recyclerView.adapter = PostAdapter()

        // will be called only once, since we consider postId should not be null in ViewModel after setting once
        if (viewModel.isPostIdNull()) {
            viewModel.setPostId(postId)
            viewModel.startSearch()
        }

        return binding.root
    }
}