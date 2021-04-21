package com.example.reddittop.usecase

import android.util.Log
import com.example.reddittop.data.network.ConstantsServer
import com.example.reddittop.data.network.items.ItemsHeadersInterceptor
import com.example.reddittop.data.network.items.RequestListItemsService
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import javax.inject.Inject

class GetListItemsUseCase @Inject constructor(private val retrofitItemsInstance: Retrofit.Builder) {

    suspend fun getListItems(token: String) {
        val mService = setListService(token)
        try {
            val result = mService.requestListItems(ConstantsServer.VALUE_TIME_YEAR,"", "", ConstantsServer.VALUE_LIMIT)
            result.let {
                Log.d("OVM", "List -> ${it.data.children.size}")
            }
        }catch (e: HttpException){
            Log.e("OVM", "Error LIST API -> ${e.response()}")
        }

    }

    private fun setListService(token: String) : RequestListItemsService {
        val interceptorToken = ItemsHeadersInterceptor(token)
        val client = OkHttpClient.Builder().addInterceptor(interceptorToken).build()
        return retrofitItemsInstance.client(client).build().create(RequestListItemsService::class.java)
    }
}