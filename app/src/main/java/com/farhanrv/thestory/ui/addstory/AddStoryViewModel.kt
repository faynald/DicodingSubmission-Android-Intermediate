package com.farhanrv.thestory.ui.addstory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.farhanrv.thestory.data.AppRepository
import com.farhanrv.thestory.data.network.response.FileUploadResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryViewModel(private val repository: AppRepository) : ViewModel() {
    fun addNewStory(
        token: String,
        description: String,
        file: File,
        result: (FileUploadResponse?) -> Unit
    ) {
        val requestBodyDescription =
            description.toRequestBody("text/plain".toMediaType())
        val requestBodyImage = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestBodyImage
        )
        repository.addNewStory(
            token,
            requestBodyDescription,
            imageMultipart
        ) {
            result(it)
        }
    }

    fun getToken() = repository.getToken().asLiveData()
}