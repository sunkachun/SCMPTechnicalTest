package com.example.presentation.staff

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.staff.usecase.GetStaff
import com.example.presentation.staff.mapper.StaffMapper
import com.example.presentation.staff.model.StaffDisplayModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StaffViewModel(private val getStaff: GetStaff, private val mapper: StaffMapper) : ViewModel() {

    private val _staffList = MutableLiveData<List<StaffDisplayModel>>(emptyList())
    val staffList: LiveData<List<StaffDisplayModel>> = _staffList

    private val _hasMore = MutableLiveData(true)
    val hasMore: LiveData<Boolean> = _hasMore

    private val _currentPage = MutableLiveData(0)

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadInitial() {
        _currentPage.value = 0
        _hasMore.value = true
        _staffList.value = emptyList()
        loadData()
    }

    fun loadMore() {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val currentPage = (_currentPage.value ?: 0) + 1

            val response = withContext(Dispatchers.IO) {
                getStaff.execute(currentPage)
            }

            if (response.data.isNotEmpty()) {
                val newData = mapper.mapToStaffDisplayModel(response.data)
                val currentList = _staffList.value.orEmpty()
                _staffList.value = currentList + newData
                _hasMore.value = response.totalPages != currentPage
                _currentPage.value = currentPage
                _error.value = null
            } else {
                _error.value = "Empty List"
            }
        }
    }

    fun resetError() {
        _error.value = null
    }
}