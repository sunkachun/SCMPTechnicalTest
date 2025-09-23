package com.example.scmptechnicaltest.staff.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.staff.model.StaffDisplayModel
import com.example.scmptechnicaltest.R
import com.example.scmptechnicaltest.databinding.ItemStaffBinding

class StaffAdapter : RecyclerView.Adapter<StaffViewHolder>() {

    private val staffList = mutableListOf<StaffDisplayModel>()

    fun submitList(newList: List<StaffDisplayModel>) {
        staffList.clear()
        staffList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffViewHolder {
        val binding = ItemStaffBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StaffViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StaffViewHolder, position: Int) {
        holder.bind(staffList[position])
    }

    override fun getItemCount(): Int = staffList.size
}