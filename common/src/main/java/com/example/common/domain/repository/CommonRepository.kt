package com.example.common.domain.repository

import com.example.common.data.remote.util.Resource

interface CommonRepository {
    suspend fun refreshToken(): Resource<String>
}