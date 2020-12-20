package com.example.appsample.framework.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.appsample.R
import com.example.appsample.databinding.FragmentProfileBinding
import com.example.appsample.framework.base.presentation.BaseFragment
import com.example.appsample.framework.presentation.auth.di.factories.viewmodels.AuthViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@FlowPreview
class ProfileFragment @Inject
constructor(
    private val viewModelFactory: AuthViewModelFactory
) : BaseFragment(R.layout.fragment_profile) {
// checking userName
    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            viewModel.startSearch()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.swipeRefreshLayout?.setOnRefreshListener {
            viewModel.startSearch()
        }
    }

    companion object {
        private const val TAG = "ProfileFragment"
    }
}



