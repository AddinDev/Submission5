package com.addindev.pastopasto.ui.movie.searchmovie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.addindev.pastopasto.R
import com.addindev.pastopasto.databinding.ActivitySearchMovieBinding
import com.addindev.pastopasto.ui.movie.MoviesAdapter
import com.addindev.pastopasto.ui.movie.detailmovie.DetailMovieActivity
import com.addindev.pastopasto.ui.movie.pojo.ResultsItem
import com.addindev.pastopasto.ui.movie.pojo.search.ResponseSearchMovie
import com.addindev.pastopasto.ui.tvshow.searchtvshow.SearchTvShowViewModel

class SearchMovieActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySearchMovieBinding
    private val observer = Observer<ResponseSearchMovie>{
        if(it!=null){
            if(it.anError == null){
                if(it.results!!.isNotEmpty()){
                    val adapter = MoviesAdapter(it.results)
                    adapter.SetOnItemClickListener(object : MoviesAdapter.OnItemClickListener {
                        override fun onItemClick(view: View, model: ResultsItem) {
                            val goToDetailMovie = Intent(view.context, DetailMovieActivity::class.java)
                            goToDetailMovie.putExtra(DetailMovieActivity.SELECTED_MOVIE, model)
                            startActivity(goToDetailMovie)
                        }
                    })
                    binding.recyclerView.adapter = adapter
                }else{
                    binding.tvMessage.setText("${getString(R.string.message_no_results)} \"${intent.getStringExtra("query")}\"")
                    binding.tvMessage.visibility = View.VISIBLE
                }
            }else{
                binding.tvMessage.setText(it.anError.errorBody)
                binding.tvMessage.visibility = View.VISIBLE
            }
        }
        binding.progressBar.visibility = View.GONE

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_search_movie)
        val viewModel = ViewModelProviders.of(this).get(SearchMovieActivityViewModel::class.java)
        binding.viewmodel = viewModel
        binding.recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "${getString(R.string.search_results)} ${intent.getStringExtra("query")}"

        viewModel.searchMovie(intent.getStringExtra("query"))
        viewModel.responseSearchMovie.observe(this,observer)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
