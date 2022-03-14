package com.example.testbalinasoft.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.testbalinasoft.data.*
import com.example.testbalinasoft.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val myDao: MyDao,
    private val repository: DataRepository
) : ViewModel() {

    private val photosChannel = Channel<PhotosEvent>()
    val photosEvent = photosChannel.receiveAsFlow()

    val images = myDao.getImages().asLiveData()


    fun downloadImages() = viewModelScope.launch {
        val status = repository.getImages()

        if (status != 200) showErrorMessage("Network error")
    }

    fun showErrorMessage(message: String) = viewModelScope.launch {
        photosChannel.send(PhotosEvent.ShowErrorMessage(message))
    }

    fun onItemClick(image: Image) = viewModelScope.launch {
        photosChannel.send(PhotosEvent.NavigateToFullScreen(image))
    }

    fun deleteImage(image: Image) = viewModelScope.launch {
        photosChannel.send(PhotosEvent.NavigateToDeleteDialog(image))
    }

    sealed class PhotosEvent() {
        data class NavigateToFullScreen(val image: Image) : PhotosEvent()
        data class NavigateToDeleteDialog(val image: Image) : PhotosEvent()
        data class ShowErrorMessage(val message: String) : PhotosEvent()
    }
}