package com.fullsekurity.theatreblood.activity

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.DrawableCompat
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.donor.DonorFragment
import com.fullsekurity.theatreblood.donors.DonorsFragment
import com.fullsekurity.theatreblood.input.InputFragment
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.modal.StandardModal
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.utils.Constants
import com.fullsekurity.theatreblood.utils.ContextInjectorModule
import com.fullsekurity.theatreblood.utils.DaggerContextDependencyInjector
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private val INPUT_FRAGMENT_TAG = "input"
    private val DONORS_FRAGMENT_TAG = "donors"
    private val DONOR_FRAGMENT_TAG = "donor"
    private lateinit var donorsFragment: DonorsFragment

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
                    .replace(R.id.home_container, InputFragment.newInstance(), INPUT_FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(R.id.donors_container, DonorsFragment.newInstance(), DONORS_FRAGMENT_TAG)
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
        setSupportActionBar(findViewById(R.id.toolbar))
        setupToolbar()
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                .add(R.id.home_container, InputFragment.newInstance(), INPUT_FRAGMENT_TAG)
                .commitNow()
            donorsFragment = DonorsFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                .add(R.id.donors_container, donorsFragment, DONORS_FRAGMENT_TAG)
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

    fun transitionToSingleDonorFragment(donor: Donor) {
        val inputFragment = supportFragmentManager.findFragmentByTag(INPUT_FRAGMENT_TAG)
        val donorsFragment = supportFragmentManager.findFragmentByTag(DONORS_FRAGMENT_TAG)
        if (inputFragment == null && donorsFragment == null) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                .add(R.id.home_container, DonorFragment.newInstance(donor), DONOR_FRAGMENT_TAG)
                .commitNow()

        } else if (inputFragment == null) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                .remove(donorsFragment!!)  // donorsFragment cannot be null
                .add(R.id.home_container, DonorFragment.newInstance(donor), DONOR_FRAGMENT_TAG)
                .commitNow()
        } else if (donorsFragment == null) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                .remove(inputFragment)
                .add(R.id.home_container, DonorFragment.newInstance(donor), DONOR_FRAGMENT_TAG)
                .commitNow()
        } else {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                .remove(inputFragment)
                .remove(donorsFragment)
                .add(R.id.home_container, DonorFragment.newInstance(donor), DONOR_FRAGMENT_TAG)
                .commitNow()
        }
    }

    private fun setupToolbar() {
        supportActionBar?.let { actionBar ->
            actionBar.setBackgroundDrawable(ColorDrawable(Color.parseColor(Constants.TOOLBAR_BACKGROUND_COLOR)))
            val toolbar = findViewById<Toolbar>(R.id.toolbar)
            colorizeToolbarOverflowButton(toolbar, Color.parseColor(Constants.TOOLBAR_TEXT_COLOR))
            toolbar.setTitleTextColor(Color.parseColor(Constants.TOOLBAR_TEXT_COLOR))
            toolbar.title = Constants.INITIAL_TOOLBAR_TITLE
        }
    }

    private fun colorizeToolbarOverflowButton(toolbar: Toolbar, color: Int): Boolean {
        val overflowIcon = toolbar.overflowIcon ?: return false
        toolbar.overflowIcon = getTintedDrawable(overflowIcon, color)
        return true
    }

    private fun getTintedDrawable(inputDrawable: Drawable, color: Int): Drawable {
        val wrapDrawable = DrawableCompat.wrap(inputDrawable)
        DrawableCompat.setTint(wrapDrawable, color)
        DrawableCompat.setTintMode(wrapDrawable, PorterDuff.Mode.SRC_IN)
        return wrapDrawable
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("Settings Selected"))
            true
        }
        R.id.action_favorite -> {
            LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("Favorites Selected"))
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    fun showDonors(donorList: List<Donor>) {
        donorsFragment.showDonors(donorList)
    }

}