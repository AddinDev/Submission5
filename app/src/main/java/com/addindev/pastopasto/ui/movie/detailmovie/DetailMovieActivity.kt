package com.addindev.pastopasto.ui.movie.detailmovie

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.addindev.pastopasto.R
import com.addindev.pastopasto.databinding.ActivityDetailMovieBinding
import com.addindev.pastopasto.db.MovieCatalogueDatabase
import com.addindev.pastopasto.ui.movie.pojo.ResultsItem
import com.addindev.pastopasto.ui.stackview.FavoriteAppWidget


class DetailMovieActivity : AppCompatActivity() {

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private lateinit var movieCatalogueDatabase: MovieCatalogueDatabase
    private lateinit var movieModel: ResultsItem
    lateinit var binding: ActivityDetailMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        movieCatalogueDatabase = MovieCatalogueDatabase.getDatabase(this)
        val viewModel = ViewModelProviders.of(this).get(DetailMovieViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_movie)

        movieModel = intent.getParcelableExtra(SELECTED_MOVIE)
        viewModel.resultsItem = movieModel
        binding.setViewmodel(viewModel)


        Glide.with(this).load("https://image.tmdb.org/t/p/w185/" + movieModel.posterPath!!).into(binding.imgPoster)

        setTitle(viewModel.resultsItem!!.title)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        checkFavorite()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        val SELECTED_MOVIE = "selected_movie"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_detail, menu)
        menuItem = menu
        setFavMovieDb()
        return true
    }

    private fun setFavMovieDb() {
        if (isFavorite)
            menuItem?.findItem(R.id.action_favorite)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp)
        else
            menuItem?.findItem(R.id.action_favorite)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp)
    }

    private fun checkFavorite() {
        AsyncTask.execute {
            val moviedb = movieCatalogueDatabase.movieDao().getMovieById(movieModel.id)

            isFavorite = moviedb?.title != null

            runOnUiThread {
                setFavMovieDb()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_favorite -> {
                if (isFavorite) {
                    AsyncTask.execute {
                        movieCatalogueDatabase.movieDao().deleteMovieById(movieModel.id)
                        runOnUiThread {
                            isFavorite = !isFavorite
                            setFavMovieDb()
                            Snackbar.make(binding.root, getString(R.string.favorite_success_remove_movie), Snackbar.LENGTH_LONG).show()
                        }
                    }
                } else {
                    AsyncTask.execute {
                        movieCatalogueDatabase.movieDao().insert(movieModel)
                        runOnUiThread {
                            isFavorite = !isFavorite
                            setFavMovieDb()
                            Snackbar.make(binding.root, getString(R.string.favorite_success_add_movie), Snackbar.LENGTH_LONG).show()
                        }
                    }
                }

                val appWidgetManager = AppWidgetManager.getInstance(this)
                val thisWidget = ComponentName(this, FavoriteAppWidget::class.java)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}

