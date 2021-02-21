package com.example.appsample.framework.presentation.profile.screens.photo

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.app.SharedElementCallback
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.appsample.R
import com.example.appsample.business.domain.state.DataState
import com.example.appsample.databinding.FragmentPhotoPagerBinding
import com.example.appsample.framework.app.ui.MainNavController
import com.example.appsample.framework.base.presentation.BaseFragment
import com.example.appsample.framework.presentation.profile.di.factories.viewmodels.GenericSavedStateViewModelFactory
import com.example.appsample.framework.presentation.profile.di.factories.viewmodels.implementations.PhotoViewModelFactory
import com.example.appsample.framework.presentation.profile.screens.album.SharedViewModel
import com.example.appsample.framework.presentation.profile.screens.photo.adapter.ImagePagerAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class PhotoPagerFragment @Inject
constructor(
    private val photoViewModelFactory: PhotoViewModelFactory
) : BaseFragment(R.layout.fragment_photo_pager) {

    val TAG = "PhotoPagerFragment"

    private val args: PhotoPagerFragmentArgs by navArgs()
    private var _binding: FragmentPhotoPagerBinding? = null
    private val binding: FragmentPhotoPagerBinding get() = _binding!!
    private lateinit var mainNavController: MainNavController

    private val viewModel: PhotoViewModel by viewModels {
        GenericSavedStateViewModelFactory(photoViewModelFactory, this)
    }

    override fun onAttach(context: Context) {
        try {
            mainNavController = context as MainNavController
        } catch (e: ClassCastException) {
            Log.e(TAG, "$context must implement MainNavController")
        }
        super.onAttach(context)
    }


    var albumId: Int = 0
    var photoId: Int = -1
    var imageUrl = ""
    var adapter: ImagePagerAdapter? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentPhotoPagerBinding.inflate(inflater, container, false)


            imageUrl = args.photoUrl
            albumId = args.albumId
            photoId = args.photoId

            adapter = ImagePagerAdapter(this, listOf(imageUrl))

            binding.viewPager.adapter = adapter
            prepareSharedElementTransition()

            if (viewModel.isAlbumIdNull()) {
                viewModel.setAlbumId(albumId)
                viewModel.searchPhotos()
            }
        }

        if (savedInstanceState == null) {
            postponeEnterTransition()
        }
        return binding.root
    }

    private fun prepareSharedElementTransition() {
        val transition = TransitionInflater.from(context)
            .inflateTransition(R.transition.image_shared_element_transition)
        sharedElementEnterTransition = transition

        this.binding.root.layoutTransition?.setAnimateParentHierarchy(false)  // to prevent bug of animation viewPager https://stackoverflow.com/questions/59660691/java-lang-illegalstateexception-page-can-only-be-offset-by-a-positive-amount

        setEnterSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(names: List<String>, sharedElements: MutableMap<String, View>) {
                    val currentFragment: View
                    if (_binding!!.viewPager.size > sharedViewModel.selected.value!!) {
                        currentFragment = _binding!!.viewPager[sharedViewModel.selected.value!!]
                    } else {
                        currentFragment = _binding!!.viewPager[0]
                    }
                    Log.d(TAG, "names.get(0) " + names[0])
                    sharedElements[names[0]] = currentFragment.findViewById(R.id.image)
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainNavController.navController().navigateUp()
            }
        })

        subscribeLiveData()
    }

    private fun subscribeLiveData() {
        viewModel.photoList.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Idle -> {
                    Log.d(TAG, "DataState Idle")
                }
                is DataState.Loading -> {
                    Log.d(TAG, "DataState Loading")
                }
                is DataState.Success -> {
                    binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            sharedViewModel.select(position)
                        }
                    })
                    var counter = 0
                    var position = 0
                    it.data?.forEach { photo ->
                        if (photo.id == photoId) {
                            position = counter
                            return@forEach
                        }
                        counter += 1
                    }
                    adapter?.setPhotoList(it.data!!.map { it.url!! }, position)
                    binding.viewPager.setCurrentItem(position, false)
                    sharedViewModel.select(position)
                }
                is DataState.Error -> {
                    Log.d(TAG, "getting DataState.Error")
                }
            }
        }
    }
}