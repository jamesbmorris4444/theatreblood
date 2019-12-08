package com.fullsekurity.theatreblood.repository.network.paging

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.repository.network.api.APIInterface
import com.fullsekurity.theatreblood.repository.network.api.DBAPIClient
import com.fullsekurity.theatreblood.repository.storage.datamodel.Donor
import com.fullsekurity.theatreblood.repository.storage.datamodel.NetworkState
import com.fullsekurity.theatreblood.utils.Constants.API_KEY
import com.fullsekurity.theatreblood.utils.Constants.LANGUAGE
import io.reactivex.subjects.ReplaySubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicInteger

val TAG = NetworkPageKeyedDataSource::class.java.simpleName
class NetworkPageKeyedDataSource internal constructor() : PageKeyedDataSource<String, Donor>() {

    private val moviesService: APIInterface = DBAPIClient.client
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()
    val moviesObservable: ReplaySubject<Donor> = ReplaySubject.create()

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, Donor>) {
        LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("loadInitial(): Loading Initial Range, Count %d", params.requestedLoadSize))
        networkState.postValue(NetworkState.LOADING)
        val callBack: Call<ArrayList<Donor>> = moviesService.getDonors(API_KEY, LANGUAGE, 1)
        callBack.enqueue(object : Callback<ArrayList<Donor>> {
            override fun onResponse(call: Call<ArrayList<Donor>>, response: Response<ArrayList<Donor>>) {
                if (response.isSuccessful) {
                    LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("onResponse(): SUCCESS"))
                    callback.onResult(response.body() as MutableList<Donor>, 1.toString(), 2.toString())
                    networkState.postValue(NetworkState.LOADED)
                    for (movie in response.body() as MutableList<Donor>) {
                        LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("onResponse(): movie=%s", movie.title))
                        moviesObservable.onNext(movie)
                    }
                } else {
                    LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("onResponse(): FAILURE %s", response.message()))
                    networkState.postValue(NetworkState(NetworkState.Status.FAILED, response.message()))
                }
            }

            override fun onFailure(call: Call<ArrayList<Donor>>, t: Throwable) {
                val errorMessage: String
                if (t.message == null) {
                    errorMessage = "unknown error"
                } else {
                    errorMessage = t.message.toString()
                }
                LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("onResponse(): FAILURE %s", errorMessage))
                networkState.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage))
                callback.onResult(ArrayList(), 1.toString(), 2.toString())
            }
        })
    }


    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Donor>) {
        LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("loadInitial(): Loading After Range, Count %d", params.requestedLoadSize))
        networkState.postValue(NetworkState.LOADING)
        val page = AtomicInteger(0)
        try {
            page.set(Integer.parseInt(params.key))
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }

        val callBack = moviesService.getDonors(API_KEY, LANGUAGE, page.get())
        callBack.enqueue(object : Callback<ArrayList<Donor>> {
            override fun onResponse(call: Call<ArrayList<Donor>>, response: Response<ArrayList<Donor>>) {
                if (response.isSuccessful) {
                    callback.onResult(response.body() as MutableList<Donor>, (page.get() + 1).toString())
                    networkState.postValue(NetworkState.LOADED)
                    for (movie in response.body() as MutableList<Donor>) {
                        moviesObservable.onNext(movie)
                    }
                } else {
                    networkState.postValue(NetworkState(NetworkState.Status.FAILED, response.message()))
                    Log.e("API CALL", response.message())
                }
            }

            override fun onFailure(call: Call<ArrayList<Donor>>, t: Throwable) {
                val errorMessage = if (t.message == null) {
                    "unknown error"
                } else {
                    t.message.toString()
                }
                networkState.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage))
                callback.onResult(ArrayList(), page.get().toString())
            }
        })
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Donor>) {

    }

}