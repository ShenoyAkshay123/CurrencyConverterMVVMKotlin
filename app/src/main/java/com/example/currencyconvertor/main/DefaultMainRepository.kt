package com.example.currencyconvertor.main

import com.example.currencyconvertor.data.models.CurrencyApi
import com.example.currencyconvertor.data.models.CurrencyResponse
import com.example.currencyconvertor.utils.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api: CurrencyApi
) : MainRepository {
    override suspend fun getRates(base: String): Resource<CurrencyResponse> {
         return try {
            val response = api.getRates(base)
            val result = response.body()
            if(response.isSuccessful && result!=null){
                    Resource.Success(result)
                }else{
                    Resource.Error("Sorry, the Api gave empty response")
                }

        } catch (e: Exception){
            Resource.Error("Error")
        }
    }


}