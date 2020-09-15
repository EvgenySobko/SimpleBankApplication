package com.peterpartner.testapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterpartner.testapp.api.*
import com.peterpartner.testapp.entities.Currency
import com.peterpartner.testapp.entities.Valute
import com.peterpartner.testapp.utils.loge
import kotlinx.coroutines.*

class MainViewModel : ViewModel() {

    val mainState = MutableLiveData<HoldersState>()
    val currency = MutableLiveData<Currency>()
    val currentValute = MutableLiveData<Valute>()
    var currentUserBalanceRub = 1.0
    var currencyUSD = 0.0
    var currentCard: Int? = 0

    private val userApi = ApiFactoryUsers.service
    private val currencyApi = ApiFactoryCurrency.service

    fun getHolders() {
        viewModelScope.launch {
            mainState.postValue(HoldersState.InProgress)
            runCatching { userApi.getHolders() }
                .onFailure {
                    mainState.postValue(HoldersState.Error(it))
                    loge("getHolders: null, ${it.message}")
                }
                .onSuccess { result ->
                    currentUserBalanceRub *= result.users[currentCard!!].balance
                    mainState.postValue(HoldersState.Success(result))
                }
        }
    }

    fun selectValute(valuteState: ValuteState) {
        when (valuteState) {
            ValuteState.GBP -> currentValute.postValue(currency.value?.Valute?.GBP)
            ValuteState.EUR -> currentValute.postValue(currency.value?.Valute?.EUR)
            ValuteState.RUB -> currentValute.postValue(currency.value?.Valute?.RUB)
        }
    }

    fun getCurrency() {
        viewModelScope.launch {
            runCatching { currencyApi.getCurrency() }
                .onFailure { loge("getCurrency: null, ${it.message}") }
                .onSuccess {
                    currency.postValue(it)
                    currencyUSD = it.Valute.USD.Value
                    currentUserBalanceRub *= it.Valute.USD.Value
                }
        }
    }

    enum class ValuteState {
        GBP, EUR, RUB
    }
}