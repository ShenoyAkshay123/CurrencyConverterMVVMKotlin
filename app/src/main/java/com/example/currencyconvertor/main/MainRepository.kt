package com.example.currencyconvertor.main

import com.example.currencyconvertor.data.models.CurrencyResponse
import com.example.currencyconvertor.utils.Resource

interface MainRepository {

    suspend fun getRates(base: String):Resource<CurrencyResponse>
}