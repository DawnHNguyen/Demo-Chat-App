package com.example.common.data.remote.services

import com.example.common.data.remote.dto.RefreshTokenRequest
import javax.inject.Inject

class CommonDataSource @Inject constructor(private val service: CommonService) {

    suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest) = service.refreshToken(refreshTokenRequest)
}