package com.example.appsample.framework.presentation.profile.screens.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appsample.R
import com.example.appsample.databinding.FragmentAlbumBinding
import com.example.appsample.framework.base.presentation.BaseFragment
import com.example.appsample.framework.presentation.auth.di.factories.viewmodels.AuthViewModelFactory
import com.example.appsample.framework.presentation.profile.screens.album.itemdecoration.GridMarginDecoration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

const val ALBUM_ID: String = "ALBUM_ID"
const val ALBUM_TITLE: String = "ALBUM_TITLE"

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@FlowPreview
class AlbumFragment @Inject
constructor(
    private val viewModelFactory: AuthViewModelFactory
) : BaseFragment(R.layout.fragment_album) {

    private var _binding: FragmentAlbumBinding? = null
    private val binding: FragmentAlbumBinding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AlbumViewModel::class.java)
    }

    val listAdapter = AlbumPhotoListAdapter({ imageView, photoModel, position ->
//                goToDetailActivity(imageView, position)
        Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
    })

    val gridLayoutManager by lazy {
        GridLayoutManager(requireContext(), 6).apply {
            this.spanCount = 3
        }
    }

    private var albumId: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        albumId = arguments?.getInt(ALBUM_ID) ?: 1
        val albumTitle = arguments?.getString(ALBUM_TITLE) ?: "AlbumTitle"

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
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.searchPhotos(albumId)
        }
        viewModel.searchPhotos(albumId)
    }
}