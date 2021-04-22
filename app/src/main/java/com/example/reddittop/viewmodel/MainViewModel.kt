package com.example.reddittop.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
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
    private var allowRequest = true
    private var paginationActive = false
    private var someRequestWorking = false

    private var mAccessTokenUC: GetAccessTokenUseCase? = null
    private var mGetListItemsUC: GetListItemsUseCase? = null

    private var afterValue: String? = null
    private var beforeValue: String? = null

    fun setDependencies(accessTokenUseCase: GetAccessTokenUseCase, getListItemsUseCase: GetListItemsUseCase){
        mAccessTokenUC = accessTokenUseCase
        mGetListItemsUC = getListItemsUseCase
    }

    fun allowRequest(value: Boolean){
        allowRequest = value
    }

    private fun getSharedPref(): SharedPreferences {
        val context: Context = getApplication()
        return context.getSharedPreferences("com.example.reddittop.MY_PREF", Context.MODE_PRIVATE)
    }

    fun getInfo(isPaginationOperation: Boolean) {
        paginationActive = isPaginationOperation

        viewModelScope.launch(Dispatchers.IO) {
            val tokenSaved = getSharedPref().getString("TOKEN_VALID", "")
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
            with(getSharedPref().edit()){
                putString("TOKEN_VALID", it)
                apply()
            }
            getListItems(it)
        }
    }

    private fun getListItems(token: String) {
        if (allowRequest && !someRequestWorking){
            someRequestWorking = true
            viewModelScope.launch {
                mGetListItemsUC?.getListItems(token, getAfterValue(), getBeforeValue())?.collect {
                    if (it.children.isNotEmpty()) {
                        afterValue = it.after
                        beforeValue = it.before
                        _mListTop.postValue(it.children)

                        allowRequest = false
                        someRequestWorking = false
                    }
                }
            }
        }else{
            Log.d("OVM", "Request blocked...")
        }
    }

    fun updateLiveDataList(mList : List<ChildrenRequest>){
        viewModelScope.launch(Dispatchers.Main) {
            _mListTop.postValue(mList)
        }
    }

    fun getPaginationStatus() = paginationActive

    fun completedPaginationOp() {
        paginationActive = false
    }

    private fun getAfterValue() = afterValue ?: ""

    private fun getBeforeValue() = beforeValue ?: ""

}