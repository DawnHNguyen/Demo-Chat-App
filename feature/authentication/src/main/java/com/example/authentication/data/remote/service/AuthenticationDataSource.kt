package com.example.authentication.data.remote.service

import com.example.core.data.remote.dto.LoginRequest
import javax.inject.Inject

class AuthenticationDataSource @Inject constructor(private val service: AuthenticationService) {
    fun login(loginRequest: LoginRequest) = service.login(loginRequest)
}