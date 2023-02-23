package com.example.common.data.repository

import com.orhanobut.hawk.Hawk
import com.example.common.constpackage.HawkKey
import com.example.common.data.remote.dto.RefreshTokenRequest
import com.example.common.data.remote.services.CommonDataSource
import com.example.common.data.remote.util.Resource
import com.example.common.domain.repository.CommonRepository
import javax.inject.Inject

class CommonRepositoryImpl @Inject constructor(private val dataSource: CommonDataSource):
    CommonRepository {

    override suspend fun refreshToken(): Resource<String> {
        val refreshToken = Hawk.get(HawkKey.REFRESH_TOKEN, "null")
        val refreshTokenRequest = RefreshTokenRequest(refreshToken)
        val refreshTokenResponse = dataSource.refreshToken(refreshTokenRequest)
        if (refreshTokenResponse.isSuccessful()) Hawk.put(HawkKey.ACCESS_TOKEN, refreshTokenResponse.data)
        return refreshTokenResponse
    }
}