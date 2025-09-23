package com.example.scmptechnicaltest.login

import com.example.scmptechnicaltest.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.scmptechnicaltest.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        // Todo: observeViewModel()
    }

    private fun setupUI() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val bundle = Bundle().apply { putString("token", "test") }
            findNavController().navigate(R.id.staffDirectoryFragment, bundle)
        }

        binding.btnErrorOk.setOnClickListener {
            binding.tvError.visibility = View.GONE
            binding.btnErrorOk.visibility = View.GONE
        }
    }
}