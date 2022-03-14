package com.example.testbalinasoft.viewmodel

import android.content.Intent
import android.graphics.Bitmap
import android.util.Base64
import androidx.lifecycle.*
import com.example.testbalinasoft.data.ImageRequest
import com.example.testbalinasoft.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private val mainChannel = Channel<MainEvent>()
    val mainEvent = mainChannel.receiveAsFlow()

    val isVisibleFab = MutableLiveData<Boolean>()

    private fun getBase64FromBitmap(bm: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun postImage(intent: Intent?, date: Int, lat: Double, lng: Double) = viewModelScope.launch {
        val image = intent?.extras?.get("data") as Bitmap
        val base64Image = getBase64FromBitmap(image)
        val imageRequest = ImageRequest(base64Image, date, lat, lng)
        val status = repository.postImage(imageRequest)

        if (status != 200) showErrorMessage("Network error")
    }

    private fun showErrorMessage(message: String) = viewModelScope.launch {
        mainChannel.send(MainEvent.ShowErrorMessage(message))
    }

    sealed class MainEvent{
        data class ShowErrorMessage(val message: String) : MainEvent()
    }
}