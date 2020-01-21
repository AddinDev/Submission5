package com.addindev.pastopasto

import android.app.Application
import android.content.Context
import android.util.Log

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.interceptors.HttpLoggingInterceptor
import com.addindev.pastopasto.db.MovieCatalogueDatabase

import okhttp3.OkHttpClient
import androidx.room.Room
import java.util.*


class MovieCatalogue : Application() {

    lateinit var context: Context
    override fun onCreate() {
        super.onCreate()
        val okHttpClient = OkHttpClient.Builder().addNetworkInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.d("API", message) }).setLevel(HttpLoggingInterceptor.Level.BODY)).build()
        AndroidNetworking.initialize(applicationContext, okHttpClient)

        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY)
        context = applicationContext
    }

    companion object {
        const val MOVIE_DB_API_KEY = "f9213f759c8716e1eecddb4c90ca4b86"
    }

    fun getLanguage():String{
        if(Locale.getDefault().language.toLowerCase().equals("in")){
            return "id"
        }else{
            return "en-US"
        }
    }
}


