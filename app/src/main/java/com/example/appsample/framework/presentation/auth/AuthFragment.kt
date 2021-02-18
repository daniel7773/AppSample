package com.example.appsample.framework.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.appsample.R
import com.example.appsample.business.domain.state.DataState.Error
import com.example.appsample.business.domain.state.DataState.Idle
import com.example.appsample.business.domain.state.DataState.Loading
import com.example.appsample.business.domain.state.DataState.Success
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelData()
    }

    private fun observeViewModelData() {
        viewModel.authState.observe(viewLifecycleOwner) {
            when (it) {
                is Idle -> {
                    if (binding.progressBar.visibility != View.GONE) binding.progressBar.visibility = View.GONE
                }
                is Loading -> {
                    if (binding.progressBar.visibility != View.GONE) binding.progressBar.visibility = View.GONE
                }
                is Error -> {
                    if (binding.progressBar.visibility != View.GONE) binding.progressBar.visibility = View.GONE
                }
                is Success -> {
                    if (binding.progressBar.visibility != View.GONE) binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun goToRegistration() =
        findNavController().navigate(R.id.action_authFragment_to_registrationFragment)
}