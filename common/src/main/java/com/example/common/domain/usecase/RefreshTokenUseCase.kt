package com.example.common.domain.usecase

import com.example.common.domain.repository.CommonRepository
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(private val repository: CommonRepository) {
    suspend operator fun invoke() = repository.refreshToken()
}