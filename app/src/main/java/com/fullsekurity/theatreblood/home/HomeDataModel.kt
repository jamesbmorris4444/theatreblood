package com.fullsekurity.theatreblood.home

import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.logger.LogUtils.TagFilter.ANX
import com.fullsekurity.theatreblood.repository.network.api.APIInterface
import com.fullsekurity.theatreblood.repository.network.api.APIClient
import com.fullsekurity.theatreblood.repository.storage.datamodel.Donor
import com.fullsekurity.theatreblood.utils.Constants.API_KEY
import com.fullsekurity.theatreblood.utils.Constants.LANGUAGE
import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HomeDataModel : Callback<ArrayList<Donor>> {

    private val TAG = HomeDataModel::class.java.simpleName
    private lateinit var donorList: ArrayList<Donor>
    private val homeDataObjectObservable: ReplaySubject<HomeDataObject> = ReplaySubject.create()
    private val moviesService: APIInterface = APIClient.client
    private lateinit var homeDataObject: HomeDataObject

    fun loadData() {
        val rand = Random(System.currentTimeMillis())
        val page = rand.nextInt(500)
        val callBack: Call<ArrayList<Donor>> = moviesService.getDonors(API_KEY, LANGUAGE, page)
        callBack.enqueue(this)
    }

    override fun onResponse(call: Call<ArrayList<Donor>>, response: Response<ArrayList<Donor>>) {
        if (response.isSuccessful) {
            response.body()?.let {
                donorList = it
                storeHomeDataObject()
            }
        } else {
            LogUtils.W(TAG, LogUtils.FilterTags.withTags(ANX), String.format("RetroFit response error: %s", response.errorBody().toString()))
        }
    }

    override fun onFailure(call: Call<ArrayList<Donor>>, t: Throwable) {
        t.message?.let { LogUtils.E(LogUtils.FilterTags.withTags(ANX), it, t) }
    }

    fun storeHomeDataObject() {
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