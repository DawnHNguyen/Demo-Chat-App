package com.example.authentication.domain.repository

import com.example.common.data.remote.util.Resource
import com.example.core.data.remote.dto.response.LoginResponse
import io.reactivex.disposables.CompositeDisposable

interface AuthenticationRepository {
    fun login(username: String, password: String, compositeDisposable: CompositeDisposable): Resource<LoginResponse>
}