package com.example.common.data.remote.util

import com.example.common.data.local.HawkDataSource
import com.orhanobut.hawk.Hawk
import okhttp3.Interceptor
import okhttp3.Response

class HeaderAuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val jwt = HawkDataSource.getAccessToken()
        val headers = request.headers.newBuilder().add("authorization", jwt).build()
        request = request.newBuilder().headers(headers).build()
        return chain.proceed(request)
    }
}
