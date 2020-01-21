package com.addindev.pastopasto.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.addindev.pastopasto.R
import com.addindev.pastopasto.SettingsActivity
import com.addindev.pastopasto.databinding.FragmentFavoriteBinding

class
FavoriteFragment : Fragment() {

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false)
        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
        binding.viewmodel = favoriteViewModel
        binding.viewPager.adapter = SectionsPagerAdapter(binding.root.context, childFragmentManager)
        binding.tabs.setupWithViewPager(binding.viewPager)
        setupToolbar()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
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
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_favorite)
    }

}