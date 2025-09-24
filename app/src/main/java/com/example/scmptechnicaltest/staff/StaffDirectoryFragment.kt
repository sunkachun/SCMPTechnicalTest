package com.example.scmptechnicaltest.staff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scmptechnicaltest.R
import com.example.scmptechnicaltest.databinding.FragmentStaffDirectoryBinding
import com.example.presentation.staff.StaffViewModel
import com.example.scmptechnicaltest.staff.adapter.StaffAdapter
import kotlin.getValue

class StaffDirectoryFragment : Fragment() {

    private lateinit var binding: FragmentStaffDirectoryBinding
    private lateinit var adapter: StaffAdapter
    private val viewModel: StaffViewModel by activityViewModels { StaffViewModelFactory.create() }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStaffDirectoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = arguments?.getString("token") ?: return // Handle null

        binding.tvToken.text = getString(R.string.staff_directory_token, token)

        adapter = StaffAdapter()
        binding.rvStaff.layoutManager = LinearLayoutManager(context)
        binding.rvStaff.adapter = adapter
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnLoadMore.setOnClickListener {
            viewModel.loadMore()
        }

        setupObservers()

        viewModel.loadInitial()
    }

    private fun setupObservers() {
        viewModel.staffList.observe(viewLifecycleOwner) { staffList ->
            adapter.submitList(staffList)
        }

        viewModel.hasMore.observe(viewLifecycleOwner) { hasMore ->
            binding.btnLoadMore.visibility = if (hasMore == true) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                viewModel.resetError()
            }
        }
    }
}