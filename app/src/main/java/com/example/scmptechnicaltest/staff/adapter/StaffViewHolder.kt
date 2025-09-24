package com.example.scmptechnicaltest.staff.adapter

import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.staff.model.StaffDisplayModel
import com.example.scmptechnicaltest.R
import com.example.scmptechnicaltest.databinding.ItemStaffBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL


class StaffViewHolder(private val binding: ItemStaffBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(staff: StaffDisplayModel) {
        binding.tvName.text = staff.name
        binding.tvEmail.text = staff.email
        binding.ivAvatar.setBackgroundResource(R.drawable.circle_icon_background)
        val image = staff.avatar

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL(image)
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 5000 // 5 seconds timeout
                connection.readTimeout = 5000
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(input)
                connection.disconnect()

                launch(Dispatchers.Main) {
                    binding.ivAvatar.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {

            }
        }
    }
}