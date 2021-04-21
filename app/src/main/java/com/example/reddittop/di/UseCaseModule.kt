package com.example.reddittop.di

import com.example.reddittop.data.network.token.RequestTokenService
import com.example.reddittop.usecase.GetAccessTokenUseCase
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    @Provides
    fun getToken(serviceRemote: RequestTokenService): GetAccessTokenUseCase {
        return GetAccessTokenUseCase(serviceRemote)
    }

}