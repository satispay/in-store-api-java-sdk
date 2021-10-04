package com.satispay.protocore.help

import com.google.gson.JsonObject
import com.satispay.protocore.models.help.PrismicResponse
import com.satispay.protocore.models.help.PrismicResponseCategories
import com.satispay.protocore.utility.NetworkUtilities
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import rx.Observable

import java.lang.reflect.Type

/**
 * Created on 05/05/15, 15:09
 */
interface SatispayPrismic {

    // Prismic API
    @GET("api")
    fun getMasterRefAndSupportedLanguages(): Observable<JsonObject>

    @GET("api/v2/documents/search")
    fun search(
        @Query("page") page: Int? = null,
        @Query("lang") lang: String,
        @Query("ref") ref: String,
        @Query("orderings") orderings: String? = null,
        @Query("q") q: String): Observable<PrismicResponse>

    @GET("api/v2/documents/search")
    fun searchCategories(
        @Query("page") page: Int? = null,
        @Query("lang") lang: String,
        @Query("ref") ref: String,
        @Query("orderings") orderings: String? = null,
        @Query("q") q: String): Observable<PrismicResponseCategories>

    @GET("api/v2/documents/search")
    fun searchArticles(
        @Query("page") page: Int? = null,
        @Query("lang") lang: String,
        @Query("ref") ref: String,
        @Query("orderings") orderings: String? = null,
        @Query("q") q: String): Observable<PrismicResponse>

    companion object {

        private const val TIPS = "[[any(document.tags, [\"suggested\"])]]"
        private const val ORDERCATEGORY = "[my.category.update]"
        private const val ORDERARTICLE = "[my.article.update]"
        private const val BASE_URL = "https://satispay-support.cdn.prismic.io/"

        object BusinessArticles{
            const val CASHBACK = "activating-network-cashback"
            const val REQUESTPAYMENT = "richiedere-un-pagamento"
            const val ACCEPTANCE = "come-accettare-automaticamente"
        }

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

        private fun buildCategoryRequest(idCategory : String) =
            "[[at(my.category.related, \"$idCategory\")]]"

        private fun buildArticles(idCategory : String) =
            "[[at(my.article.category, \"$idCategory\")]]"

        private fun buildRelatedArticlesUID(uids: List<String>?) =
            "[[in(my.article.uid, [${uids?.joinToString { "\"${it}\"" }}])]]"

        private var clientBuilder: OkHttpClient.Builder = NetworkUtilities.getClientNoCert()
        private val service: SatispayPrismic = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(nullOnEmptyConverterFactory)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(clientBuilder.build()).build()
            .create(SatispayPrismic::class.java)

        fun prismicInit(): Observable<JsonObject> =
            service.getMasterRefAndSupportedLanguages()

        fun getTips(page: Int? = null,language: String, ref:String?) =
            if(ref != null) service.search(page,language,ref,null, TIPS)
            else Observable.empty()

        fun getPersonalPage(page: Int? = null,language: String, ref:String?, uid:String) =
            if(ref != null) service.search(page, language, ref, null, "[[at(my.page.uid, \"$uid\")]]")
            else Observable.empty()

        fun getCategories(page: Int? = null,language: String, ref:String?, idCategory : String) =
            if(ref != null) service.searchCategories(page,language,ref, ORDERCATEGORY, buildCategoryRequest(idCategory))
            else Observable.empty()

        fun getArticles(page: Int? = null,language: String, ref:String?, idCategory : String) =
            if(ref != null) service.searchArticles(page,language,ref, ORDERARTICLE, buildArticles(idCategory))
            else Observable.empty()

        fun getArticlesByUids(page: Int? = null,language: String, ref:String?, uids : List<String>?) =
            if(ref != null) service.searchArticles(page,language,ref, ORDERARTICLE, buildRelatedArticlesUID(uids))
            else Observable.empty()
    }
}

