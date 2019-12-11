package com.fullsekurity.theatreblood.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.donors.DonorsFragment
import com.fullsekurity.theatreblood.input.HomeFragment
import com.fullsekurity.theatreblood.modal.StandardModal
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.utils.ContextInjectorModule
import com.fullsekurity.theatreblood.utils.DaggerContextDependencyInjector
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    internal var repository: Repository? = null
        @Inject set

    enum class UITheme {
        LIGHT, DARK, NOT_ASSIGNED,
    }

    var currentTheme: UITheme = UITheme.LIGHT

    override fun onResume() {
        super.onResume()
        val settings = getSharedPreferences("THEME", Context.MODE_PRIVATE)
        val name: String? = settings.getString("THEME", UITheme.LIGHT.name)
        if (name != null) {
            currentTheme = UITheme.valueOf(name)
        }
    }

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
                    .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
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
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                .add(R.id.donors_container, DonorsFragment.newInstance())
                .commitNow()
            if (repository == null) {
                StandardModal(
                    this,
                    modalType = StandardModal.ModalType.STANDARD,
                    titleText = getString(R.string.std_modal_no_repository_title),
                    bodyText = getString(R.string.std_modal_no_repository_body),
                    positiveText = getString(R.string.std_modal_ok),
                    dialogFinishedListener = object : StandardModal.DialogFinishedListener {
                        override fun onPositive(password: String) {
                            finish()
                        }
                        override fun onNegative() { }
                        override fun onNeutral() { }
                        override fun onBackPressed() {
                            finish()
                        }
                    }
                ).show(supportFragmentManager, "MODAL")
            }
        }

    }

}