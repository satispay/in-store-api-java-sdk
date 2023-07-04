package com.satispay.protocore.utility

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce

private const val NETWORK_ISSUE_THRESHOLD: Long = 1000


val hasNetworkIssue = MutableStateFlow<NetworkResponse>(NetworkResponse.OnNetworkResponse)
val showNetworkIssue = MutableStateFlow(false).also { init() }

private fun init() {
    CoroutineScope(Dispatchers.Main.immediate).launch {
        hasNetworkIssue
            .debounce(NETWORK_ISSUE_THRESHOLD)
            .collect {
                showNetworkIssue.value = it != NetworkResponse.OnNetworkResponse
            }
    }
}

fun hideNetworkIssue() = CoroutineScope(Dispatchers.Main.immediate).launch {
    showNetworkIssue.value = false
}

sealed class NetworkResponse {
    object BadNetworkResponse: NetworkResponse()
    object LowNetworkResponse: NetworkResponse()
    object OnNetworkResponse: NetworkResponse()
}
