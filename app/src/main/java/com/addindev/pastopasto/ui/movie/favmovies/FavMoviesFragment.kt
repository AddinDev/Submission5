package com.addindev.pastopasto.ui.movie.favmovies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.addindev.pastopasto.R
import com.addindev.pastopasto.databinding.FavMoviesFragmentBinding
import com.addindev.pastopasto.ui.movie.MoviesAdapter
import com.addindev.pastopasto.ui.movie.detailmovie.DetailMovieActivity
import com.addindev.pastopasto.ui.movie.pojo.ResultsItem

class FavMoviesFragment : Fragment() {

    private lateinit var binding: FavMoviesFragmentBinding
    private lateinit var mViewModel: FavMoviesViewModel
    private lateinit var alertDialog: AlertDialog

    private val getFavMovie = Observer<List<ResultsItem>> {
        val mAdapter = MoviesAdapter(it)
        if (it.size > 0) {
            binding.tvMessage.visibility = View.GONE
            mAdapter.SetOnItemClickListener(object : MoviesAdapter.OnItemClickListener {
                override fun onItemClick(view: View, model: ResultsItem) {
                    val goToDetailMovie = Intent(view.context, DetailMovieActivity::class.java)
                    goToDetailMovie.putExtra(DetailMovieActivity.SELECTED_MOVIE, model)
                    startActivity(goToDetailMovie)
                }
            })

            binding.recyclerView.adapter = mAdapter
        } else {
            binding.recyclerView.adapter = null
            binding.tvMessage.visibility = View.VISIBLE
        }

        binding.progressBar.visibility = View.GONE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fav_movies_fragment, container, false)
        mViewModel = ViewModelProviders.of(this).get(FavMoviesViewModel::class.java)
        binding.viewmodel = mViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alertDialog = AlertDialog.Builder(view.context).setTitle(getString(R.string.failure)).setPositiveButton("OK") { dialog, which -> }.create()

        val layoutManager = LinearLayoutManager(view.context)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.setHasFixedSize(true)
        binding.viewmodel = mViewModel

    }

    override fun onResume() {
        super.onResume()
        mViewModel.fetchFavMovies()
        mViewModel.movies.observe(this, getFavMovie)
    }

}
