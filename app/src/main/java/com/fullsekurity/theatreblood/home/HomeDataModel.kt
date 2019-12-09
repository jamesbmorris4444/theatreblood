package com.fullsekurity.theatreblood.home

import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.logger.LogUtils.TagFilter.ANX
import com.fullsekurity.theatreblood.repository.network.APIClient
import com.fullsekurity.theatreblood.repository.network.APIInterface
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.utils.Constants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject
import java.util.*

class HomeDataModel {

    private val TAG = HomeDataModel::class.java.simpleName
    private lateinit var donorList: List<Donor>
    private val homeDataObjectObservable: ReplaySubject<HomeDataObject> = ReplaySubject.create()
    private val donorsService: APIInterface = APIClient.client
    private lateinit var homeDataObject: HomeDataObject
    private var disposable: Disposable? = null

    fun loadData() {
        disposable = donorsService.getDonors(Constants.API_KEY, Constants.LANGUAGE,"popularity.desc", "false", "false", 1)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({
                donorList = it.results
                storeHomeDataObject()
            },
            {
                throwable -> LogUtils.E(LogUtils.FilterTags.withTags(ANX), "Exception getting Donor in HomeDataModel", throwable)
            })

    }

    private fun storeHomeDataObject() {
        val rand = Random(System.currentTimeMillis())
        val donor: Donor? = donorList?.let { it[rand.nextInt(20)] }
        if (donor != null) {
            homeDataObject = HomeDataObject(donor.title, donor.releaseDate, donor.posterPath)
            homeDataObjectObservable.onNext(homeDataObject)
        }
    }

    fun getHomeDataObject(): Observable<HomeDataObject> {
        return homeDataObjectObservable
    }

}