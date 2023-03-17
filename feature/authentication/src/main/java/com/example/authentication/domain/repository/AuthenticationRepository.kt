package com.example.authentication.domain.repository

import com.example.common.data.remote.util.Resource
import com.example.core.data.remote.dto.response.LoginResponse
import io.reactivex.Single

interface AuthenticationRepository {
    fun login(username: String, password: String): Single<Resource<LoginResponse>>
}