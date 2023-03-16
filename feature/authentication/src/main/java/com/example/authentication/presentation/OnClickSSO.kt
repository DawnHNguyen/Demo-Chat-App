package com.example.authentication.presentation

import com.example.authentication.domain.entity.SSOPlatform

interface OnClickSSO {
    fun loginSSO(type: SSOPlatform)
}
