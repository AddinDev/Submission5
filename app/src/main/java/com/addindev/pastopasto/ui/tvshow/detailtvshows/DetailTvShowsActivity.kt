package com.addindev.pastopasto.ui.tvshow.detailtvshows

import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.addindev.pastopasto.R
import com.addindev.pastopasto.databinding.ActivityDetailTvShowsBinding
import com.addindev.pastopasto.db.MovieCatalogueDatabase
import com.addindev.pastopasto.ui.tvshow.pojo.ResultsItem

class DetailTvShowsActivity : AppCompatActivity() {

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private lateinit var movieCatalogueDatabase: MovieCatalogueDatabase
    private lateinit var tvShowModel: ResultsItem
    private lateinit var binding: ActivityDetailTvShowsBinding

    private val resultItemObserver = Observer<ResultsItem> {
        title = it.name
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieCatalogueDatabase = MovieCatalogueDatabase.getDatabase(this)
        val viewModel = ViewModelProviders.of(this).get(DetailTvShowsViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_tv_shows)
        tvShowModel = intent.getParcelableExtra(SELECTED_TV_SHOWS)
        viewModel.resultsItem.postValue(tvShowModel)
        binding.viewmodel = viewModel

        Glide.with(this).load("https://image.tmdb.org/t/p/w185/" + tvShowModel.posterPath!!).into(binding.imgPoster)

        viewModel.resultsItem.observe(this, resultItemObserver)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        checkFavorite()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        val SELECTED_TV_SHOWS = "selected_tv_shows"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_detail, menu)
        menuItem = menu
        setFavTvShowDb()
        return true
    }

    private fun setFavTvShowDb() {
        if (isFavorite)
            menuItem?.findItem(R.id.action_favorite)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp)
        else
            menuItem?.findItem(R.id.action_favorite)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp)
    }

    private fun checkFavorite() {
        AsyncTask.execute {
            val tvShowDb = movieCatalogueDatabase.tvShowDao().getTvShowById(tvShowModel.id)

            isFavorite = tvShowDb?.name != null

            runOnUiThread {
                setFavTvShowDb()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_favorite -> {
                if (isFavorite) {
                    AsyncTask.execute {
                        movieCatalogueDatabase.tvShowDao().deleteTvShowById(tvShowModel.id)
                        runOnUiThread {
                            isFavorite = !isFavorite
                            setFavTvShowDb()
                            Snackbar.make(binding.root, getString(R.string.favorite_success_remove_tv_show), Snackbar.LENGTH_LONG).show()
                        }
                    }
                } else {
                    AsyncTask.execute {
                        movieCatalogueDatabase.tvShowDao().insert(tvShowModel)
                        runOnUiThread {
                            isFavorite = !isFavorite
                            setFavTvShowDb()
                            Snackbar.make(binding.root, getString(R.string.favorite_success_add_tv_show), Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

