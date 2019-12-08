package com.fullsekurity.theatreblood.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.donors.DonorsFragment
import com.fullsekurity.theatreblood.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                    .replace(R.id.home_container, HomeFragment.newInstance())
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                    .replace(R.id.donors_container, DonorsFragment.newInstance())
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                .add(R.id.home_container, HomeFragment.newInstance())
                .commitNow()
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                .add(R.id.donors_container, DonorsFragment.newInstance())
                .commitNow()
        }

    }

}
