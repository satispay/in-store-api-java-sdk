package com.satispay.protocore.help

import com.satispay.protocore.models.help.AlgoliaResponse
import com.satispay.protocore.utility.NetworkUtilities
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import rx.Observable
import java.lang.reflect.Type

interface SatispayAlgolia {

    @Headers(
        "$APIKEY: ${"151100c540cfb4d1d1906e18783cc208"}",
        "$APPID: ${"PFK5V6MRYY"}"
    )
    @GET("1/indexes/support/")
    fun search(
        @Query("page") page: Int? = 0,
        @Query("query") lang: String,
        @Query("filters") ref: String,
        @Query("analyticsTags") orderings: String? = "[\"mobile\"]") : Observable<AlgoliaResponse>

    companion object {

        const val APIKEY = "X-Algolia-API-Key"
        const val APPID = "X-Algolia-Application-Id"
        private const val BASE_URL = "https://PFK5V6MRYY.algolia.net/"

        private val nullOnEmptyConverterFactory = object : Converter.Factory() {
            fun converterFactory() = this
            override fun responseBodyConverter(
                type: Type,
                annotations: Array<out Annotation>,
                retrofit: Retrofit
            ) = object : Converter<ResponseBody, Any?> {
                val nextResponseBodyConverter =
                    retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)

                override fun convert(value: ResponseBody) =
                    if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
            }
        }
        private var clientBuilder: OkHttpClient.Builder = NetworkUtilities.getClientNoCert()
        private val service: SatispayAlgolia = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(nullOnEmptyConverterFactory)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(clientBuilder.build()).build()
            .create(SatispayAlgolia::class.java)

        fun searchFaq(page: Int? = 0, query: String, language: String) =
            service.search(page,query,"consumer AND lang:$language")
    }
}