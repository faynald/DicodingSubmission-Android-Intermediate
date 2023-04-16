package com.farhanrv.thestory.ui.auth.signup

import androidx.lifecycle.ViewModel
import com.farhanrv.thestory.data.AppRepository
import com.farhanrv.thestory.data.network.request.SignupRequest
import com.farhanrv.thestory.data.network.response.SignupResponse

class SignupViewModel(private val repository: AppRepository) : ViewModel() {
    fun register(auth: SignupRequest, result: (SignupResponse?) -> Unit) = repository.register(auth) {
        result(it)
    }


}