package com.example.authentication.domain.usecase

import com.example.authentication.domain.repository.AuthenticationRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: AuthenticationRepository) {
    operator fun invoke(username: String, password: String) = repository.login(username, password)
}