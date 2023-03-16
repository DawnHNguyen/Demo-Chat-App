package com.example.authentication.domain.entity

import androidx.annotation.DrawableRes

data class SSOEntity(
    @DrawableRes val icon: Int,
    val platformName: String
)