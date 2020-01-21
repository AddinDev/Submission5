package com.addindev.pastopasto.ui.tvshow

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.addindev.pastopasto.MovieCatalogue
import com.addindev.pastopasto.ui.movie.pojo.search.ResponseSearchMovie
import com.addindev.pastopasto.ui.tvshow.pojo.ResponseTvShows

class TvShowViewModel(application: Application) : AndroidViewModel(application) {
    private val responseTvShows = MutableLiveData<ResponseTvShows>()

    internal val getTvShowList: MutableLiveData<ResponseTvShows>
        get() {
            return responseTvShows
        }

    internal fun doRequestListTvShows() {
        AndroidNetworking.get("https://api.themoviedb.org/3/discover/tv")
                .addQueryParameter("api_key", MovieCatalogue.MOVIE_DB_API_KEY)
                .addQueryParameter("language", (getApplication() as MovieCatalogue).getLanguage())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(ResponseTvShows::class.java, object : ParsedRequestListener<ResponseTvShows> {

                    override fun onResponse(response: ResponseTvShows) {
                        responseTvShows.postValue(response)
                    }

                    override fun onError(anError: ANError) {
                        responseTvShows.value = ResponseTvShows(anError)
                    }
                })
    }
}