package com.farhanrv.thestory.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farhanrv.thestory.data.AppRepository
import com.farhanrv.thestory.data.network.request.LoginRequest
import com.farhanrv.thestory.data.network.response.LoginResponse
import com.farhanrv.thestory.model.User
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AppRepository) : ViewModel() {
    fun auth(auth: LoginRequest, result: (LoginResponse?) -> Unit) = repository.auth(auth) {
        result(it)
    }

    fun savePreferences(token: String, user: User) {
        viewModelScope.launch {
            repository.saveUser(token, user)
        }
    }
}