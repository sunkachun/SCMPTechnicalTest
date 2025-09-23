package com.example.scmptechnicaltest.staff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scmptechnicaltest.R
import com.example.scmptechnicaltest.databinding.FragmentStaffDirectoryBinding
import com.example.scmptechnicaltest.staff.adapter.StaffAdapter

class StaffDirectoryFragment : Fragment() {

    private lateinit var binding: FragmentStaffDirectoryBinding
    private lateinit var adapter: StaffAdapter

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

        // Todo: Viewmodel observe + submit list


        binding.btnLoadMore.setOnClickListener {
            // Todo: load more
        }

        // Todo: Initial load
    }
}