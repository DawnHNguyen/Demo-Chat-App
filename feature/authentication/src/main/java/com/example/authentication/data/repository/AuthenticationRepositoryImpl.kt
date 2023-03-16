package com.example.authentication.data.repository

import android.util.Log
import com.example.authentication.data.remote.service.AuthenticationDataSource
import com.example.authentication.domain.repository.AuthenticationRepository
import com.example.common.data.local.HawkDataSource
import com.example.common.data.remote.util.*
import com.example.core.data.remote.dto.LoginRequest
import com.example.core.data.remote.dto.response.LoginResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(private val dataSource: AuthenticationDataSource) :
    AuthenticationRepository {
    override fun login(
        username: String,
        password: String,
        compositeDisposable: CompositeDisposable
    ): Resource<LoginResponse> {
        val loginRequest = LoginRequest(2, "asdadadsadaa", password, username)
        var loginResponse: Resource<LoginResponse> = Resource.error(UnknownException("Unknown exception"), null)
//        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            dataSource.login(2, "2.0.0", loginRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    loginResponse = if (response.code == 200) {
                        HawkDataSource.saveAccessToken(response.data.accessToken)
                        response.mapToResourceSuccess()
                    }
                    else response.mapToResourceError()
                },
                    { t ->
                        Log.e("LoginResponse", t.toString())
                    })
        )
        return loginResponse
    }
}
