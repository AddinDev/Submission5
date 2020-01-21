package com.addindev.pastopasto.ui.movie.searchmovie

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.addindev.pastopasto.MovieCatalogue
import com.addindev.pastopasto.ui.movie.pojo.ResultsItem
import com.addindev.pastopasto.ui.movie.pojo.search.ResponseSearchMovie

class SearchMovieActivityViewModel(application: Application) :AndroidViewModel(application){
    val responseSearchMovie : MutableLiveData<ResponseSearchMovie> = MutableLiveData()


    fun searchMovie(query: String){
        AndroidNetworking.get("https://api.themoviedb.org/3/search/movie")
                .addQueryParameter("api_key",MovieCatalogue.MOVIE_DB_API_KEY)
                .addQueryParameter("language",(getApplication() as MovieCatalogue).getLanguage())
                .addQueryParameter("query",query)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(ResponseSearchMovie::class.java, object : ParsedRequestListener<ResponseSearchMovie> {
                    override fun onResponse(response: ResponseSearchMovie?) {
                        responseSearchMovie.postValue(response)
                    }

                    override fun onError(anError: ANError?) {
                        responseSearchMovie.postValue(ResponseSearchMovie(anError))
                    }
                })
    }
}