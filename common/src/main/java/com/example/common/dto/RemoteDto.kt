package com.example.common.dto

import com.example.common.DomainModel

interface RemoteDto {
    fun mapToDomainModel(): DomainModel
    fun mapToLocalDto(): LocalDto
}