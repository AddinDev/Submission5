package com.addindev.pastopasto.ui.movie.favmovies


import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.addindev.pastopasto.db.MovieCatalogueDatabase
import com.addindev.pastopasto.ui.movie.pojo.ResultsItem

class FavMoviesViewModel(application: Application) : AndroidViewModel(application) {
    private val favMovies = MutableLiveData<MutableList<ResultsItem>>()
    private val movieCatalogueDatabase: MovieCatalogueDatabase = MovieCatalogueDatabase.getDatabase(getApplication())


    val movies: MutableLiveData<MutableList<ResultsItem>>
        get() {
            return favMovies
        }

    fun fetchFavMovies() {
        AsyncTask.execute {
            favMovies.postValue(movieCatalogueDatabase.movieDao().getAllMovie())
        }
    }
}
