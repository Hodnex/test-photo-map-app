package com.example.testbalinasoft.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.testbalinasoft.data.Comment
import com.example.testbalinasoft.data.Image
import com.example.testbalinasoft.di.ApplicationScope
import com.example.testbalinasoft.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteDialogViewModel @Inject constructor(
    private val state: SavedStateHandle,
    @ApplicationScope private val applicationScope: CoroutineScope,
    private val repository: DataRepository
) : ViewModel() {

    private val image = state.get<Image>("image")
    private val comment = state.get<Comment>("comment")

    fun onConfirmClick() {
        if (image != null) {
            deleteImage(image)
        } else if (comment != null) {
            deleteComment(comment)
        }
    }

    private fun deleteImage(image: Image) = applicationScope.launch() {
        val status = repository.deleteImage(image)

        Log.d("Main", status.toString())
    }

    private fun deleteComment(comment: Comment) = applicationScope.launch() {
        val status = repository.deleteComment(comment)

        Log.d("Main", status.toString())
    }
}