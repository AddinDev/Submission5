package com.addindev.pastopasto.ui.movie

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.addindev.pastopasto.MovieCatalogue
import com.addindev.pastopasto.ui.movie.pojo.ResponseMovies
import com.addindev.pastopasto.ui.movie.pojo.search.ResponseSearchMovie

class MovieFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val responseMovies = MutableLiveData<ResponseMovies>()
    private val responseSearchMovie = MutableLiveData<ResponseSearchMovie>()


    val movies: MutableLiveData<ResponseMovies>
        get() {
            return responseMovies
        }

    fun doRequestListMovies() {
        AndroidNetworking.get("https://api.themoviedb.org/3/discover/movie")
                .addQueryParameter("api_key", MovieCatalogue.MOVIE_DB_API_KEY)
                .addQueryParameter("language", (getApplication() as MovieCatalogue).getLanguage())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(ResponseMovies::class.java, object : ParsedRequestListener<ResponseMovies> {
                    override fun onResponse(response: ResponseMovies) {
                        responseMovies.postValue(response)
                    }

                    override fun onError(anError: ANError) {
                        responseMovies.value = ResponseMovies(anError)
                    }
                })
    }

    internal fun requestSearchMovie(keyword: String){
        AndroidNetworking.get("https://api.themoviedb.org/3/search/movie")
                .addQueryParameter("api_key",MovieCatalogue.MOVIE_DB_API_KEY)
                .addQueryParameter("language",(getApplication() as MovieCatalogue).getLanguage())
                .addQueryParameter("query",keyword)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(ResponseSearchMovie::class.java, object : ParsedRequestListener<ResponseSearchMovie> {
                    override fun onResponse(response: ResponseSearchMovie?) {
                        responseMovies.postValue(object : ResponseMovies(resultsItems = response?.results, anError = null) {})
                    }

                    override fun onError(anError: ANError?) {
                        responseMovies.value = (ResponseMovies(anError = anError))
                    }
                })
    }

}