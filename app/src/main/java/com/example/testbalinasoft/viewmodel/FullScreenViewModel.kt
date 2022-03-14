package com.example.testbalinasoft.viewmodel

import androidx.lifecycle.SavedStateHandle
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
class FullScreenViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val myDao: MyDao,
    private val repository: DataRepository
) : ViewModel() {
    val image = state.get<Image>("image")!!

    val comments = myDao.getComments(image.id).asLiveData()

    private val fullScreenChannel = Channel<FullScreenEvent>()
    val fullScreenEvent = fullScreenChannel.receiveAsFlow()

    fun getComments() = viewModelScope.launch {
        val status = repository.getComments(image.id)

        if (status != 200) showErrorMessage("Network error")
    }

    fun postComment(text: String) = viewModelScope.launch {
        val status = repository.postComment(image.id, text)

        if (status != 200) showErrorMessage("Network error")
    }

    fun showErrorMessage(message: String) = viewModelScope.launch {
        fullScreenChannel.send(FullScreenEvent.ShowErrorMessage(message))
    }

    fun deleteComment(comment: Comment) = viewModelScope.launch {
        fullScreenChannel.send(FullScreenEvent.NavigateToDeleteDialog(comment))
    }

    sealed class FullScreenEvent() {
        data class NavigateToDeleteDialog(val comment: Comment) : FullScreenEvent()
        data class ShowErrorMessage(val message: String) : FullScreenEvent()
    }
}