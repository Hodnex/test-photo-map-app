package com.example.testbalinasoft.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.testbalinasoft.data.MyDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val myDao: MyDao
) : ViewModel() {
    val images = myDao.getImages().asLiveData()
}