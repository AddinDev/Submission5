package com.addindev.pastopasto

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.addindev.pastopasto.databinding.ActivityMainBotNavBinding

class MainBotNavActivity : AppCompatActivity() {
    private lateinit var navView: BottomNavigationView
    private lateinit var binding: ActivityMainBotNavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main_bot_nav)
        navView = binding.navView

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val intent = Intent(this@MainBotNavActivity, SettingsActivity::class.java)
            startActivity(intent)
            return true
        }
        return true
    }
}
