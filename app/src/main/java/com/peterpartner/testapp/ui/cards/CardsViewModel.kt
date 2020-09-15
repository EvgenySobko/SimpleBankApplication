package com.peterpartner.testapp.ui.cards

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterpartner.testapp.api.ApiFactoryCurrency
import com.peterpartner.testapp.api.ApiFactoryUsers
import com.peterpartner.testapp.api.HoldersState
import com.peterpartner.testapp.ui.main.MainViewModel
import com.peterpartner.testapp.utils.loge
import kotlinx.coroutines.launch

class CardsViewModel : ViewModel() {

    private val userApi = ApiFactoryUsers.service
    val mainState = MutableLiveData<HoldersState>()

    fun getHolders() {
        viewModelScope.launch {
            mainState.postValue(HoldersState.InProgress)
            runCatching { userApi.getHolders() }
                .onFailure {
                    mainState.postValue(HoldersState.Error(it))
                    loge("getHolders: null, ${it.message}")
                }
                .onSuccess { result ->
                    mainState.postValue(HoldersState.Success(result))
                }
        }
    }
}