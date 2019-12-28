package com.fullsekurity.theatreblood.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.view.View
import android.widget.ProgressBar
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.ActivityCallbacks
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.donors.Donor
import com.fullsekurity.theatreblood.donors.Product
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.modal.StandardModal
import com.fullsekurity.theatreblood.repository.network.APIClient
import com.fullsekurity.theatreblood.repository.network.APIInterface
import com.fullsekurity.theatreblood.repository.storage.BloodDatabase
import com.fullsekurity.theatreblood.utils.Constants
import com.fullsekurity.theatreblood.utils.Constants.INSERTED_DATABASE_NAME
import com.fullsekurity.theatreblood.utils.Constants.MAIN_DATABASE_NAME
import com.fullsekurity.theatreblood.utils.Constants.MODIFIED_DATABASE_NAME
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.concurrent.TimeUnit


class Repository(private val activityCallbacks: ActivityCallbacks) {

    private val TAG = Repository::class.java.simpleName
    lateinit var mainBloodDatabase: BloodDatabase
    lateinit var modifiedBloodDatabase: BloodDatabase
    lateinit var insertedBloodDatabase: BloodDatabase
    private val donorsService: APIInterface = APIClient.client
    private var disposable: Disposable? = null
    private var transportType = TransportType.NONE
    private var isMetered: Boolean = false
    private var cellularNetwork: Network? = null
    private var wiFiNetwork: Network? = null
    var isOfflineMode = true

    fun setBloodDatabase(context: Context) {
        mainBloodDatabase = BloodDatabase.newInstance(context, MAIN_DATABASE_NAME)
        modifiedBloodDatabase = BloodDatabase.newInstance(context, MODIFIED_DATABASE_NAME)
        insertedBloodDatabase = BloodDatabase.newInstance(context, INSERTED_DATABASE_NAME)
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

    private fun initializeDataBase(donors: List<Donor>, products: List<Product>, activity: MainActivity) {
        for (index in donors.indices) {
            products[index].donor_id = donors[index].id
            insertDonorIntoLocalDatabase(mainBloodDatabase, donors[index])
            insertProductIntoLocalDatabase(mainBloodDatabase, products[index])
        }
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

    private fun insertDonorIntoLocalDatabase(database: BloodDatabase, donor: Donor) {
        database.donorDao().insertLocalDonor(donor)
    }

    private fun insertProductIntoLocalDatabase(database: BloodDatabase, product: Product) {
        database.donorDao().insertLocalProduct(product)
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
        modifiedBloodDatabase.let { bloodDatabase ->
            if (bloodDatabase.isOpen) {
                bloodDatabase.close()
            }
        }
        insertedBloodDatabase.let { bloodDatabase ->
            if (bloodDatabase.isOpen) {
                bloodDatabase.close()
            }
        }
    }

    // The code below here does CRUD on the database

    fun insertIntoDatabase(database: BloodDatabase, donor: Donor) {
        var disposable: Disposable? = null
        disposable = Completable.fromAction { database.donorDao().insertDonor(donor) }
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
                            activityCallbacks.fetchActivity().loadCreateProductsFragment(donor)
                        }
                        override fun onNegative() { }
                        override fun onNeutral() { }
                        override fun onBackPressed() {
                            disposable?.dispose()
                            disposable = null
                            activityCallbacks.fetchActivity().loadCreateProductsFragment(donor)
                        }
                    }
                ).show(activityCallbacks.fetchActivity().supportFragmentManager, "MODAL")
            }
    }

    fun donorsFromFullName(database: BloodDatabase, search: String): Single<List<Donor>> {
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
        return database.donorDao().donorsFromFullName(searchLast, searchFirst)
    }

    fun databaseDonorCount(database: BloodDatabase): Single<Int> {
        return database.donorDao().getDonorEntryCount()
    }

    fun databaseProductCount(database: BloodDatabase): Single<Int> {
        return database.donorDao().getProductEntryCount()
    }

    fun insertProducts(database: BloodDatabase, products: List<Product>) {
//        disposable = Completable.fromAction { database.donorDao().insertProducts(products) }
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
//                        }
//                        override fun onNegative() { }
//                        override fun onNeutral() { }
//                        override fun onBackPressed() {
//                            disposable?.dispose()
//                            disposable = null
//                        }
//                    }
//                ).show(activityCallbacks.fetchActivity().supportFragmentManager, "MODAL")
//            }
    }

}