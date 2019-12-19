package com.fullsekurity.theatreblood.activity

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.FragmentManager
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.donor.DonorFragment
import com.fullsekurity.theatreblood.donors.DonateProductsFragment
import com.fullsekurity.theatreblood.modal.StandardModal
import com.fullsekurity.theatreblood.products.CreateProductsFragment
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.Constants
import com.fullsekurity.theatreblood.utils.Constants.ROOT_FRAGMENT_TAG
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    private val tag = MainActivity::class.java.simpleName

    lateinit var repository: Repository
    @Inject
    lateinit var uiViewModel: UIViewModel

    private var networkStatusMenuItem: MenuItem? = null

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
        setupRepositoryDatabase()
        setupToolbar()
        setToolbarNetworkStatus()
    }

    override fun onStop() {
        super.onStop()
        repository.closeDatabase()
        repository.onCleared()
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_donations -> {
                supportFragmentManager.popBackStack(ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                loadDonateProductsFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_inventory -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_transfusions -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.plant(Timber.DebugTree())
        repository = Repository(this)
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(this))
            .build()
            .inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        if (savedInstanceState == null) {
            navView.selectedItemId = R.id.navigation_donations

        }
    }

    private fun setupRepositoryDatabase() {
        repository.setBloodDatabase(this)
    }

    private fun refreshDatabase() {
        val progressBar = main_progress_bar
        progressBar.visibility = View.VISIBLE
        repository.refreshDatabase(progressBar, this)
    }

    fun finishActivity() {
        finish()
    }

    private fun loadDonateProductsFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
            .replace(R.id.main_activity_container, DonateProductsFragment.newInstance())
            .commitAllowingStateLoss()
    }

    fun loadDonorFragment(donor: Donor) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            .replace(R.id.main_activity_container, DonorFragment.newInstance(donor))
            .addToBackStack(ROOT_FRAGMENT_TAG)
            .commitAllowingStateLoss()
    }

    fun loadCreateProductsFragment(donor: Donor) {
        supportFragmentManager.popBackStack(ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            .replace(R.id.main_activity_container, CreateProductsFragment.newInstance(donor))
            .addToBackStack(ROOT_FRAGMENT_TAG)
            .commitAllowingStateLoss()
    }

    private fun setupToolbar() {
        supportActionBar?.let { actionBar ->
            actionBar.setBackgroundDrawable(ColorDrawable(Color.parseColor(uiViewModel.primaryColor)))
            colorizeToolbarOverflowButton(toolbar, Color.parseColor(uiViewModel.toolbarTextColor))
            val upArrow = ContextCompat.getDrawable(this, R.drawable.toolbar_back_arrow)
            actionBar.setHomeAsUpIndicator(upArrow);
            toolbar.setTitleTextColor(Color.parseColor(uiViewModel.toolbarTextColor))
            toolbar.title = Constants.DONATE_PRODUCTS_TITLE
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

    fun setToolbarNetworkStatus() {
        val resInt: Int
        if (repository.isOfflineMode) {
            resInt = R.drawable.ic_network_status_none
        } else {
            resInt = repository.getNetworkStatusResInt()
        }
        networkStatusMenuItem?.let { networkStatusMenuItem ->
            runOnUiThread {
                networkStatusMenuItem.icon = ContextCompat.getDrawable(this, resInt)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            true
        }
        R.id.action_favorite -> {
            true
        }
        R.id.action_staging_count -> {
            val entryCountList = listOf(
                repository.databaseCount(repository.insertedBloodDatabase),
                repository.databaseCount(repository.modifiedBloodDatabase)
            )
            Single.zip(entryCountList) { args -> listOf(args) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ responseList ->
                    val response = responseList[0]
                    StandardModal(
                        this,
                        modalType = StandardModal.ModalType.STANDARD,
                        titleText = getString(R.string.std_modal_staging_database_count_title),
                        bodyText = String.format(getString(R.string.std_modal_staging_database_count_body), response[0] as Int, response[1] as Int),
                        positiveText = getString(R.string.std_modal_ok),
                        dialogFinishedListener = object : StandardModal.DialogFinishedListener {
                            override fun onPositive(password: String) { }
                            override fun onNegative() { }
                            override fun onNeutral() { }
                            override fun onBackPressed() { }
                        }
                    ).show(supportFragmentManager, "MODAL")
                }, { response -> val c = response })
            true
        }
        R.id.network_status -> {
            refreshDatabase()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        networkStatusMenuItem = menu.findItem(R.id.network_status)
        setToolbarNetworkStatus()
        return true
    }

}