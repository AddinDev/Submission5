package com.addindev.pastopasto.ui.tvshow.favtvshow

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
import com.addindev.pastopasto.databinding.FavTvShowFragmentBinding
import com.addindev.pastopasto.ui.tvshow.TvShowsAdapter
import com.addindev.pastopasto.ui.tvshow.detailtvshows.DetailTvShowsActivity
import com.addindev.pastopasto.ui.tvshow.pojo.ResultsItem

class FavTvShowFragment : Fragment() {

    private lateinit var binding: FavTvShowFragmentBinding
    private lateinit var mViewModel: FavTvShowViewModel
    private lateinit var alertDialog: AlertDialog

    val getTvShow = Observer<List<ResultsItem>> {
        val mAdapter = TvShowsAdapter(it)
        if (it.size > 0) {
            binding.tvMessage.visibility = View.GONE
            mAdapter.SetOnItemClickListener(object : TvShowsAdapter.OnItemClickListener {
                override fun onItemClick(view: View, model: ResultsItem) {
                    val goToDetailMovie = Intent(view.context, DetailTvShowsActivity::class.java)
                    goToDetailMovie.putExtra(DetailTvShowsActivity.SELECTED_TV_SHOWS, model)
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fav_tv_show_fragment, container, false)
        mViewModel = ViewModelProviders.of(this).get(FavTvShowViewModel::class.java)
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
        mViewModel.fetchFavTvShows()
        mViewModel.getTvShowList.observe(this, getTvShow)
    }
}
