package com.addindev.pastopasto.ui.tvshow.pojo

import com.androidnetworking.error.ANError
import com.google.gson.annotations.SerializedName

class ResponseTvShows(val anError: ANError?) {

    @SerializedName("page")
    private val page: Int = 0

    @SerializedName("total_pages")
    private val totalPages: Int = 0

    @SerializedName("results")
    val results: List<ResultsItem>? = null

    @SerializedName("total_results")
    private val totalResults: Int = 0

    override fun toString(): String {
        return "ResponseTvShows{" +
                "page = '" + page + '\''.toString() +
                ",total_pages = '" + totalPages + '\''.toString() +
                ",results = '" + results + '\''.toString() +
                ",total_results = '" + totalResults + '\''.toString() +
                "}"
    }
}