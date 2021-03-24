package com.example.currencyconvertor.main

import android.icu.util.Currency
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconvertor.data.models.Rates
import com.example.currencyconvertor.utils.DispatcherProviderInterface
import com.example.currencyconvertor.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProviderInterface
): ViewModel() {



    sealed class CurrencyEvent(){

        class Success(val resultText: String): CurrencyEvent()
        class Failure(val errorText: String): CurrencyEvent()
        object Loading: CurrencyEvent()
        object Empty: CurrencyEvent()
    }

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion : MutableStateFlow<CurrencyEvent> = _conversion

    fun convert(
        amountStr: String,
        fromCurrency: String,
        toCurrency: String
    ){
        var fromAmount = amountStr.toFloatOrNull()
        if(fromAmount == null){
            _conversion.value = CurrencyEvent.Failure("The Amount is not Valid")
            return
        }

        viewModelScope.launch(dispatchers.io) {
            _conversion.value = CurrencyEvent.Loading
            when(val ratesResponse = repository.getRates(fromCurrency)){
                is Resource.Error -> _conversion.value = CurrencyEvent.Failure(ratesResponse.message!!)

                is Resource.Success -> {val rates = ratesResponse.data!!.rates
                    val rate = getRateForCurrency(toCurrency, rates)
                    if(rate == null){
                        _conversion.value = CurrencyEvent.Failure("Unexpected Error")
                    }else{
                        val convertedRate = round((fromAmount * rate * 100)/100)
                        _conversion.value = CurrencyEvent.Success("$fromAmount $fromCurrency = $convertedRate $toCurrency")
                    }


                }
            }
        }
    }

    private fun getRateForCurrency(currency: String, rates: Rates) = when (currency) {
        "CAD" -> rates.CAD
        "HKD" -> rates.HKD
        "ISK" -> rates.ISK
        "PHP" -> rates.PHP
        "DKK" -> rates.DKK
        "HUF" -> rates.HUF
        "CZK" -> rates.CZK
        "AUD" -> rates.AUD
        "RON" -> rates.RON
        "SEK" -> rates.SEK
        "IDR" -> rates.IDR
        "INR" -> rates.INR
        "BRL" -> rates.BRL
        "RUB" -> rates.RUB
        "HRK" -> rates.HRK
        "JPY" -> rates.JPY
        "THB" -> rates.THB
        "CHF" -> rates.CHF
        "SGD" -> rates.SGD
        "PLN" -> rates.PLN
        "BGN" -> rates.BGN
        "CNY" -> rates.CNY
        "NOK" -> rates.NOK
        "NZD" -> rates.NZD
        "ZAR" -> rates.ZAR
        "USD" -> rates.USD
        "MXN" -> rates.MXN
        "ILS" -> rates.ILS
        "GBP" -> rates.GBP
        "KRW" -> rates.KRW
        "MYR" -> rates.MYR
        "EUR" -> rates.EUR

        else -> null
    }


}