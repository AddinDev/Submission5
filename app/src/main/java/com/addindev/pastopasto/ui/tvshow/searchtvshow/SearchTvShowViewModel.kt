package com.addindev.pastopasto.ui.tvshow.searchtvshow

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.addindev.pastopasto.MovieCatalogue
import com.addindev.pastopasto.ui.tvshow.pojo.search.ResponseSearchTvShows

class SearchTvShowViewModel(application: Application) : AndroidViewModel(application){
    val responseSearchTvShow: MutableLiveData<ResponseSearchTvShows> = MutableLiveData()

    fun searchTvShow(query: String){
        AndroidNetworking.get("https://api.themoviedb.org/3/search/tv?api_key=f9213f759c8716e1eecddb4c90ca4b86&language=en-US&query=walking")
                .addQueryParameter("api_key",MovieCatalogue.MOVIE_DB_API_KEY)
                .addQueryParameter("language",(getApplication() as MovieCatalogue).getLanguage())
                .addQueryParameter("query",query)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(ResponseSearchTvShows::class.java, object : ParsedRequestListener<ResponseSearchTvShows> {
                    override fun onResponse(response: ResponseSearchTvShows?) {
                        responseSearchTvShow.postValue(response)
                    }

                    override fun onError(anError: ANError?) {
                        responseSearchTvShow.postValue(ResponseSearchTvShows(anError!!))
                    }
                })
    }

}