package com.farhanrv.thestory.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.farhanrv.thestory.data.AppRepository
import com.farhanrv.thestory.data.network.request.LoginRequest
import com.farhanrv.thestory.data.network.response.LoginResponse
import kotlinx.coroutines.launch

class MainViewModel(private val repository: AppRepository) : ViewModel() {
    fun auth(auth: LoginRequest, result: (LoginResponse?) -> Unit) = repository.auth(auth) {
        result(it)
    }

    fun logoutUser() {
        viewModelScope.launch {
            repository.logoutUser()
        }
    }

    fun getAllStories(token: String) =
        repository.getAllStories(token).cachedIn(viewModelScope)

    fun getToken() = repository.getToken().asLiveData()
}