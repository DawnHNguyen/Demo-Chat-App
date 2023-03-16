package com.example.authentication.data.remote.service

import com.example.core.data.remote.dto.LoginRequest
import com.example.core.data.remote.dto.response.LoginResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthenticationService {
    @POST("user/login")
    fun login(@Header("device") device: Int, @Header("version") version: String, @Body loginRequest: LoginRequest): Observable<LoginResponse>
}