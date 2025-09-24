package com.example.scmptechnicaltest.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.presentation.login.LoginUiState
import com.example.presentation.login.LoginViewModel
import com.example.scmptechnicaltest.R
import com.example.scmptechnicaltest.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by activityViewModels { LoginViewModelFactory.create() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupObservers()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is LoginUiState.Idle -> {
                            binding.progressLoading.visibility = View.GONE
                            binding.btnLogin.isEnabled = true
                            binding.tvError.visibility = View.GONE
                            binding.btnErrorOk.visibility = View.GONE
                        }
                        is LoginUiState.Loading -> {
                            binding.progressLoading.visibility = View.VISIBLE
                            binding.btnLogin.isEnabled = false
                        }
                        is LoginUiState.Success -> {
                            binding.progressLoading.visibility = View.GONE
                            binding.btnLogin.isEnabled = true
                            val bundle = Bundle().apply { putString("token", state.token) }
                            findNavController().navigate(R.id.staffDirectoryFragment, bundle)
                            viewModel.resetState()
                        }
                        is LoginUiState.Error -> {
                            binding.progressLoading.visibility = View.GONE
                            binding.btnLogin.isEnabled = true
                            binding.tvError.text = state.message
                            binding.tvError.visibility = View.VISIBLE
                            binding.btnErrorOk.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun setupUI() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.login(email, password)
        }

        binding.btnErrorOk.setOnClickListener {
            binding.tvError.visibility = View.GONE
            binding.btnErrorOk.visibility = View.GONE
            viewModel.resetState()
        }
    }
}