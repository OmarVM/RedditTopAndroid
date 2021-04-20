package com.example.reddittop.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.reddittop.BaseApplication
import com.example.reddittop.R
import com.example.reddittop.data.network.ConstantsServer
import com.example.reddittop.data.network.token.RequestTokenService
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var serviceRemote: RequestTokenService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BaseApplication.getAppComponent().inject(this)

        lifecycleScope.launch {
            val userId = UUID.randomUUID().toString()
            try {
                val service = serviceRemote.requestToken(ConstantsServer.VALUE_GRANT_TYPE, userId)
                service.let {
                    Log.d("OVM", "Token -> ${it.access_token}")
                }
            }catch (e: HttpException){
                Log.e("OVM", "Error API -> ${e.response()}")
            }
        }

    }
}