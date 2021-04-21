package com.example.reddittop.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.reddittop.usecase.GetAccessTokenUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application){

    fun getInfo(accessTokenUseCase: GetAccessTokenUseCase) {

        val context: Context = getApplication()
        val sharedPref = context.getSharedPreferences("com.example.reddittop.MY_PREF", Context.MODE_PRIVATE)

        viewModelScope.launch(Dispatchers.IO) {
            val tokenSaved = sharedPref.getString("TOKEN_VALID", "")
            if (tokenSaved != null) {
                if (tokenSaved.isEmpty()){
                    Log.d("OVM", "TOKEN NO SAVED YET.")
                    getRemoteTokenRequest(accessTokenUseCase)
                }else{
                    Log.d("OVM", "TOKEN FROM PREF -> $tokenSaved")
                    getListItems()
                }
            }
        }
    }

    private suspend fun getRemoteTokenRequest(accessTokenUseCase: GetAccessTokenUseCase){
        accessTokenUseCase.getToken().collect {
            val context: Context = getApplication()
            val sharedPref = context.getSharedPreferences("com.example.reddittop.MY_PREF", Context.MODE_PRIVATE)
            with(sharedPref.edit()){
                putString("TOKEN_VALID", it)
                apply()
            }
            getListItems()
        }
    }

    private suspend fun getListItems(){
    }

}