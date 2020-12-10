package com.example.appsample.framework.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.appsample.R
import com.example.appsample.databinding.FragmentAuthBinding
import com.example.appsample.framework.base.presentation.BaseFragment
import com.example.appsample.framework.presentation.auth.di.factories.viewmodels.AuthViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@FlowPreview
class AuthFragment @Inject
constructor(
    private val viewModelFactory: AuthViewModelFactory
) : BaseFragment(R.layout.fragment_auth) {

    private var _binding: FragmentAuthBinding? = null
    private val binding: FragmentAuthBinding get() = _binding!!


    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AuthViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.register.setOnClickListener { goToRegistration() }
            it.lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    private fun goToRegistration() =
        findNavController().navigate(R.id.action_authFragment_to_registrationFragment)
}