package com.fullsekurity.theatreblood.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.FragmentManager
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.ActivityCallbacks
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.modal.StandardModal
import com.fullsekurity.theatreblood.repository.network.APIClient
import com.fullsekurity.theatreblood.repository.network.APIInterface
import com.fullsekurity.theatreblood.repository.storage.BloodDatabase
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.repository.storage.DonorWithProducts
import com.fullsekurity.theatreblood.repository.storage.Product
import com.fullsekurity.theatreblood.utils.Constants
import com.fullsekurity.theatreblood.utils.Constants.MAIN_DATABASE_NAME
import com.fullsekurity.theatreblood.utils.Constants.MODIFIED_DATABASE_NAME
import com.fullsekurity.theatreblood.utils.Utils
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class Repository(private val activityCallbacks: ActivityCallbacks) {

    private val TAG = Repository::class.java.simpleName
    lateinit var mainBloodDatabase: BloodDatabase
    lateinit var stagingBloodDatabase: BloodDatabase
    private val donorsService: APIInterface = APIClient.client
    private var disposable: Disposable? = null
    private var transportType = TransportType.NONE
    private var isMetered: Boolean = false
    private var cellularNetwork: Network? = null
    private var wiFiNetwork: Network? = null
    var isOfflineMode = true

    fun setBloodDatabase(context: Context) {
        mainBloodDatabase = BloodDatabase.newInstance(context, MAIN_DATABASE_NAME)
        stagingBloodDatabase = BloodDatabase.newInstance(context, MODIFIED_DATABASE_NAME)
    }

    fun onCleared() {
        disposable?.let {
            it.dispose()
            disposable = null
        }
    }

    // The code below here manages the network status

    private enum class TransportType {
        NONE,
        CELLULAR,
        WIFI,
        BOTH
    }

    init {
        val connectivityManager = activityCallbacks.fetchActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder: NetworkRequest.Builder = NetworkRequest.Builder()
        connectivityManager.registerNetworkCallback(
            builder.build(),
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    onAvailableHelper(connectivityManager, network)
                }

                override fun onLost(network: Network) {
                    onLostHelper()
                }
            }
        )
    }

    private fun onAvailableHelper(connectivityManager: ConnectivityManager, network: Network) {
        isOfflineMode = false
        setConnectedTransportType(connectivityManager, network)
        isMetered = connectivityManager.isActiveNetworkMetered
        LogUtils.W(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.NET), String.format("Network is connected, TYPE: %s (metered=%b)", transportType.name, isMetered))
    }

    private fun onLostHelper() {
        isOfflineMode = false
        setDisconnectedTransportType()
        isMetered = false
        LogUtils.W(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.NET), String.format("Network connectivity is lost, TYPE: %s (metered=%b)", transportType.name, isMetered))
    }


    private fun setConnectedTransportType(connectivityManager: ConnectivityManager, network: Network) {
        when (transportType) {
            TransportType.NONE -> {
                connectivityManager.getNetworkCapabilities(network)?.let { networkCapabiities ->
                    if (networkCapabiities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        wiFiNetwork = network
                        transportType = TransportType.WIFI
                        activityCallbacks.fetchActivity().setToolbarNetworkStatus()
                    } else if (networkCapabiities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        cellularNetwork = network
                        transportType = TransportType.CELLULAR
                        activityCallbacks.fetchActivity().setToolbarNetworkStatus()
                    }
                }
            }
            TransportType.WIFI -> {
                connectivityManager.getNetworkCapabilities(network)?.let { networkCapabiities ->
                    if (networkCapabiities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        cellularNetwork = network
                        transportType = TransportType.BOTH
                        activityCallbacks.fetchActivity().setToolbarNetworkStatus()
                    }
                }
            }
            TransportType.CELLULAR -> {
                connectivityManager.getNetworkCapabilities(network)?.let { networkCapabiities ->
                    if (networkCapabiities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        wiFiNetwork = network
                        transportType = TransportType.BOTH
                        activityCallbacks.fetchActivity().setToolbarNetworkStatus()
                    }
                }

            }
            TransportType.BOTH -> { }
        }
    }

    private fun setDisconnectedTransportType() {
        when (transportType) {
            TransportType.NONE -> { }
            TransportType.WIFI, TransportType.CELLULAR -> {
                transportType = TransportType.NONE
                activityCallbacks.fetchActivity().setToolbarNetworkStatus()
            }
            TransportType.BOTH -> {
                val connectivityManager = activityCallbacks.fetchActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                connectivityManager.getNetworkCapabilities(wiFiNetwork)?.let { networkCapabiities ->
                    if (networkCapabiities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        transportType = TransportType.WIFI
                        activityCallbacks.fetchActivity().setToolbarNetworkStatus()
                    }
                }
                if (transportType == TransportType.BOTH) {
                    transportType = TransportType.CELLULAR
                    activityCallbacks.fetchActivity().setToolbarNetworkStatus()
                }
                activityCallbacks.fetchActivity().setToolbarNetworkStatus()
            }
        }
    }

    fun getNetworkStatusResInt(): Int {
        when (transportType) {
            TransportType.NONE -> {
                return R.drawable.ic_network_status_none
            }
            TransportType.CELLULAR -> {
                return R.drawable.ic_network_status_cellular
            }
            TransportType.BOTH, TransportType.WIFI -> {
                return R.drawable.ic_network_status_wifi
            }
        }
    }

    // The code below here refreshes the main data base

    fun refreshDatabase(progressBar: ProgressBar, activity: MainActivity) {
        saveDatabase(activity, MAIN_DATABASE_NAME)
        deleteDatabase(activity, MAIN_DATABASE_NAME)
        disposable = donorsService.getDonors(Constants.API_KEY, Constants.LANGUAGE, 13)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .timeout(15L, TimeUnit.SECONDS)
            .subscribe ({ donorResponse ->
                progressBar.visibility = View.GONE
                initializeDataBase(donorResponse.results, donorResponse.products, activity)
            },
            {
                throwable -> initializeDatabaseFailureModal(activity, throwable.message)
            })
    }

    private fun initializeDataBase(donors: List<Donor>, products: List<List<Product>>, activity: MainActivity) {
        for (donorIndex in donors.indices) {
            for (productIndex in products[donorIndex].indices) {
                products[donorIndex][productIndex].donorId = donors[donorIndex].id
            }
        }
        insertDonorsIntoLocalDatabase(mainBloodDatabase, donors)
        insertProductsIntoLocalDatabase(mainBloodDatabase, products)
        StandardModal(
            activity,
            modalType = StandardModal.ModalType.STANDARD,
            titleText = activity.getString(R.string.std_modal_refresh_success_title),
            bodyText = String.format(activity.getString(R.string.std_modal_refresh_success_body, activity.getDatabasePath(MAIN_DATABASE_NAME))),
            positiveText = activity.getString(R.string.std_modal_ok),
            dialogFinishedListener = object : StandardModal.DialogFinishedListener {
                override fun onPositive(string: String) { }
                override fun onNegative() { }
                override fun onNeutral() { }
                override fun onBackPressed() { }
            }
        ).show(activity.supportFragmentManager, "MODAL")
    }

    private fun insertDonorsIntoLocalDatabase(database: BloodDatabase, donors: List<Donor>) {
        database.databaseDao().insertLocalDonors(donors)
    }

    private fun insertProductsIntoLocalDatabase(database: BloodDatabase, products: List<List<Product>>) {
        for (index in products.indices) {
            database.databaseDao().insertProducts(products[index])
        }
    }

    private fun initializeDatabaseFailureModal(activity: MainActivity, errorMessage: String?) {
        var error = errorMessage
        if (error == null) {
            error = "App cannot continue"
        }
        StandardModal(
            activity,
            modalType = StandardModal.ModalType.STANDARD,
            iconType = StandardModal.IconType.ERROR,
            titleText = activity.getString(R.string.std_modal_initialize_database_failure_title),
            bodyText = error,
            positiveText = activity.getString(R.string.std_modal_ok),
            dialogFinishedListener = object : StandardModal.DialogFinishedListener {
                override fun onPositive(string: String) {
                    activity.finishActivity()
                }
                override fun onNegative() { }
                override fun onNeutral() { }
                override fun onBackPressed() {
                    activity.finishActivity()
                }
            }
        ).show(activity.supportFragmentManager, "MODAL")
    }

    private fun deleteDatabase(context: Context, databaseName: String) {
        context.deleteDatabase(databaseName)
    }

    private fun saveDatabase(context: Context, databaseName: String) {
        val db = context.getDatabasePath(databaseName)
        val dbBackup = File(db.parent, databaseName+"_backup")
        if (db.exists()) {
            db.copyTo(dbBackup, true)
            LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("Path Name \"%s\" exists and was backed up", db.toString()))
        }
    }

    fun closeDatabase() {
        mainBloodDatabase.let { bloodDatabase ->
            if (bloodDatabase.isOpen) {
                bloodDatabase.close()
            }
        }
        stagingBloodDatabase.let { bloodDatabase ->
            if (bloodDatabase.isOpen) {
                bloodDatabase.close()
            }
        }
    }

    // The code below here does CRUD on the database

    fun insertDonorIntoDatabase(database: BloodDatabase, donor: Donor, transitionToCreateDonation: Boolean) {
        var disposable: Disposable? = null
        disposable = Completable.fromAction { database.databaseDao().insertDonor(donor) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                StandardModal(
                    activityCallbacks,
                    modalType = StandardModal.ModalType.STANDARD,
                    titleText = activityCallbacks.fetchActivity().getString(R.string.std_modal_insert_donor_staging_title),
                    bodyText = activityCallbacks.fetchActivity().getString(R.string.std_modal_insert_donor_staging_body),
                    positiveText = activityCallbacks.fetchActivity().getString(R.string.std_modal_ok),
                    dialogFinishedListener = object : StandardModal.DialogFinishedListener {
                        override fun onPositive(string: String) {
                            disposable?.dispose()
                            disposable = null
                            if (transitionToCreateDonation) {
                                activityCallbacks.fetchActivity().loadCreateProductsFragment(donor)
                            } else {
                                activityCallbacks.fetchActivity().onBackPressed()
                            }
                        }
                        override fun onNegative() { }
                        override fun onNeutral() { }
                        override fun onBackPressed() {
                            disposable?.dispose()
                            disposable = null
                            activityCallbacks.fetchActivity().onBackPressed()
                        }
                    }
                ).show(activityCallbacks.fetchActivity().supportFragmentManager, "MODAL")
            }
    }

    fun insertDonorAndProductsIntoDatabaseChained(database: BloodDatabase, donor: Donor, products: List<Product>) {
        var disposable: Disposable? = null
        disposable = Completable.fromAction { database.databaseDao().insertDonor(donor) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                disposable?.dispose()
                disposable = null
                insertProductsIntoDatabase(database, products)
            }
    }

    private fun insertProductsIntoDatabase(database: BloodDatabase, products: List<Product>) {
        var disposable: Disposable? = null
        disposable = Completable.fromAction { database.databaseDao().insertProducts(products) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                StandardModal(
                    activityCallbacks,
                    modalType = StandardModal.ModalType.STANDARD,
                    titleText = activityCallbacks.fetchActivity().getString(R.string.std_modal_insert_products_staging_title),
                    bodyText = activityCallbacks.fetchActivity().getString(R.string.std_modal_insert_products_staging_body),
                    positiveText = activityCallbacks.fetchActivity().getString(R.string.std_modal_ok),
                    dialogFinishedListener = object : StandardModal.DialogFinishedListener {
                        override fun onPositive(string: String) {
                            disposable?.dispose()
                            disposable = null
                            activityCallbacks.fetchActivity().supportFragmentManager.popBackStack(Constants.ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            activityCallbacks.fetchActivity().loadDonateProductsFragment(true)
                        }
                        override fun onNegative() { }
                        override fun onNeutral() { }
                        override fun onBackPressed() {
                            disposable?.dispose()
                            disposable = null
                            activityCallbacks.fetchActivity().supportFragmentManager.popBackStack(Constants.ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            activityCallbacks.fetchActivity().loadDonateProductsFragment(true)
                        }
                    }
                ).show(activityCallbacks.fetchActivity().supportFragmentManager, "MODAL")
            }
    }

    fun insertReassociatedProductsIntoDatabase(database: BloodDatabase, products: List<Product>, initializeView: () -> Unit) {
        var disposable: Disposable? = null
        disposable = Completable.fromAction { database.databaseDao().insertProducts(products) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                StandardModal(
                    activityCallbacks,
                    modalType = StandardModal.ModalType.STANDARD,
                    titleText = activityCallbacks.fetchActivity().getString(R.string.std_modal_insert_products_staging_title),
                    bodyText = activityCallbacks.fetchActivity().getString(R.string.std_modal_insert_products_staging_body),
                    positiveText = activityCallbacks.fetchActivity().getString(R.string.std_modal_ok),
                    dialogFinishedListener = object : StandardModal.DialogFinishedListener {
                        override fun onPositive(string: String) {
                            initializeView()
                        }
                        override fun onNegative() { }
                        override fun onNeutral() { }
                        override fun onBackPressed() {
                            initializeView()
                        }
                    }
                ).show(activityCallbacks.fetchActivity().supportFragmentManager, "MODAL")
            }
    }

    private fun donorsFromFullName(database: BloodDatabase, search: String): Single<List<Donor>> {
        var searchLast: String
        var searchFirst = "%"
        val index = search.indexOf(',')
        if (index < 0) {
            searchLast = "%$search%"
        } else {
            val last = search.substring(0, index)
            val first = search.substring(index + 1)
            searchFirst = "%$first%"
            searchLast = "%$last%"
        }
        return database.databaseDao().donorsFromFullName(searchLast, searchFirst)
    }

    private fun donorsFromFullNameWithProducts(database: BloodDatabase, search: String): Single<List<DonorWithProducts>> {
        var searchLast: String
        var searchFirst = "%"
        val index = search.indexOf(',')
        if (index < 0) {
            searchLast = "%$search%"
        } else {
            val last = search.substring(0, index)
            val first = search.substring(index + 1)
            searchFirst = "%$first%"
            searchLast = "%$last%"
        }
        return database.databaseDao().donorsFromFullNameWithProducts(searchLast, searchFirst)
    }
    
    fun databaseCounts() {
        var disposable: Disposable? = null
        val entryCountList = listOf(
            databaseDonorCount(stagingBloodDatabase),
            databaseDonorCount(mainBloodDatabase)
        )
        disposable = Single.zip(entryCountList) { args -> listOf(args) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseList ->
                val response = responseList[0]
                getProductEntryCount(response[0] as Int, response[1] as Int)
            }, { response -> val c = response })
    }

    private fun getProductEntryCount(modifiedDonors: Int, mainDonors: Int) {
        var disposable: Disposable? = null
        val entryCountList = listOf(
            databaseProductCount(stagingBloodDatabase),
            databaseProductCount(mainBloodDatabase)
        )
        disposable = Single.zip(entryCountList) { args -> listOf(args) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseList ->
                val response = responseList[0]
                StandardModal(
                    activityCallbacks,
                    modalType = StandardModal.ModalType.STANDARD,
                    titleText = activityCallbacks.fetchActivity().getString(R.string.std_modal_staging_database_count_title),
                    bodyText = String.format(activityCallbacks.fetchActivity().getString(R.string.std_modal_staging_database_count_body), modifiedDonors, mainDonors, response[0] as Int, response[1] as Int),
                    positiveText = activityCallbacks.fetchActivity().getString(R.string.std_modal_ok),
                    dialogFinishedListener = object : StandardModal.DialogFinishedListener {
                        override fun onPositive(password: String) { }
                        override fun onNegative() { }
                        override fun onNeutral() { }
                        override fun onBackPressed() { }
                    }
                ).show(activityCallbacks.fetchActivity().supportFragmentManager, "MODAL")
            }, { response -> val c = response })
    }

    private fun databaseDonorCount(database: BloodDatabase): Single<Int> {
        return database.databaseDao().getDonorEntryCount()
    }

    private fun databaseProductCount(database: BloodDatabase): Single<Int> {
        return database.databaseDao().getProductEntryCount()
    }

    @Suppress("UNCHECKED_CAST")
    fun handleSearchClick(view: View, searchKey: String, showDonors: (donorList: List<Donor>) -> Unit) {
        var disposable: Disposable? = null
        val fullNameResponseList = listOf(
            donorsFromFullName(mainBloodDatabase, searchKey),
            donorsFromFullName(stagingBloodDatabase, searchKey)
        )
        disposable = Single.zip(fullNameResponseList) { args -> listOf(args) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ responseList ->
                val response = responseList[0]
                for (donor in response[0] as List<Donor>) {
                    LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("JIMX  mainm"))
                    if (donor.posterPath.length > 11) {
                        donor.posterPath = donor.posterPath.substring(1,11).toUpperCase(Locale.getDefault())
                    }
                    if (donor.releaseDate.isEmpty()) {
                        donor.releaseDate = "04 Jul 2019"
                    } else if (donor.releaseDate[4] == '-') {
                        val year: Int = donor.releaseDate.substring(0,4).toInt()
                        val monthOfYear = donor.releaseDate.substring(5,7).toInt()
                        val dayOfMonth = donor.releaseDate.substring(8,10).toInt()
                        val calendar = Calendar.getInstance()
                        calendar.set(year, monthOfYear, dayOfMonth)
                        val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.US)
                        donor.releaseDate = dateFormatter.format(calendar.time)
                    }
                }
                for (donor in response[1] as List<Donor>) {
                    if (donor.posterPath.length > 11) {
                        donor.posterPath = donor.posterPath.substring(1,11).toUpperCase(Locale.getDefault())
                    }
                    if (donor.releaseDate.isEmpty()) {
                        donor.releaseDate = "04 Jul 2019"
                    } else if (donor.releaseDate[4] == '-') {
                        val year: Int = donor.releaseDate.substring(0,4).toInt()
                        val monthOfYear = donor.releaseDate.substring(5,7).toInt()
                        val dayOfMonth = donor.releaseDate.substring(8,10).toInt()
                        val calendar = Calendar.getInstance()
                        calendar.set(year, monthOfYear, dayOfMonth)
                        val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.US)
                        donor.releaseDate = dateFormatter.format(calendar.time)
                    }
                }
                val stagingDatabaseList = response[1] as List<Donor>
                val mainDatabaseList = response[0] as List<Donor>
                val newList = stagingDatabaseList.union(mainDatabaseList).distinctBy { donor -> Utils.donorUnionStringForDistinctBy(donor) }
                showDonors(newList)
                Utils.hideKeyboard(view)
            }, { response -> val c = response })
    }

    fun handleReassociateNewDonorClick(view: View) {

    }

    fun handleReassociateSearchClick(view: View, searchKey: String, showDonorsAndProducts: (donorsAndProductsList: List<DonorWithProducts>) -> Unit) {
        var disposable: Disposable? = null
        disposable = donorsFromFullNameWithProducts(stagingBloodDatabase, searchKey)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe{ donorWithProducts ->
                for (index in donorWithProducts.indices) {
                    transformDonorData(donorWithProducts[index].donor)
                }
                showDonorsAndProducts(donorWithProducts)
            }

    }

    private fun transformDonorData(donorResponse: Donor) {
        if (donorResponse.posterPath.length > 11) {
            donorResponse.posterPath = donorResponse.posterPath.substring(1,11).toUpperCase(Locale.getDefault())
        }
        if (donorResponse.releaseDate.isEmpty()) {
            donorResponse.releaseDate = "04 Jul 2019"
        } else if (donorResponse.releaseDate[4] == '-') {
            val year: Int = donorResponse.releaseDate.substring(0,4).toInt()
            val monthOfYear = donorResponse.releaseDate.substring(5,7).toInt()
            val dayOfMonth = donorResponse.releaseDate.substring(8,10).toInt()
            val calendar = Calendar.getInstance()
            calendar.set(year, monthOfYear, dayOfMonth)
            val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.US)
            donorResponse.releaseDate = dateFormatter.format(calendar.time)
        }
    }

    fun getAllNewProductsForDonor(donor: Donor, showProducts: (productList: List<Product>) -> Unit) {
        var disposable: Disposable? = null
        disposable = donorsFromNameAndDateWithProducts(stagingBloodDatabase, donor)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe{ donorWithProducts ->
                val donorResponse = donorWithProducts.donor
                if (donorResponse.posterPath.length > 11) {
                    donorResponse.posterPath = donorResponse.posterPath.substring(1,11).toUpperCase(Locale.getDefault())
                }
                if (donorResponse.releaseDate.isEmpty()) {
                    donorResponse.releaseDate = "04 Jul 2019"
                } else if (donorResponse.releaseDate[4] == '-') {
                    val year: Int = donorResponse.releaseDate.substring(0,4).toInt()
                    val monthOfYear = donorResponse.releaseDate.substring(5,7).toInt()
                    val dayOfMonth = donorResponse.releaseDate.substring(8,10).toInt()
                    val calendar = Calendar.getInstance()
                    calendar.set(year, monthOfYear, dayOfMonth)
                    val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.US)
                    donorResponse.releaseDate = dateFormatter.format(calendar.time)
                }
                showProducts(donorWithProducts.products)
            }
                
    }

    private fun donorsFromNameAndDateWithProducts(database: BloodDatabase, donor: Donor): Single<DonorWithProducts> {
        return database.databaseDao().donorsFromNameAndDateWithProducts(donor.title, donor.posterPath, donor.voteCount.toString(), donor.releaseDate)
    }

//    fun insertProductList(database: BloodDatabase, products: List<Product>) {
//        disposable = Completable.fromAction { database.databaseDao().insertProducts(products) }
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe {
//                StandardModal(
//                    activityCallbacks,
//                    modalType = StandardModal.ModalType.STANDARD,
//                    titleText = activityCallbacks.fetchActivity().getString(R.string.std_modal_insert_products_staging_title),
//                    bodyText = activityCallbacks.fetchActivity().getString(R.string.std_modal_insert_products_staging_body),
//                    positiveText = activityCallbacks.fetchActivity().getString(R.string.std_modal_ok),
//                    dialogFinishedListener = object : StandardModal.DialogFinishedListener {
//                        override fun onPositive(string: String) {
//                            disposable?.dispose()
//                            disposable = null
//                            activityCallbacks.fetchActivity().supportFragmentManager.popBackStack(Constants.ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//                            activityCallbacks.fetchActivity().loadDonateProductsFragment()
//                        }
//                        override fun onNegative() { }
//                        override fun onNeutral() { }
//                        override fun onBackPressed() {
//                            disposable?.dispose()
//                            disposable = null
//                            activityCallbacks.fetchActivity().supportFragmentManager.popBackStack(Constants.ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//                            activityCallbacks.fetchActivity().loadDonateProductsFragment()
//                        }
//                    }
//                ).show(activityCallbacks.fetchActivity().supportFragmentManager, "MODAL")
//            }
//    }

}