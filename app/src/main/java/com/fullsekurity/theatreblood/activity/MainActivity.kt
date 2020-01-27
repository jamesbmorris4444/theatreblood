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
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.fullsekurity.theatreblood.donateproducts.DonateProductsListViewModel
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.logger.LogUtils.TagFilter.LOT
import com.fullsekurity.theatreblood.managedonor.DonorFragment
import com.fullsekurity.theatreblood.reassociateproducts.ReassociateProductsFragment
import com.fullsekurity.theatreblood.reassociateproducts.ReassociateProductsListViewModel
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.Constants.ROOT_FRAGMENT_TAG
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.Utils
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import com.fullsekurity.theatreblood.viewdonorlist.ViewDonorListFragment
import com.fullsekurity.theatreblood.viewdonorlist.ViewDonorListListViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject


class MainActivity : AppCompatActivity(), Callbacks, NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {

    private val tag = MainActivity::class.java.simpleName

    lateinit var repository: Repository
    @Inject
    lateinit var uiViewModel: UIViewModel

    private var networkStatusMenuItem: MenuItem? = null
    private lateinit var navView: ConstraintLayout
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

    lateinit var currentTheme: UITheme

    override fun onResume() {
        super.onResume()
        setupRepositoryDatabase()
        setupToolbar()
        setToolbarNetworkStatus()
        uiViewModel.lottieAnimation(lottieBackgroundView, uiViewModel.backgroundLottieJsonFileName, LottieDrawable.INFINITE)
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
        activityMainBinding.mainActivity = this
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        navView = findViewById(R.id.bottom_nav_view)
        navDrawerView = findViewById(R.id.nav_drawer_view)
        navDrawerView.setNavigationItemSelectedListener(this)
        drawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.addDrawerListener(this)
        lottieBackgroundView = activityMainBinding.root.findViewById(R.id.main_background_lottie)
        setupLottieForBottomNavigationBar()
        onDonationsClicked()
        val settings = getSharedPreferences("THEME", Context.MODE_PRIVATE)
        val name: String? = settings.getString("THEME", UITheme.LIGHT.name)
        if (name != null) {
            currentTheme = UITheme.valueOf(name)
        }
    }

    private fun setupLottieForBottomNavigationBar() {
        val task1: LottieTask<LottieComposition> = LottieCompositionFactory.fromRawRes(this, R.raw.transfusions)
        val lottieDrawable1 = LottieDrawable()
        task1.addListener { result ->
            lottieDrawable1.composition = result
            lottieDrawable1.repeatCount = LottieDrawable.INFINITE
            lottieDrawable1.playAnimation()
            lottieDrawable1.scale = 0.20f
            val lottieView1 = navView.findViewById<LottieAnimationView>(R.id.nav_bar_image_1)
            lottieView1.setImageDrawable(lottieDrawable1)
        }
        task1.addFailureListener { result ->
            LogUtils.E(LogUtils.FilterTags.withTags(LOT), "Lottie Drawable Failure", result)
        }

        val task2: LottieTask<LottieComposition> = LottieCompositionFactory.fromRawRes(this, R.raw.donations)
        val lottieDrawable2 = LottieDrawable()
        task2.addListener { result ->
            lottieDrawable2.composition = result
            lottieDrawable2.repeatCount = LottieDrawable.INFINITE
            lottieDrawable2.playAnimation()
            lottieDrawable2.scale = 0.20f
            val lottieView2 = navView.findViewById<LottieAnimationView>(R.id.nav_bar_image_2)
            lottieView2.setImageDrawable(lottieDrawable2)
        }
        task2.addFailureListener { result ->
            LogUtils.E(LogUtils.FilterTags.withTags(LOT), "Lottie Drawable Failure", result)
        }

        val task3: LottieTask<LottieComposition> = LottieCompositionFactory.fromRawRes(this, R.raw.inventory)
        val lottieDrawable3 = LottieDrawable()
        task3.addListener { result ->
            lottieDrawable3.composition = result
            lottieDrawable3.repeatCount = LottieDrawable.INFINITE
            lottieDrawable3.playAnimation()
            lottieDrawable3.scale = 0.25f
            val lottieView3 = navView.findViewById<LottieAnimationView>(R.id.nav_bar_image_3)
            lottieView3.setImageDrawable(lottieDrawable3)
        }
        task3.addFailureListener { result ->
            LogUtils.E(LogUtils.FilterTags.withTags(LOT), "Lottie Drawable Failure", result)
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
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.main_activity_container, DonateProductsFragment.newInstance(transitionToCreateDonation))
            .commitAllowingStateLoss()
    }

    private fun loadReassociateProductsFragment() {
        reassociateProductsFragment = ReassociateProductsFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.main_activity_container, reassociateProductsFragment)
            .commitAllowingStateLoss()
    }

    private fun loadViewDonorListFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.main_activity_container, ViewDonorListFragment.newInstance())
            .commitAllowingStateLoss()
    }

    fun loadDonorFragment(donor: Donor?, transitionToCreateDonation: Boolean) {
        if (donor == null) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(R.id.main_activity_container, DonorFragment.newInstance(Donor(), transitionToCreateDonation))
                .addToBackStack(ROOT_FRAGMENT_TAG)
                .commitAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(R.id.main_activity_container, DonorFragment.newInstance(donor, transitionToCreateDonation))
                .addToBackStack(ROOT_FRAGMENT_TAG)
                .commitAllowingStateLoss()
        }
    }

    fun loadCreateProductsFragment(donor: Donor) {
        supportFragmentManager.popBackStack(ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.main_activity_container, CreateProductsFragment.newInstance(donor))
            .addToBackStack(ROOT_FRAGMENT_TAG)
            .commitAllowingStateLoss()
    }

    fun reassociateOnNewDonorClicked(view: View) {
        transitionToCreateDonation = false
        loadDonorFragment(null, transitionToCreateDonation)
    }

    private fun setupToolbar() {
        supportActionBar?.let { actionBar ->
            actionBar.setBackgroundDrawable(ColorDrawable(Color.parseColor(uiViewModel.primaryColor)))
            colorizeToolbarOverflowButton(toolbar, Color.parseColor(uiViewModel.toolbarTextColor))
            val upArrow = ContextCompat.getDrawable(this, R.drawable.toolbar_back_arrow)
            actionBar.setHomeAsUpIndicator(upArrow);
            toolbar.setTitleTextColor(Color.parseColor(uiViewModel.toolbarTextColor))
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

    fun onDonationsClicked() {
        supportFragmentManager.popBackStack(ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        transitionToCreateDonation = true
        loadDonateProductsFragment(transitionToCreateDonation)
    }

    fun onTransfusionsClicked() {
    }

    fun onInventoryClicked() {
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

    override fun onDrawerOpened(view: View) {
        Utils.hideKeyboard(view)
    }

    override fun onDrawerClosed(view: View) { }
    override fun onDrawerSlide(view: View, float: Float) { }
    override fun onDrawerStateChanged(state: Int) { }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        networkStatusMenuItem = menu.findItem(R.id.network_status)
        setToolbarNetworkStatus()
        return true
    }

    // Start Barcode Scanner handler

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

    // End Barcode Scanner handler

    override fun fetchActivity(): MainActivity {
        return this
    }

    override fun fetchRootView(): View {
        return activityMainBinding.root
    }

    override fun fetchRadioButton(resId:Int): RadioButton? { return null }
    override fun fetchDropdown(resId: Int) : Spinner? { return null }
    override fun fetchCreateProductsListViewModel() : CreateProductsListViewModel? { return null }
    override fun fetchDonateProductsListViewModel() : DonateProductsListViewModel? { return null }
    override fun fetchReassociateProductsListViewModel() : ReassociateProductsListViewModel? { return null }
    override fun fetchViewDonorListViewModel() : ViewDonorListListViewModel? { return null }

}