package com.example.appsample.framework.presentation.profile.screens.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.example.appsample.R
import com.example.appsample.databinding.FragmentProfileBinding
import com.example.appsample.framework.app.ui.MainNavController
import com.example.appsample.framework.base.presentation.BaseFragment
import com.example.appsample.framework.presentation.profile.di.factories.viewmodels.GenericSavedStateViewModelFactory
import com.example.appsample.framework.presentation.profile.di.factories.viewmodels.implementations.ProfileViewModelFactory
import com.example.appsample.framework.presentation.profile.model.AlbumModel
import com.example.appsample.framework.presentation.profile.model.PostModel
import com.example.appsample.framework.presentation.profile.screens.album.ALBUM_ID
import com.example.appsample.framework.presentation.profile.screens.album.ALBUM_TITLE
import com.example.appsample.framework.presentation.profile.screens.post.POST_ID
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@FlowPreview
class ProfileFragment @Inject
constructor(
    private val profileViewModelFactory: ProfileViewModelFactory
) : BaseFragment(R.layout.fragment_profile) {
    // checking userName
    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels {
        GenericSavedStateViewModelFactory(profileViewModelFactory, this)
    }

    private lateinit var mainNavController: MainNavController

    private val goToAlbumFragment: ((ImageView, AlbumModel, Int) -> Unit) =
        { _, albumModel, _ ->
            val bundle = bundleOf(ALBUM_ID to albumModel.id, ALBUM_TITLE to albumModel.title)

            mainNavController.navController()
                .navigate(R.id.action_profileFragment_to_albumFragment, bundle)
        }

    private val goToPostFragment: ((PostModel) -> Unit) =
        { postModel ->
            Log.d(TAG, "goToPostFragment called")
            val bundle = bundleOf(POST_ID to postModel.id)

            mainNavController.navController()
                .navigate(R.id.action_profileFragment_to_postFragment, bundle)
        }

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
        _binding = FragmentProfileBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
        }

        _binding!!.profileRv.adapter = ProfileAdapter(goToAlbumFragment, goToPostFragment)

        _binding?.swipeRefreshLayout?.setOnRefreshListener {
            viewModel.startSearch()
        }

        return binding.root
    }

    companion object {
        private const val TAG = "ProfileFragment"
    }
}



