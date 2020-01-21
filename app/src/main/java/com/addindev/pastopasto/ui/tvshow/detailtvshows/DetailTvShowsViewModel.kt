package com.addindev.pastopasto.ui.tvshow.detailtvshows

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.addindev.pastopasto.ui.tvshow.pojo.ResultsItem


class DetailTvShowsViewModel : ViewModel() {
    val resultsItem = MutableLiveData<ResultsItem>()
}
