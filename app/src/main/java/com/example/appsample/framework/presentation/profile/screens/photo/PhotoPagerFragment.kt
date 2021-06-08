package com.example.appsample.framework.presentation.profile.screens.photo

import android.annotation.SuppressLint
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
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.appsample.R
import com.example.appsample.business.domain.state.DataState
import com.example.appsample.databinding.FragmentPhotoPagerBinding
import com.example.appsample.framework.app.ui.MainNavController
import com.example.appsample.framework.base.presentation.BaseFragment
import com.example.appsample.framework.presentation.profile.screens.common.SharedAlbumViewModel
import com.example.appsample.framework.presentation.profile.screens.photo.adapter.ImagePagerAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@FlowPreview
class PhotoPagerFragment @Inject constructor() : BaseFragment(R.layout.fragment_photo_pager) {

    val TAG = "PhotoPagerFragment"
    private lateinit var mainNavController: MainNavController

    private var _binding: FragmentPhotoPagerBinding? = null
    private val binding: FragmentPhotoPagerBinding get() = _binding!!
    var adapter: ImagePagerAdapter? = null

    private val sharedViewModel: SharedAlbumViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        try {
            mainNavController = context as MainNavController
        } catch (e: ClassCastException) {
            Log.e(TAG, "$context must implement MainNavController")
        }
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentPhotoPagerBinding.inflate(inflater, container, false)
        }
        setupAdapter()

        prepareSharedElementTransition()

        if (savedInstanceState == null) {
            postponeEnterTransition()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sharedViewModel.select(position)
            }
        })
    }

    private fun setupAdapter() {
        when (val data = sharedViewModel.photoList.value) {
            is DataState.Success -> {
                if (data.data != null) {
                    adapter = ImagePagerAdapter(this, data.data!!.map { it.url ?: "" }, sharedViewModel.selected.value)
                    binding.viewPager.adapter = adapter
                    binding.viewPager.setCurrentItem(sharedViewModel.selected.value!!, false)
                }
            }
            is DataState.Error -> {
            }
            is DataState.Loading -> {
            }
            is DataState.Idle -> {
            }
        }
    }

    private fun prepareSharedElementTransition() {
        val transition = TransitionInflater.from(context)
            .inflateTransition(R.transition.image_shared_element_transition)
        sharedElementEnterTransition = transition
        setEnterSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(names: List<String>, sharedElements: MutableMap<String, View>) {
                    val currentFragment: View

                    if (_binding!!.viewPager.size > sharedViewModel.selected.value!!) {
                        currentFragment = _binding!!.viewPager[sharedViewModel.selected.value!!]
                    } else {
                        currentFragment = _binding!!.viewPager[0]
                    }
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

    @SuppressLint("SetTextI18n")
    private fun subscribeLiveData() {
        sharedViewModel.selected.observe(viewLifecycleOwner) {
            binding.photoNumber.text = "${it + 1} из ${sharedViewModel.photoList.value?.data?.size}" // {it + 1} cause position starts from 0
        }
    }
}