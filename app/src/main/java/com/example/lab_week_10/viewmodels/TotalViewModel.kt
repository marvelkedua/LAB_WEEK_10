package com.example.lab_week_10.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TotalViewModel: ViewModel() {
    // Deklarasi LiveData object
    private val _total = MutableLiveData<Int>()
    val total: LiveData<Int> = _total

    init {
        // Inisialisasi nilai awal 0
        _total.postValue(0)
    }

    fun incrementTotal() {
        // Update value menggunakan postValue
        _total.postValue(_total.value?.plus(1))
    }
    fun setTotal(newTotal: Int) {
        _total.postValue(newTotal)
    }
}