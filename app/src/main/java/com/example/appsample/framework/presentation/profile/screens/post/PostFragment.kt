package com.example.appsample.framework.presentation.profile.screens.post

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.appsample.R
import com.example.appsample.databinding.FragmentPostBinding
import com.example.appsample.framework.app.ui.MainNavController
import com.example.appsample.framework.base.presentation.BaseFragment
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.CompositeDelegateAdapter
import com.example.appsample.framework.base.presentation.delegateadapter.separators.DividerAdapterDelegate
import com.example.appsample.framework.base.presentation.delegateadapter.separators.EmptySpaceAdapterDelegate
import com.example.appsample.framework.presentation.profile.di.factories.viewmodels.GenericSavedStateViewModelFactory
import com.example.appsample.framework.presentation.profile.di.factories.viewmodels.implementations.PostViewModelFactory
import com.example.appsample.framework.presentation.profile.screens.post.adapters.PostBodyAdapterDelegate
import com.example.appsample.framework.presentation.profile.screens.post.adapters.PostCommentAdapterDelegate
import com.example.appsample.framework.presentation.profile.screens.post.adapters.PostSourceAdapterDelegate
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
    private val TAG = PostFragment::class.java.simpleName

    private val args: PostFragmentArgs by navArgs()

    private lateinit var mainNavController: MainNavController

    private var _binding: FragmentPostBinding? = null
    private val binding: FragmentPostBinding get() = _binding!!

    private val viewModel: PostViewModel by viewModels {
        GenericSavedStateViewModelFactory(postViewModelFactory, this)
    }

    private val adapter = CompositeDelegateAdapter(
        PostBodyAdapterDelegate(),
        PostCommentAdapterDelegate(),
        PostSourceAdapterDelegate(),
        DividerAdapterDelegate(),
        EmptySpaceAdapterDelegate()
    )

    override fun onAttach(context: Context) {
        try {
            mainNavController = context as MainNavController
        } catch (e: ClassCastException) {
            Log.e(TAG, "$context must implement MainNavController")
        }
        super.onAttach(context)
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
        _binding!!.recyclerView.adapter = adapter

        _binding!!.backButton.setOnClickListener { closeFragment() }

        // will be called only once, since we consider postId should not be null in ViewModel after setting once
        if (viewModel.isPostIdNull()) {
            viewModel.setPostId(postId)
            viewModel.startSearch()
        }
        return binding.root
    }

    private fun closeFragment() {
        mainNavController.navController().popBackStack()
    }
}