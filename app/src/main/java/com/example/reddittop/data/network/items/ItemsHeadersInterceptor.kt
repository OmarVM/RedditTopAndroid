package com.example.reddittop.data.network.items

import com.example.reddittop.data.network.ConstantsServer
import okhttp3.Interceptor
import okhttp3.Response

class ItemsHeadersInterceptor(private val token: String): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val mHeaders = request.newBuilder()
                .header("User-Agent", ConstantsServer.VALUE_HEADER_USER_AGENT)
                .header("Authorization", "bearer $token")
                .build()
        return chain.proceed(mHeaders)
    }

}