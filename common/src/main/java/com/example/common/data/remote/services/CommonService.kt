package com.example.common.data.remote.services

import com.example.common.data.remote.dto.RefreshTokenRequest
import com.example.common.data.remote.util.Resource
import retrofit2.http.Body
import retrofit2.http.POST

interface CommonService {

    @POST("auth/refresh")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Resource<String>
}