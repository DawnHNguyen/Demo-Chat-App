package com.example.authentication.data.repository

import com.example.authentication.data.remote.service.AuthenticationDataSource
import com.example.authentication.domain.repository.AuthenticationRepository
import com.example.common.data.local.HawkDataSource
import com.example.common.data.remote.util.Resource
import com.example.common.data.remote.util.mapToResourceError
import com.example.common.data.remote.util.mapToResourceSuccess
import com.example.core.data.remote.dto.LoginRequest
import com.example.core.data.remote.dto.response.LoginResponse
import io.reactivex.Single
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(private val dataSource: AuthenticationDataSource) :
    AuthenticationRepository {
    override fun login(
        username: String,
        password: String
    ): Single<Resource<LoginResponse>> {
        val loginRequest = LoginRequest(2, "asdadadsadaa", password, username)
        return dataSource.login(loginRequest).map {
            if (it.code == 200) {
                HawkDataSource.saveAccessToken(it.data.accessToken)
                it.mapToResourceSuccess()
            } else it.mapToResourceError()
        }
    }
}
