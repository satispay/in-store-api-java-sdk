package com.satispay.protocore.models.help

import java.io.Serializable

data class PrismicResponse(
    val license: String,
    val next_page: String?,
    val page: Int?,
    val prev_page: Int?,
    val results: List<Result>?,
    val results_per_page: Int,
    val results_size: Int,
    val total_pages: Int,
    val total_results_size: Int,
    val version: String
)

data class PrismicResponseCategories(
    val license: String,
    val next_page: String?,
    val page: Int?,
    val prev_page: Int?,
    val results: List<ResultCategories>?,
    val results_per_page: Int,
    val results_size: Int,
    val total_pages: Int,
    val total_results_size: Int,
    val version: String
)

data class Result(
    val `data`: Data,
    val alternate_languages: List<AlternateLanguage>,
    val first_publication_date: String,
    val href: String,
    val id: String,
    val lang: String,
    val last_publication_date: String,
    val linked_documents: List<LinkedDocument>,
    val slugs: List<String>,
    val tags: List<String>,
    val type: String,
    val uid: String) : Serializable

data class ResultCategories(
    val `data`: DataCategories,
    val alternate_languages: List<AlternateLanguage>,
    val first_publication_date: String,
    val href: String,
    val id: String,
    val lang: String,
    val last_publication_date: String,
    val linked_documents: List<LinkedDocument>,
    val slugs: List<String>,
    val tags: List<String>,
    val type: String,
    val uid: String
): Serializable

data class AlternateLanguage(
    val id: String,
    val lang: String,
    val type: String,
    val uid: String
): Serializable

data class Data(
    val body: List<Body>,
    val category: Category,
    val page: Page,
    val related: List<Related>?,
    val title: List<Value>,
    val update: String
): Serializable

data class LinkedDocument(
    val id: String,
    val lang: String,
    val slug: String,
    val tags: List<String>,
    val type: String
): Serializable

data class DataCategories(
    val body: List<Body>,
    val category: Category,
    val page: Page,
    val related: Related?,
    val relatedObj: Related,
    val title: List<Value>,
    val update: String
): Serializable

data class Body(
    val slice_label: String?,
    val slice_type: String,
    val value: List<Value>?,
    val primary: Primary?
    ): Serializable

data class Category(
    val id: String,
    val isBroken: Boolean,
    val lang: String,
    val link_type: String,
    val slug: String,
    val tags: List<String>,
    val type: String,
    val uid: String
): Serializable

data class Page(
    val id: String,
    val isBroken: Boolean,
    val lang: String,
    val link_type: String,
    val slug: String,
    val tags: List<String>,
    val type: String,
    val uid: String
): Serializable

data class Related(
    val link: Link
): Serializable

data class Link(
    val id: String,
    val isBroken: Boolean,
    val lang: String,
    val link_type: String,
    val slug: String,
    val tags: List<String>,
    val type: String,
    val uid: String
): Serializable

data class Value(
    val spans: List<Spans>?,
    val text: String,
    val type: String
): Serializable
{
    companion object{
        const val PARAGRAPH = "paragraph"
        const val HEADING2 = "heading2"
        const val LISTITEM = "list-item"
    }
}

data class Spans(
    val start: Int,
    val end: Int,
    val type: String,
    val data: SpansLink?
): Serializable
{
    companion object{
        const val STRONG = "strong"
        const val ITALIC = "em"
    }
}

data class SpansLink(
    val link_type: String,
    val url: String,
    val uid: String
): Serializable
{
    companion object {
        const val DOCUMENT = "Document"
    }
}

data class Primary(
    val text: List<Value>,
    val link: List<Value>
): Serializable

data class AlgoliaResponse(
    val hits: List<Hit>,
    val hitsPerPage: Int,
    val nbHits: Int,
    val nbPages: Int,
    val page: Int,
    val params: String,
    val parsed_query: String,
    val processingTimeMS: Int,
    val query: String
)
data class Hit(
    val uid: String,
    val title: String?
)