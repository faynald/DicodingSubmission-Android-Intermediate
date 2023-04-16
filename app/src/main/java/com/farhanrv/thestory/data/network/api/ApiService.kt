package com.farhanrv.thestory.data.network.api

import com.farhanrv.thestory.data.network.request.LoginRequest
import com.farhanrv.thestory.data.network.request.SignupRequest
import com.farhanrv.thestory.data.network.response.FileUploadResponse
import com.farhanrv.thestory.data.network.response.LoginResponse
import com.farhanrv.thestory.data.network.response.SignupResponse
import com.farhanrv.thestory.data.network.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("login")
    fun auth(@Body auth: LoginRequest): Call<LoginResponse>

    @POST("register")
    fun register(@Body auth: SignupRequest): Call<SignupResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ): StoryResponse

    @Multipart
    @POST("stories")
    fun addNewStory(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part,
    ): Call<FileUploadResponse>
}