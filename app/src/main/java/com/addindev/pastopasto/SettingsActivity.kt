package com.addindev.pastopasto

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupActionBar()

        if (fragmentManager.findFragmentById(R.id.content) == null) {
            fragmentManager.beginTransaction()
                .add(R.id.content, SettingsFragment()).commit()
        }
    }

    private fun setupActionBar() {
        val rootView = findViewById<View>(R.id.action_bar_root) as ViewGroup
        val view = layoutInflater.inflate(R.layout.activity_setting, rootView, false)
        rootView.addView(view, 0)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = getString(R.string.txt_setting)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
