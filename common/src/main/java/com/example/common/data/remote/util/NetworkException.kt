package com.example.common.data.remote.util

class NoNetworkException(message: String?) : Exception()
class NetworkAuthenticationException(message: String?) : Exception()
class NetworkServerException(message: String?) : Exception()
class NetworkResourceNotFoundException(message: String?) : Exception()
class RequestTimeoutException(message: String?) : Exception()
class BadRequestException(message: String?) : Exception()
class UnknownException(message: String?) : Exception()
class NetworkException(message: String?) : Exception()