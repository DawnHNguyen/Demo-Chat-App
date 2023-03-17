package com.example.authentication.data.remote.service

import com.example.core.data.remote.dto.LoginRequest
import com.example.core.data.remote.dto.response.LoginResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationService {
    @POST("user/login")
    fun login(@Body loginRequest: LoginRequest): Single<LoginResponse>
}