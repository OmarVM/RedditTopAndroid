package com.example.reddittop.usecase

import android.util.Log
import com.example.reddittop.data.network.ConstantsServer
import com.example.reddittop.data.network.token.RequestTokenService
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.util.*
import javax.inject.Inject

class GetAccessTokenUseCase @Inject constructor(private val serviceRemote: RequestTokenService) {

    suspend fun getToken() =  flow {
        val userId = UUID.randomUUID().toString()
        try {
            val service = serviceRemote.requestToken(ConstantsServer.VALUE_GRANT_TYPE, userId)
            service.let {
                Log.d("OVM", "Token -> ${it.access_token}")
                val result = it.access_token ?: ""
                emit(result)
            }
        }catch (e: HttpException){
            Log.e("OVM", "Error API -> ${e.response()}")
        }
    }
}