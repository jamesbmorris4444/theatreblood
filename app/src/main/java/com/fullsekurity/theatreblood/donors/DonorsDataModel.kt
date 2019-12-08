package com.fullsekurity.theatreblood.donors

import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.repository.network.api.APIInterface
import com.fullsekurity.theatreblood.repository.network.api.DBAPIClient
import com.fullsekurity.theatreblood.repository.storage.datamodel.Donor
import com.fullsekurity.theatreblood.utils.Constants
import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DonorsDataModel : Callback<ArrayList<Donor>> {

    private val TAG = DonorsDataModel::class.java.simpleName
    private lateinit var donorList: ArrayList<Donor>
    private val donorsDataObjectObservable: ReplaySubject<ArrayList<Donor>> = ReplaySubject.create()
    private val donorsService: APIInterface = DBAPIClient.client

    fun loadData() {
        val rand = Random(System.currentTimeMillis())
        val page = rand.nextInt(500)
        val callBack: Call<ArrayList<Donor>> = donorsService.getMovies(Constants.API_KEY, Constants.LANGUAGE, page)
        callBack.enqueue(this)
    }

    override fun onResponse(call: Call<ArrayList<Donor>>, response: Response<ArrayList<Donor>>) {
        if (response.isSuccessful) {
            response.body()?.let {
                donorsDataObjectObservable.onNext(it)
            }
        } else {
            LogUtils.W(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("RetroFit response error: %s", response.errorBody().toString()))
        }
    }

    override fun onFailure(call: Call<ArrayList<Donor>>, t: Throwable) {
        t.message?.let { LogUtils.E(LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), it, t) }
    }

    fun getDonorsDataObject(): Observable<ArrayList<Donor>> {
        return donorsDataObjectObservable
    }

}