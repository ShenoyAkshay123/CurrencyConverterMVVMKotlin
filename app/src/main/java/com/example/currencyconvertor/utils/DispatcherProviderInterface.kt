package com.example.currencyconvertor.utils

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProviderInterface {

    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}