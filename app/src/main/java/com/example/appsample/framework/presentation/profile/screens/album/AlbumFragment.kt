package com.example.appsample.framework.presentation.profile.screens.album

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appsample.R
import com.example.appsample.databinding.FragmentAlbumBinding
import com.example.appsample.framework.app.ui.MainNavController
import com.example.appsample.framework.base.presentation.BaseFragment
import com.example.appsample.framework.presentation.profile.di.factories.viewmodels.GenericSavedStateViewModelFactory
import com.example.appsample.framework.presentation.profile.di.factories.viewmodels.implementations.AlbumViewModelFactory
import com.example.appsample.framework.presentation.profile.screens.album.itemdecoration.GridMarginDecoration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@FlowPreview
class AlbumFragment @Inject
constructor(
    private val albumViewModelFactory: AlbumViewModelFactory
) : BaseFragment(R.layout.fragment_album) {
    private val TAG = AlbumFragment::class.java.simpleName

    private val args: AlbumFragmentArgs by navArgs()

    private lateinit var mainNavController: MainNavController

    private var _binding: FragmentAlbumBinding? = null
    private val binding: FragmentAlbumBinding get() = _binding!!

    private val viewModel: AlbumViewModel by viewModels {
        GenericSavedStateViewModelFactory(albumViewModelFactory, this)
    }

    val listAdapter = AlbumPhotoListAdapter({ imageView, photoModel, position ->
//                goToDetailActivity(imageView, position)
        Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
    })

    private val gridLayoutManager by lazy {
        GridLayoutManager(requireContext(), 6).apply {
            this.spanCount = 3
        }
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

        val albumId = args.albumId
        val albumTitle = args.albumTitle

        _binding = FragmentAlbumBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
        }
        _binding!!.albumTitle.text = albumTitle
        _binding!!.recyclerView.apply {
            adapter = listAdapter
            layoutManager = gridLayoutManager
            addItemDecoration(GridMarginDecoration(2))
        }
        _binding!!.backButton.setOnClickListener { closeFragment() }

        // will be called only once, since we consider postId should not be null in ViewModel after setting once
        if (viewModel.isAlbumIdNull()) {
            viewModel.setAlbumId(albumId)
            viewModel.searchPhotos()
        }
        return binding.root
    }

    private fun closeFragment() {
        mainNavController.navController().popBackStack()
    }
}