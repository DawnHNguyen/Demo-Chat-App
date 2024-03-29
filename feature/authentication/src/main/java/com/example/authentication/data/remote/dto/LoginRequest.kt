package com.example.core.data.remote.dto


import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("device")
    val device: Int,
    @SerializedName("device_token")
    val deviceToken: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("username")
    val username: String
)