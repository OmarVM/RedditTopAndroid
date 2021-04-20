package com.example.reddittop.di

import com.example.reddittop.data.network.ConstantsServer
import com.example.reddittop.data.network.token.BasicAuthInterceptor
import com.example.reddittop.data.network.token.RequestTokenService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    fun getRetrofitInstanceToken(okHttpClient: OkHttpClient): RequestTokenService {
        return Retrofit.Builder()
            .baseUrl(ConstantsServer.URL_BASE)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RequestTokenService::class.java)
    }

    @Provides
    fun getInterceptor(): BasicAuthInterceptor {
        return BasicAuthInterceptor(ConstantsServer.CLIENT_ID, ConstantsServer.CLIENT_SECRET)
    }

    @Provides
    fun getOkHttpClient(interceptor: BasicAuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }
}