package com.example.authentication.domain.usecase

import com.example.authentication.domain.repository.AuthenticationRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: AuthenticationRepository) {
    operator fun invoke(username: String, password: String, compositeDisposable: CompositeDisposable) = repository.login(username, password, compositeDisposable)
}