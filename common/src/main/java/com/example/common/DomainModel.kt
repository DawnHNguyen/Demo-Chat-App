package com.example.common

import com.example.common.dto.LocalDto
import com.example.common.dto.RemoteDto

interface DomainModel {
    fun toLocalDto(): LocalDto
    fun toRemoteDto(): RemoteDto
}