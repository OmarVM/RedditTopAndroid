package com.example.reddittop.di

import com.example.reddittop.data.network.ConstantsServer
import com.example.reddittop.data.network.token.BasicAuthInterceptor
import com.example.reddittop.data.network.token.RequestTokenService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    //Token Remote

    @Provides
    fun getRetrofitInstanceToken(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory): RequestTokenService {
        return Retrofit.Builder()
            .baseUrl(ConstantsServer.URL_BASE)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
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

    //Items Remote

    @Provides
    @Named("ITEMS_LIST_RETRO")
    fun provideRetrofitInstance(gsonConverterFactory: GsonConverterFactory): Retrofit.Builder {
        return Retrofit.Builder()
                .baseUrl(ConstantsServer.URL_BASE_OAUTH)
                .addConverterFactory(gsonConverterFactory)

    }
}