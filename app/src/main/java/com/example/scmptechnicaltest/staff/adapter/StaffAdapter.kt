package com.example.scmptechnicaltest.staff.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.presentation.staff.model.StaffDisplayModel
import com.example.scmptechnicaltest.databinding.ItemStaffBinding

class StaffAdapter : ListAdapter<StaffDisplayModel, StaffViewHolder>(StaffDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffViewHolder {
        val binding = ItemStaffBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StaffViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StaffViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object StaffDiffCallback : DiffUtil.ItemCallback<StaffDisplayModel>() {
    override fun areItemsTheSame(oldItem: StaffDisplayModel, newItem: StaffDisplayModel): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: StaffDisplayModel, newItem: StaffDisplayModel): Boolean {
        return oldItem == newItem
    }
}
