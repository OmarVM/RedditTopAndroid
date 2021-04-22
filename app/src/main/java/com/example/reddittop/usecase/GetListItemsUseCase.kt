package com.example.reddittop.usecase

import android.util.Log
import com.example.reddittop.data.network.ConstantsServer
import com.example.reddittop.data.network.items.ItemsHeadersInterceptor
import com.example.reddittop.data.network.items.RequestListItemsService
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import javax.inject.Inject

class GetListItemsUseCase @Inject constructor(private val retrofitItemsInstance: Retrofit.Builder) {

    suspend fun getListItems(token: String, after: String, before: String) = flow {
        val mService = setListService(token)
        try {
            val result = mService.requestListItems(ConstantsServer.VALUE_TIME_YEAR,after, before, ConstantsServer.VALUE_LIMIT)
            result.let {
                val mListTop = it.data.children
                Log.d("OVM", "LIST API -> ${mListTop.size}")
                emit(it.data)
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