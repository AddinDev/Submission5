package com.addindev.pastopasto.ui.tvshow.searchtvshow

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.addindev.pastopasto.R
import com.addindev.pastopasto.databinding.ActivitySearchTvShowBinding
import com.addindev.pastopasto.ui.tvshow.TvShowsAdapter
import com.addindev.pastopasto.ui.tvshow.detailtvshows.DetailTvShowsActivity
import com.addindev.pastopasto.ui.tvshow.pojo.search.ResponseSearchTvShows

class SearchTvShowActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySearchTvShowBinding
    private val observer = Observer<ResponseSearchTvShows>{
        if(it!=null){
            if(it.anError == null){
                if(it.results!!.isNotEmpty()){
                    val adapter = TvShowsAdapter(it.results!!)
                    adapter.SetOnItemClickListener(object : TvShowsAdapter.OnItemClickListener {

                        override fun onItemClick(view: View, model: com.addindev.pastopasto.ui.tvshow.pojo.ResultsItem) {
                            val goToDetailTvShow = Intent(view.context, DetailTvShowsActivity::class.java)
                            goToDetailTvShow.putExtra(DetailTvShowsActivity.SELECTED_TV_SHOWS, model)
                            startActivity(goToDetailTvShow)
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
        binding = DataBindingUtil.setContentView(this,R.layout.activity_search_tv_show)
        val viewModel = ViewModelProviders.of(this).get(SearchTvShowViewModel::class.java)
        binding.viewmodel = viewModel
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "${getString(R.string.search_results)} ${intent.getStringExtra("query")}"

        viewModel.searchTvShow(intent.getStringExtra("query"))
        viewModel.responseSearchTvShow.observe(this,observer)

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
