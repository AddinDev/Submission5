package com.addindev.pastopasto.ui.stackview.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.DatabaseUtils
import android.net.Uri
import android.util.Log
import com.addindev.pastopasto.db.MovieCatalogueDatabase
import com.addindev.pastopasto.ui.movie.MovieDao.Companion.TABLE_NAME

class FavoriteProvider : ContentProvider() {


    private val MOVIE = 1
    private val MOVIE_ID = 2
    val AUTHORITY = "com.addindev.pastopasto"
    private val SCHEME = "content"

    private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        // content://com.addindev.pastopasto/table_fav_movie
        this.addURI(AUTHORITY, TABLE_NAME, MOVIE)

        // content://com.addindev.pastopasto/movie/id
        this.addURI(AUTHORITY, TABLE_NAME + "/#", MOVIE_ID)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val movieCatalogueDatabase: MovieCatalogueDatabase = MovieCatalogueDatabase.getDatabase(context!!)
        val cursor = when(sUriMatcher.match(uri)){
            MOVIE -> {
                movieCatalogueDatabase.movieDao().getAllMovieCursor()
            }
            MOVIE_ID -> {
                movieCatalogueDatabase.movieDao().getMovieCursorById(uri.lastPathSegment!!.toInt())
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

        Log.d("IDN","Dump Cursor : " + DatabaseUtils.dumpCursorToString(cursor))

        return cursor
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0

    }

    override fun getType(uri: Uri): String? {
        return null
    }

}