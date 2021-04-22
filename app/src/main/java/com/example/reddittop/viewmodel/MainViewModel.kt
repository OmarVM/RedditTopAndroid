package com.example.reddittop.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.reddittop.data.model.listitems.ChildrenRequest
import com.example.reddittop.usecase.GetAccessTokenUseCase
import com.example.reddittop.usecase.GetListItemsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application){

    private val _mListTop = MutableLiveData<List<ChildrenRequest>>()
    val mListTop: LiveData<List<ChildrenRequest>> = _mListTop
    private var dataAvailable = false

    private var mAccessTokenUC: GetAccessTokenUseCase? = null
    private var mGetListItemsUC: GetListItemsUseCase? = null

    fun setDependencies(accessTokenUseCase: GetAccessTokenUseCase, getListItemsUseCase: GetListItemsUseCase){
        mAccessTokenUC = accessTokenUseCase
        mGetListItemsUC = getListItemsUseCase
    }

    fun getInfo() {
        val context: Context = getApplication()
        val sharedPref = context.getSharedPreferences("com.example.reddittop.MY_PREF", Context.MODE_PRIVATE)

        viewModelScope.launch(Dispatchers.IO) {
            val tokenSaved = sharedPref.getString("TOKEN_VALID", "")
            if (tokenSaved != null) {
                if (tokenSaved.isEmpty()){
                    Log.d("OVM", "TOKEN NO SAVED YET.")
                    getRemoteTokenRequest()
                }else{
                    Log.d("OVM", "TOKEN FROM PREF -> $tokenSaved")
                    getListItems(tokenSaved)
                }
            }
        }
    }

    private suspend fun getRemoteTokenRequest(){
        mAccessTokenUC?.getToken()?.collect {
            val context: Context = getApplication()
            val sharedPref = context.getSharedPreferences("com.example.reddittop.MY_PREF", Context.MODE_PRIVATE)
            with(sharedPref.edit()){
                putString("TOKEN_VALID", it)
                apply()
            }
            getListItems( it)
        }
    }

    private fun getListItems(token: String) {
        if (!dataAvailable){
            viewModelScope.launch {
                mGetListItemsUC?.getListItems(token)?.collect {
                    if (it.isNotEmpty()) {
                        _mListTop.postValue(it)
                        dataAvailable = true
                    }
                }
            }
        }
    }

    fun updateLiveDataList(mList : List<ChildrenRequest>){
        _mListTop.postValue(mList)
    }
}