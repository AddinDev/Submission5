package com.addindev.pastopasto.ui.stackview.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.addindev.pastopasto.R
import com.addindev.pastopasto.db.MovieCatalogueDatabase
import com.addindev.pastopasto.ui.movie.pojo.ResultsItem
import com.addindev.pastopasto.ui.stackview.FavoriteAppWidget

class StackRemoteViewsFactory(private val context: Context): RemoteViewsService.RemoteViewsFactory {
    private var listFavMovie : MutableList<ResultsItem> = mutableListOf()
    private val movieCatalogueDatabase: MovieCatalogueDatabase = MovieCatalogueDatabase.getDatabase(context)

    override fun onCreate() {

    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun onDataSetChanged() {
        Log.d("IDN","${StackRemoteViewsFactory::class.java.simpleName} : onDataSetChanged")
        AsyncTask.execute {
            listFavMovie.clear()
            listFavMovie = movieCatalogueDatabase.movieDao().getAllMovie()
            if(listFavMovie.isNotEmpty()){
                for(movie in listFavMovie){
                    Log.d("IDN","${StackRemoteViewsFactory::class.java.simpleName} : onDataSetChanged movie : ${movie.title}")
                }
            }
        }
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getViewAt(position: Int): RemoteViews? {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)

        val posterUrl = "https://image.tmdb.org/t/p/w342" + listFavMovie[position].posterPath
        Log.d("CekPosterUrl", "posterUrl = $posterUrl")

        val bmp: Bitmap = Glide.with(context)
                .asBitmap()
                .load(posterUrl)
                .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .get()

        rv.setImageViewBitmap(R.id.imageView, bmp)

        val extras = Bundle()
        extras.putInt(FavoriteAppWidget.EXTRA_ITEM, position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getCount(): Int {
        return listFavMovie.size
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {

    }

}