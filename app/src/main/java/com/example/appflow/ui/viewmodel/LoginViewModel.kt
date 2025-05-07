package com.example.appflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appflow.data.AuthManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authManager: AuthManager
) : ViewModel() {

    private val _currentUser = MutableStateFlow<String?>(null)
    val currentUser: StateFlow<String?> = _currentUser

    private val _loginState = MutableStateFlow<Boolean?>(null)
    val loginState: StateFlow<Boolean?> = _loginState

    private val _registerState = MutableStateFlow<Boolean?>(null)
    val registerState: StateFlow<Boolean?> = _registerState

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        FirebaseAuth.getInstance().currentUser?.email?.let {
            _currentUser.value = it
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = authManager.login(email, password)
            if (result.isSuccess) {
                FirebaseAuth.getInstance().currentUser?.email?.let {
                    _currentUser.value = it
                }
                _loginState.value = true
                _errorMessage.value=null
            } else {
                _loginState.value = false
                _errorMessage.value = result.exceptionOrNull()?.localizedMessage
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            val result = authManager.register(email, password)
            if (result.isSuccess) {
                FirebaseAuth.getInstance().currentUser?.email?.let {
                    _currentUser.value = it
                }
                _registerState.value = true
                _errorMessage.value=null
            } else {
                _registerState.value = false
                _errorMessage.value = result.exceptionOrNull()?.localizedMessage
            }
        }
    }

    fun logout() {
        authManager.logout()
        _currentUser.value = null
        _loginState.value = false
    }

    fun isUserLoggedIn() = authManager.isUserLoggedIn()
}