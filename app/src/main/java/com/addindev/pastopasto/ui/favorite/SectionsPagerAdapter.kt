package com.addindev.pastopasto.ui.favorite

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import com.addindev.pastopasto.R
import com.addindev.pastopasto.ui.movie.favmovies.FavMoviesFragment
import com.addindev.pastopasto.ui.tvshow.favtvshow.FavTvShowFragment

internal class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_movies, R.string.tab_tv_shows)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }


    private val pages = listOf(
            FavMoviesFragment(),
            FavTvShowFragment()
    )

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }
}