package com.example.testbalinasoft.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testbalinasoft.data.LoginRequest
import com.example.testbalinasoft.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private val loginChannel = Channel<LoginEvent>()
    val loginEvent = loginChannel.receiveAsFlow()

    private fun loginUser(login: String, password: String, isRegistered: Boolean) =
        viewModelScope.launch {
            val loginRequest = LoginRequest(login.lowercase(), password)
            val status = repository.postLogin(loginRequest, isRegistered)
            if (status == 200) {
                navigateToPhotos()
            } else if (status == 400 && !isRegistered) {
                showErrorMessage("Login already use")
            } else if (status == 400 && isRegistered) {
                showErrorMessage("Login or password incorrect")
            } else {
                showErrorMessage("Login error")
            }
        }

    fun navigateToPhotos() = viewModelScope.launch {
        loginChannel.send(LoginEvent.NavigateToPhotoScreen)
    }

    fun loginCheck(login: String, password: String) {
        if (login.isEmpty() || password.isEmpty()) {
            showErrorMessage("Fill all fields!")
        } else if (login.length < 4 || login.length > 32) {
            showErrorMessage("Login size must be between 4 and 32")
        } else if (login.contains(" ") ||
            login.contains(",") ||
            login.contains("?") ||
            login.contains("!")
        ) {
            showErrorMessage("Login must match \"[a-z0-9_\\-.@]+\"")
        } else if (password.length < 8 || password.length > 500) {
            showErrorMessage("Password size must be between 8 and 500")
        } else {
            loginUser(login, password, true)
        }
    }

    fun signInCheck(login: String, password: String, confirmPassword: String) {
        if (login.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showErrorMessage("Fill all fields!")
        } else if (password != confirmPassword) {
            showErrorMessage("Passwords do not match")
        } else if (login.length < 4 || login.length > 32) {
            showErrorMessage("Login size must be between 4 and 32")
        } else if (login.contains(" ") ||
            login.contains(",") ||
            login.contains("?") ||
            login.contains("!")
        ) {
            showErrorMessage("Login must match \"[a-z0-9_\\-.@]+\"")
        } else if (password.length < 8 || password.length > 500) {
            showErrorMessage("Password size must be between 8 and 500")
        } else {
            loginUser(login, password, false)
        }
    }

    private fun showErrorMessage(message: String) = viewModelScope.launch {
        loginChannel.send(LoginEvent.ShowErrorMessage(message))
    }

    sealed class LoginEvent {
        object NavigateToPhotoScreen : LoginEvent()
        data class ShowErrorMessage(val message: String) : LoginEvent()
    }
}