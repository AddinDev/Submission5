package com.addindev.pastopasto.ui.movie.pojo.search

import com.androidnetworking.error.ANError
import com.google.gson.annotations.SerializedName
import com.addindev.pastopasto.ui.movie.pojo.ResultsItem

class ResponseSearchMovie(val anError: ANError?){

	@SerializedName("page")
	public val page: Int? = null

	@SerializedName("total_pages")
	public val totalPages: Int? = null

	@SerializedName("results")
	public val results: List<ResultsItem>? = null

	@SerializedName("total_results")
	public val totalResults: Int? = null
}