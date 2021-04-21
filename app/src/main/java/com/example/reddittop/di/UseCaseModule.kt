package com.example.reddittop.di

import com.example.reddittop.data.network.token.RequestTokenService
import com.example.reddittop.usecase.GetAccessTokenUseCase
import com.example.reddittop.usecase.GetListItemsUseCase
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

@Module
class UseCaseModule {

    @Provides
    fun getToken(serviceRemote: RequestTokenService): GetAccessTokenUseCase {
        return GetAccessTokenUseCase(serviceRemote)
    }

    @Provides
    fun provideListItemUseCase(@Named("ITEMS_LIST_RETRO") retrofitBuilder: Retrofit.Builder) : GetListItemsUseCase{
        return GetListItemsUseCase(retrofitBuilder)
    }

}