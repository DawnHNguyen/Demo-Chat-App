package com.example.common.dto

import com.example.common.DomainModel

interface LocalDto {
    fun mapToDomainModel(): DomainModel
    fun mapToRemoteDto(): RemoteDto
}