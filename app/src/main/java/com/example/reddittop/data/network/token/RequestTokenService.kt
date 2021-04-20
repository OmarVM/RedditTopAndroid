package com.example.reddittop.data.network.token

import com.example.reddittop.data.model.TokenResponse
import com.example.reddittop.data.network.ConstantsServer
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RequestTokenService {

    @POST("api/v1/access_token")
    suspend fun requestToken(
        @Query(ConstantsServer.KEY_GRANT_TYPE) grant_type: String,
        @Query (ConstantsServer.KEY_DEVICE_ID) deviceId: String) : TokenResponse
}