package com.example.common.data.remote.util

import com.google.gson.annotations.SerializedName

data class BaseErrorResponse(
    val code: Int,
    val msg: String
)
