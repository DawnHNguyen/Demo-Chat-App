package com.example.authentication.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.authentication.data.remote.service.AuthenticationDataSource
import com.example.authentication.domain.usecase.LoginUseCase
import com.example.common.data.local.HawkDataSource
import com.example.common.data.remote.util.Resource
import com.example.common.data.remote.util.UnknownException
import com.example.common.data.remote.util.mapToResourceError
import com.example.common.data.remote.util.mapToResourceSuccess
import com.example.core.data.remote.dto.LoginRequest
import com.example.core.data.remote.dto.response.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val dataSource: AuthenticationDataSource
) : ViewModel() {

    private val _loginStateFlow = MutableLiveData<Resource<LoginResponse>>(Resource.loading())
    val loginStateFlow: LiveData<Resource<LoginResponse>> get() = _loginStateFlow

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun login() {
//        _loginStateFlow.value = loginUseCase("demo1@example.com", "Abcd!234", compositeDisposable)
        val loginRequest = LoginRequest(2, "asdadadsadaa", "Abcd!234", "demo1@example.com")
        compositeDisposable.add(
            dataSource.login(2, "2.0.0", loginRequest)
                .delay(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    _loginStateFlow.value = if (response.code == 200) {
                        HawkDataSource.saveAccessToken(response.data.accessToken)
                        response.mapToResourceSuccess()
                    } else response.mapToResourceError()
                },
                    { t ->
                        Log.e("LoginResponse", t.toString())
                    })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}