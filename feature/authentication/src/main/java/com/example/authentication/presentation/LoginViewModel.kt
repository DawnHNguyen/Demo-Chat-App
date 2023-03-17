package com.example.authentication.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.authentication.domain.usecase.LoginUseCase
import com.example.common.data.remote.util.Resource
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
) : ViewModel() {

    private val _loginStateFlow = MutableLiveData<Resource<LoginResponse>>(Resource.loading())
    val loginStateFlow: LiveData<Resource<LoginResponse>> get() = _loginStateFlow

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun login() {
        compositeDisposable.add(
            loginUseCase("demo1@example.com", "Abcd!234")
                .delay(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    _loginStateFlow.value = response
                },
                    { t ->
                        Log.e("LoginResponse", t.toString())
                        _loginStateFlow.value = Resource.error(Exception(t.message))
                    })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}