package com.fullsekurity.theatreblood.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import com.airbnb.lottie.*
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.barcode.BarCodeScannerActivity
import com.fullsekurity.theatreblood.createproducts.CreateProductsFragment
import com.fullsekurity.theatreblood.createproducts.CreateProductsListViewModel
import com.fullsekurity.theatreblood.databinding.ActivityMainBinding
import com.fullsekurity.theatreblood.donateproducts.DonateProductsFragment
import com.fullsekurity.theatreblood.donor.DonorFragment
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.logger.LogUtils.TagFilter.ANX
import com.fullsekurity.theatreblood.reassociateproducts.ReassociateProductsFragment
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.Constants.ROOT_FRAGMENT_TAG
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import com.fullsekurity.theatreblood.viewdonorlist.ViewDonorListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject


class MainActivity : AppCompatActivity(), ActivityCallbacks, NavigationView.OnNavigationItemSelectedListener {

    private val tag = MainActivity::class.java.simpleName

    lateinit var repository: Repository
    @Inject
    lateinit var uiViewModel: UIViewModel

    private var networkStatusMenuItem: MenuItem? = null
    private lateinit var navView: BottomNavigationView
    private lateinit var navDrawerView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var lottieBackgroundView: LottieAnimationView
    private lateinit var activityMainBinding: ActivityMainBinding
    lateinit var createProductsListViewModel: CreateProductsListViewModel
    private lateinit var reassociateProductsFragment: ReassociateProductsFragment
    var transitionToCreateDonation = true

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
        uiViewModel.lottieAnimation(lottieBackgroundView, uiViewModel.backgroundLottieJsonFileName, LottieDrawable.INFINITE)
    }

    override fun onDestroy() {
        super.onDestroy()
        repository.closeDatabase()
        repository.onCleared()
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_donations -> {
                supportFragmentManager.popBackStack(ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                transitionToCreateDonation = true
                loadDonateProductsFragment(transitionToCreateDonation)
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
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        activityMainBinding.uiViewModel = uiViewModel
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        navView = findViewById(R.id.bottom_nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        navDrawerView = findViewById(R.id.nav_drawer_view)
        navDrawerView.setNavigationItemSelectedListener(this)
        drawerLayout = findViewById(R.id.drawer_layout)
        lottieBackgroundView = activityMainBinding.root.findViewById(R.id.main_background_lottie)
        setupLottieForBottomNavigationBar()
        navView.selectedItemId = R.id.navigation_donations
    }

    private fun setupLottieForBottomNavigationBar() {
        setupMenuItemLottieAnimation(0, R.raw.donations)
        setupMenuItemLottieAnimation(1, R.raw.transfusions)
        setupMenuItemLottieAnimation(2, R.raw.inventory)
    }

    private fun setupMenuItemLottieAnimation(position: Int, resInt: Int) {
        val task: LottieTask<LottieComposition> = LottieCompositionFactory.fromRawRes(this, resInt)
        val lottieDrawable = LottieDrawable()
        task.addListener { result ->
            lottieDrawable.composition = result
            lottieDrawable.repeatCount = LottieDrawable.INFINITE
            lottieDrawable.scale = 2.0f
            lottieDrawable.playAnimation()
            navView.menu.getItem(position).icon = lottieDrawable
        }
        task.addFailureListener { result ->
            LogUtils.E(LogUtils.FilterTags.withTags(ANX), "Lottie Drawable Failure", result)
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

    fun loadDonateProductsFragment(transitionToCreateDonation: Boolean) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
            .replace(R.id.main_activity_container, DonateProductsFragment.newInstance(transitionToCreateDonation))
            .commitAllowingStateLoss()
    }

    private fun loadReassociateProductsFragment() {
        reassociateProductsFragment = ReassociateProductsFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
            .replace(R.id.main_activity_container, reassociateProductsFragment)
            .commitAllowingStateLoss()
    }

    private fun loadViewDonorListFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
            .replace(R.id.main_activity_container, ViewDonorListFragment.newInstance())
            .commitAllowingStateLoss()
    }

    fun loadDonorFragment(donor: Donor?, transitionToCreateDonation: Boolean) {
        if (donor == null) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.main_activity_container, DonorFragment.newInstance(Donor(), transitionToCreateDonation))
                .addToBackStack(ROOT_FRAGMENT_TAG)
                .commitAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.main_activity_container, DonorFragment.newInstance(donor, transitionToCreateDonation))
                .addToBackStack(ROOT_FRAGMENT_TAG)
                .commitAllowingStateLoss()
        }
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
            navView.itemBackground = ColorDrawable(Color.parseColor(uiViewModel.primaryColor))
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
        R.id.action_toggle_theme -> {
            if (currentTheme == UITheme.LIGHT) {
                currentTheme = UITheme.DARK
            } else {
                currentTheme = UITheme.LIGHT
            }
            uiViewModel.currentTheme = currentTheme
            uiViewModel.lottieAnimation(lottieBackgroundView, uiViewModel.backgroundLottieJsonFileName, LottieDrawable.INFINITE)
            setupToolbar()
            true
        }
        R.id.action_settings -> {
            true
        }
        R.id.action_favorite -> {
            true
        }
        R.id.action_staging_count -> {
            repository.databaseCounts()
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.donate_products -> {
                supportFragmentManager.popBackStack(ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                transitionToCreateDonation = true
                loadDonateProductsFragment(transitionToCreateDonation)
            }
            R.id.manage_donor -> {
                supportFragmentManager.popBackStack(ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                transitionToCreateDonation = false
                loadDonateProductsFragment(transitionToCreateDonation)
            }
            R.id.reassociate_donation -> {
                supportFragmentManager.popBackStack(ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                loadReassociateProductsFragment()
            }
            R.id.update_test_results -> {
            }
            R.id.view_donor_list -> {
                supportFragmentManager.popBackStack(ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                loadViewDonorListFragment()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        networkStatusMenuItem = menu.findItem(R.id.network_status)
        setToolbarNetworkStatus()
        return true
    }

    // CreateProducts handlers

    fun onCreateProductsDeleteClicked(view: View) {
        createProductsListViewModel.onCreateProductsDeleteClicked(view)
    }

    fun onCreateProductsEditClicked(view: View) {
        createProductsListViewModel.onCreateProductsEditClicked(view)
    }

    // Reassociate Products handlers

    fun reassociateOnTextNameChanged(key: String) {
        reassociateProductsFragment.reassociateProductsListViewModel.onTextNameChanged(key, 0, 0, 0)
    }

    fun reassociateOnSearchClicked(view: View) {
        reassociateProductsFragment.reassociateProductsListViewModel.handleReassociateSearchClick(view)
    }

    fun reassociateOnNewDonorClicked(view: View) {
        loadDonorFragment(null, transitionToCreateDonation)
    }

    fun reassociateIncorrectDonorClicked(donor: Donor) {
        reassociateProductsFragment.reassociateProductsListViewModel.handleReassociateIncorrectDonorClick(donor)
    }

    fun barcodeScanner(gridNumber: Int) {
        val intent = Intent(this, BarCodeScannerActivity::class.java)
        startActivityForResult(intent, 100 + gridNumber)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val barcodeResult = data?.getStringExtra("rawResult")
            when (requestCode) {
                111 -> {
                    createProductsListViewModel.editTextProductDin.set(barcodeResult)
                }
                121 -> {
                    createProductsListViewModel.editTextProductCode.set(barcodeResult)
                }
                122 -> {
                    createProductsListViewModel.editTextProductExpDate.set(barcodeResult)
                }
                else -> {}
            }
        }
    }

    override fun fetchActivity(): MainActivity {
        return this
    }

    override fun fetchRootView(): View {
        return activityMainBinding.root
    }

    override fun fetchRadioButton(resId:Int): RadioButton? {
        return null
    }

}