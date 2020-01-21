package com.addindev.pastopasto.ui.movie

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.addindev.pastopasto.MainBotNavActivity
import com.addindev.pastopasto.R
import com.addindev.pastopasto.SettingsActivity
import com.addindev.pastopasto.databinding.FragmentMovieBinding
import com.addindev.pastopasto.ui.movie.detailmovie.DetailMovieActivity
import com.addindev.pastopasto.ui.movie.pojo.ResponseMovies
import com.addindev.pastopasto.ui.movie.pojo.ResultsItem
import com.addindev.pastopasto.ui.movie.searchmovie.SearchMovieActivity



class MovieFragment : Fragment() {

    lateinit var alertDialog: AlertDialog
    lateinit var binding: FragmentMovieBinding
    lateinit var mViewModel: MovieFragmentViewModel


    private val getMovies = Observer<ResponseMovies> { responseMovies ->
        if (responseMovies != null) {
            if (responseMovies.anError == null) {
                val mAdapter = MoviesAdapter(responseMovies.results!!)

                mAdapter.SetOnItemClickListener(object : MoviesAdapter.OnItemClickListener {
                    override fun onItemClick(view: View, model: ResultsItem) {
                        val goToDetailMovie = Intent(view.context, DetailMovieActivity::class.java)
                        goToDetailMovie.putExtra(DetailMovieActivity.SELECTED_MOVIE, model)
                        startActivity(goToDetailMovie)
                    }
                })

                binding.recyclerView.adapter = mAdapter
            } else {
                alertDialog.setMessage(responseMovies.anError.message)
                alertDialog.show()
            }
        }
        binding.progressBar.visibility = View.GONE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie, container, false)
        mViewModel = ViewModelProviders.of(this).get(MovieFragmentViewModel::class.java)
        binding.viewmodel = mViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainBotNavActivity).setSupportActionBar(binding.toolbar)

        setupToolbar()
        alertDialog = AlertDialog.Builder(view.context).setTitle(getString(R.string.failure)).setPositiveButton("OK") { dialog, which -> }.create()

        val layoutManager = LinearLayoutManager(view.context)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.setHasFixedSize(true)

        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        mViewModel.doRequestListMovies()
        mViewModel.movies.observe(this, getMovies)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)

        var searchView: SearchView? = null
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView

            val searchClose = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
            searchClose.setImageResource(R.drawable.ic_close_white_24dp)

            var searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText?
            searchEditText?.setTextColor(ContextCompat.getColor(mViewModel.getApplication(), R.color.white))
            searchEditText?.setHintTextColor(ContextCompat.getColor(mViewModel.getApplication(), R.color.colorAccent))

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    val toSearchActivity = Intent(mViewModel.getApplication(), SearchMovieActivity::class.java)
                    toSearchActivity.putExtra("query",query)
                    startActivity(toSearchActivity)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val intent = Intent(context, SettingsActivity::class.java)
            startActivity(intent)
            return true
        }
        return true
    }

    private fun setupToolbar() {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        }
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.tab_movies)
    }
}