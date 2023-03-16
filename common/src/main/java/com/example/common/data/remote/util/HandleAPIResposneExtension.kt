package com.example.common.data.remote.util

fun <T> T.mapToResourceSuccess(): Resource<T> {
    return Resource.success(this)
}

fun <T> T.mapToResourceError(): Resource<T> {
    return Resource.error(Exception(), this)
}