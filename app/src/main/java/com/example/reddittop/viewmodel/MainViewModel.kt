package com.example.reddittop.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.reddittop.data.model.listitems.ChildrenRequest
import com.example.reddittop.usecase.GetAccessTokenUseCase
import com.example.reddittop.usecase.GetListItemsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application){

    private val _mListTop = MutableLiveData<List<ChildrenRequest>>()
    val mListTop: LiveData<List<ChildrenRequest>> = _mListTop


    fun getInfo(accessTokenUseCase: GetAccessTokenUseCase, getListItemsUseCase: GetListItemsUseCase) {

        val context: Context = getApplication()
        val sharedPref = context.getSharedPreferences("com.example.reddittop.MY_PREF", Context.MODE_PRIVATE)

        viewModelScope.launch(Dispatchers.IO) {
            val tokenSaved = sharedPref.getString("TOKEN_VALID", "")
            if (tokenSaved != null) {
                if (tokenSaved.isEmpty()){
                    Log.d("OVM", "TOKEN NO SAVED YET.")
                    getRemoteTokenRequest(accessTokenUseCase, getListItemsUseCase)
                }else{
                    Log.d("OVM", "TOKEN FROM PREF -> $tokenSaved")
                    getListItems(getListItemsUseCase, tokenSaved)
                }
            }
        }
    }

    private suspend fun getRemoteTokenRequest(accessTokenUseCase: GetAccessTokenUseCase, getListItemsUseCase: GetListItemsUseCase){
        accessTokenUseCase.getToken().collect {
            val context: Context = getApplication()
            val sharedPref = context.getSharedPreferences("com.example.reddittop.MY_PREF", Context.MODE_PRIVATE)
            with(sharedPref.edit()){
                putString("TOKEN_VALID", it)
                apply()
            }
            getListItems(getListItemsUseCase, it)
        }
    }

    private fun getListItems(getListItemsUseCase: GetListItemsUseCase, token: String){
        viewModelScope.launch {
            getListItemsUseCase.getListItems(token).collect {
                _mListTop.postValue(it)
            }
        }
    }

}