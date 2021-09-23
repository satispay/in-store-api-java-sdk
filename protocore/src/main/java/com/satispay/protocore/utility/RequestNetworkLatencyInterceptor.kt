package com.satispay.protocore.utility

import com.satispay.protocore.log.ProtoLogger
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.HttpException
import java.net.UnknownHostException

private const val MAX_LATENCY = 7500

class RequestNetworkLatencyInterceptor: Interceptor{

    private var networkLatency = 0L

    @ExperimentalCoroutinesApi
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val start = System.currentTimeMillis()
        val response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            when (e) {
                is UnknownHostException,
                is HttpException -> {
                    ProtoLogger.info("NetworkStatus: [-] ${e.javaClass.simpleName}! ${e.message}")
                    GlobalScope.launch(Dispatchers.Main.immediate) {
                        hasNetworkIssue.value = NetworkResponse.BadNetworkResponse
                    }
                }
            }
            throw e
        }
        val delay = System.currentTimeMillis() - start
        networkLatency = (networkLatency * 2 + delay) / 3
        (networkLatency > MAX_LATENCY).also { hasIssue ->
            if (hasIssue) {
                ProtoLogger.info("NetworkStatus: [-] Latency ${networkLatency}ms")
                CoroutineScope(Dispatchers.Main.immediate).launch {
                    hasNetworkIssue.value= NetworkResponse.LowNetworkResponse
                }
            } else {
                ProtoLogger.info("NetworkStatus: [+] Latency ${networkLatency}ms")
                CoroutineScope(Dispatchers.Main.immediate).launch {
                    hasNetworkIssue.value = NetworkResponse.OnNetworkResponse
                    showNetworkIssue.value = false
                }
            }
        }
        return response
    }
}