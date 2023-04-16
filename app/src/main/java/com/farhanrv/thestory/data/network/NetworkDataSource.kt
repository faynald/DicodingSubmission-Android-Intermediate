package com.farhanrv.thestory.data.network

import android.util.Log
import com.farhanrv.thestory.data.network.api.ApiResponse
import com.farhanrv.thestory.data.network.api.ApiService
import com.farhanrv.thestory.data.network.request.LoginRequest
import com.farhanrv.thestory.data.network.request.SignupRequest
import com.farhanrv.thestory.data.network.response.FileUploadResponse
import com.farhanrv.thestory.data.network.response.ListStoryItemResponse
import com.farhanrv.thestory.data.network.response.LoginResponse
import com.farhanrv.thestory.data.network.response.SignupResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkDataSource(private val apiService: ApiService) {
    fun auth(auth: LoginRequest, onResult: (LoginResponse?) -> Unit) {
        val call : Call<LoginResponse> = apiService.auth(auth)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                Log.e("RemoteDataSource", "Auth Success with Message -> " + response.body().toString())
                val responseBody = response.body()
                onResult(responseBody)
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("RemoteDataSource Auth", "Error -> $t")
            }
        })
        apiService.auth(auth)
    }

    fun register(auth: SignupRequest, onResult: (SignupResponse?) -> Unit) {
        val call : Call<SignupResponse> = apiService.register(auth)
        call.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(
                call: Call<SignupResponse>,
                response: Response<SignupResponse>
            ) {
                Log.e("RemoteDataSource", "Register Success with Message -> " + response.body().toString())
                val responseBody = response.body()
                onResult(responseBody)
            }
            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Log.e("RemoteDataSource Register", "Error -> $t")
            }
        })
        apiService.register(auth)
    }

    suspend fun getAllStoriesWithMap(token: String): Flow<ApiResponse<List<ListStoryItemResponse>>> {
        return flow {
            try {
                val response = apiService.getAllStories("Bearer $token" , location = 1)
                val error = response.error
                if (!error) {
                    emit(ApiResponse.Success(response.listStory))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }



    fun addNewStory(token: String, description: RequestBody, file: MultipartBody.Part, onResult: (FileUploadResponse?) -> Unit) {
        val call : Call<FileUploadResponse> = apiService.addNewStory("Bearer $token", description, file)
        call.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                Log.e("RemoteDataSource", "AddNewStory Success with Message -> " + response.body().toString())
                val responseBody = response.body()
                onResult(responseBody)
            }
            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                Log.e("RemoteDataSource", "AddNewStory Error -> $t")
            }
        })
        apiService.addNewStory(token, description, file)
    }
}