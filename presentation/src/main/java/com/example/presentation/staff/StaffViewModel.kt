package com.example.presentation.staff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.staff.usecase.GetStaff
import com.example.presentation.staff.mapper.StaffMapper
import com.example.presentation.staff.model.StaffDisplayModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StaffViewModel(private val getStaff: GetStaff, private val mapper: StaffMapper) : ViewModel() {

    private val _staffList = MutableStateFlow<List<StaffDisplayModel>>(emptyList())
    val staffList: StateFlow<List<StaffDisplayModel>> = _staffList.asStateFlow()

    private val _hasMore = MutableStateFlow(true)
    val hasMore: StateFlow<Boolean> = _hasMore.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var currentPage = 0

    fun loadInitial() {
        currentPage = 0
        _hasMore.value = true
        _staffList.value = emptyList()
        _error.value = null
        loadData()
    }

    fun loadMore() {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val nextPage = currentPage + 1

            try {
                val response = withContext(Dispatchers.IO) {
                    getStaff.execute(nextPage)
                }

                if (response.data.isNotEmpty()) {
                    val newData = mapper.mapToStaffDisplayModel(response.data)
                    val currentList = _staffList.value
                    _staffList.value = currentList + newData
                    _hasMore.value = response.totalPages > nextPage
                    currentPage = nextPage
                    _error.value = null
                } else {
                    _error.value = "Empty List"
                    _hasMore.value = false
                }
            } catch (e: Exception) {
                _error.value = "Load Fail: ${e.message}"
            }
        }
    }

    fun resetError() {
        _error.value = null
    }
}