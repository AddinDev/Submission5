package com.addindev.pastopasto.ui.tvshow.favtvshow

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.addindev.pastopasto.db.MovieCatalogueDatabase
import com.addindev.pastopasto.ui.tvshow.pojo.ResultsItem

class FavTvShowViewModel(application: Application) : AndroidViewModel(application) {
    private val favTvShow = MutableLiveData<MutableList<ResultsItem>>()
    private val movieCatalogueDatabase: MovieCatalogueDatabase = MovieCatalogueDatabase.getDatabase(getApplication())

    internal val getTvShowList: MutableLiveData<MutableList<ResultsItem>>
        get() {
            return favTvShow
        }

    internal fun fetchFavTvShows() {
        AsyncTask.execute {
            favTvShow.postValue(movieCatalogueDatabase.tvShowDao().getAllTvShow())
        }
    }
}
