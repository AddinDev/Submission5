package com.addindev.pastopasto.ui.movie

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.addindev.pastopasto.ui.movie.pojo.ResultsItem

@Dao
interface MovieDao {
    companion object {
        const val TABLE_NAME: String = "table_fav_movie"
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(resultsItem: ResultsItem)

    @Query("SELECT * FROM ${TABLE_NAME}")
    fun getAllMovie(): MutableList<ResultsItem>

    @Query("SELECT * FROM ${TABLE_NAME}")
    fun getAllMovieCursor(): Cursor

    @Query("DELETE FROM ${TABLE_NAME}")
    fun deleteAll()

    @Query("SELECT * FROM ${TABLE_NAME}")
    fun getMovieById(): List<ResultsItem>

    @Query("SELECT * FROM ${TABLE_NAME} WHERE id = :id LIMIT 1")
    fun getMovieCursorById(id: Int): Cursor


    @Query("SELECT * FROM ${TABLE_NAME} WHERE id = :id LIMIT 1")
    fun getMovieById(id: Int): ResultsItem?

    @Query("DELETE FROM ${TABLE_NAME} WHERE id = :id")
    fun deleteMovieById(id: Int)
}