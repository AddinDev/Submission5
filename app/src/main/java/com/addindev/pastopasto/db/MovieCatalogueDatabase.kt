package com.addindev.pastopasto.db

import android.content.Context
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.Room
import com.addindev.pastopasto.ui.movie.MovieDao
import com.addindev.pastopasto.ui.movie.pojo.ResultsItem
import androidx.sqlite.db.SupportSQLiteDatabase
import com.addindev.pastopasto.ui.tvshow.TvShowDao


@Database(entities = [ResultsItem::class, com.addindev.pastopasto.ui.tvshow.pojo.ResultsItem::class], version = 1, exportSchema = false)
abstract class MovieCatalogueDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun tvShowDao(): TvShowDao


    companion object{
        const val DB_NAME = "movie_catalogue_database"

        @Volatile
        private var INSTANCE: MovieCatalogueDatabase? = null

        fun getDatabase(context: Context): MovieCatalogueDatabase {
            if (INSTANCE == null) {
                synchronized(MovieCatalogueDatabase::class.java) {
                    if (INSTANCE == null) {
                        // Create database here
                        INSTANCE = Room.databaseBuilder(context,
                                MovieCatalogueDatabase::class.java, DB_NAME)
                                .fallbackToDestructiveMigration()
                                .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}