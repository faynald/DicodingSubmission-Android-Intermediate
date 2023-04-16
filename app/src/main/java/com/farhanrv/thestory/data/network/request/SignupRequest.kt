package com.farhanrv.thestory.data.network.request

import com.google.gson.annotations.SerializedName

data class SignupRequest(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String
)