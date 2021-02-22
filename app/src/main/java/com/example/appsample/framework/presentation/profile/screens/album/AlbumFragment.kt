package com.example.appsample.framework.presentation.profile.screens.album

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appsample.R
import com.example.appsample.business.domain.model.Photo
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


class SharedViewModel : ViewModel() {
    val selected = MutableLiveData<Int>(0)

    fun select(position: Int) {
        selected.value = position
    }
}

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
    private val sharedViewModel: SharedViewModel by activityViewModels()

    var listAdapter: AlbumPhotoListAdapter? = null

    private var gridLayoutManager: GridLayoutManager? = null

    override fun onAttach(context: Context) {
        try {
            mainNavController = context as MainNavController
        } catch (e: ClassCastException) {
            Log.e(TAG, "$context must implement MainNavController")
        }
        super.onAttach(context)
    }

    var albumId: Int = 1
    var albumTitle: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            albumId = args.albumId
            albumTitle = args.albumTitle

            listAdapter = AlbumPhotoListAdapter({ imageView, photoModel, position ->
                sharedViewModel.select(position)
                goToImageFragment(imageView, position, photoModel)
            })

            gridLayoutManager = GridLayoutManager(requireContext(), 3)

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
            exitTransition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.grid_exit_transition)
        } else { // scrolling to new position
            binding.recyclerView.post {
                val layoutManager: RecyclerView.LayoutManager? = binding.recyclerView.layoutManager
                layoutManager?.scrollToPosition(sharedViewModel.selected.value!!)
            }
        }

        return binding.root
    }

    private fun goToImageFragment(imageView: ImageView, position: Int, photo: Photo) {
        val extras = FragmentNavigatorExtras(
            imageView to requireContext().getString(R.string.photo_transition_name) + position
        )
        setExitSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(names: List<String>, sharedElements: MutableMap<String, View>) {
                    val selectedViewHolder: RecyclerView.ViewHolder = binding.recyclerView
                        .findViewHolderForAdapterPosition(sharedViewModel.selected.value!!) ?: return
                    Log.d(TAG, "sharedViewModel.selected.value!!: ${sharedViewModel.selected.value}")
                    Log.d(TAG, "names size: ${names.size}")
                    Log.d(TAG, "names.get(0) " + names[0])
                    sharedElements[names[0]] = selectedViewHolder.itemView.findViewById(R.id.ivPhoto)
                }
            })
        val action = AlbumFragmentDirections.actionAlbumFragmentToPhotoFragment(photoUrl = photo.url!!, photoId = photo.id!!, albumId = albumId)
        mainNavController.navController().navigate(action, extras)
    }

    override fun onDestroyView() {
        if (view != null) {
            val parentViewGroup = requireView().parent as ViewGroup?
            parentViewGroup?.removeAllViews() // prevents of java.lang.IllegalStateException if user goes back here before transition will be completed
        }
        super.onDestroyView()
    }

    private fun closeFragment() {
        mainNavController.navController().popBackStack()
    }
}