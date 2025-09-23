package com.example.scmptechnicaltest.staff.adapter


import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.staff.model.StaffDisplayModel
import com.example.scmptechnicaltest.databinding.ItemStaffBinding

class StaffViewHolder(private val binding: ItemStaffBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(staff: StaffDisplayModel) {
        binding.tvName.text = staff.name
        // Avatar: Set background or load if URL (native BitmapFactory if needed, but placeholder here)
        binding.ivAvatar.setBackgroundColor(0xFF000000.toInt()) // Black circle
    }
}