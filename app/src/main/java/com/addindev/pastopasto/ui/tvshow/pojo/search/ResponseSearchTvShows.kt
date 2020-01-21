package com.addindev.pastopasto.ui.tvshow.pojo.search

import com.androidnetworking.error.ANError
import com.google.gson.annotations.SerializedName
import com.addindev.pastopasto.ui.tvshow.pojo.ResultsItem

class ResponseSearchTvShows(val anError: ANError) {

    @SerializedName("page")
    var page: Int = 0

    @SerializedName("total_pages")
    var totalPages: Int = 0

    @SerializedName("results")
    var results: List<ResultsItem>? = null

    @SerializedName("total_results")
    var totalResults: Int = 0

    override fun toString(): String {
        return "ResponseSearchTvShows{" +
                "page = '" + page + '\''.toString() +
                ",total_pages = '" + totalPages + '\''.toString() +
                ",results = '" + results + '\''.toString() +
                ",total_results = '" + totalResults + '\''.toString() +
                "}"
    }
}