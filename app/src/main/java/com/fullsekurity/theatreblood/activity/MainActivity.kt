package com.fullsekurity.theatreblood.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.donors.DonorsFragment
import com.fullsekurity.theatreblood.home.HomeFragment
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.utils.ContextInjectorModule
import com.fullsekurity.theatreblood.utils.DaggerContextDependencyInjector
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    internal var repository: Repository? = null
        @Inject set

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
        DaggerContextDependencyInjector.builder()
            .contextInjectorModule(ContextInjectorModule(applicationContext))
            .build()
            .inject(this)
        repository ?: return completeActivity()
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

    private fun completeActivity() {
        finish()
        // TODO: popup a modal here and then finish after a click
    }

}
