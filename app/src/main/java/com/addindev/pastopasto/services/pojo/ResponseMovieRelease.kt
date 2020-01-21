package com.addindev.pastopasto.notifications.services.pojo

import com.androidnetworking.error.ANError
import com.google.gson.annotations.SerializedName
import com.addindev.pastopasto.ui.movie.pojo.ResultsItem

class ResponseMovieRelease(val anError: ANError?){

	@SerializedName("page")
	val page: Int? = null

	@SerializedName("total_pages")
	val totalPages: Int? = null

	@SerializedName("results")
	val results: List<ResultsItem>? = null

	@SerializedName("total_results")
	val totalResults: Int? = null
}