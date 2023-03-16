package com.example.core.data.remote.dto.response


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: Data,
    @SerializedName("msg")
    val msg: String
)