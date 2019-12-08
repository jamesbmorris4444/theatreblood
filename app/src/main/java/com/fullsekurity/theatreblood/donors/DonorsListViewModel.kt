package com.fullsekurity.theatreblood.donors

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewViewModel
import com.fullsekurity.theatreblood.repository.network.api.APIInterface
import com.fullsekurity.theatreblood.repository.network.api.DBAPIClient
import com.fullsekurity.theatreblood.repository.storage.datamodel.Donor
import com.fullsekurity.theatreblood.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

@Suppress("UNCHECKED_CAST")
class DonorsListViewModelFactory(private val activity: MainActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DonorsListViewModel(activity) as T
    }
}

@Suppress("UNCHECKED_CAST")
class DonorsListViewModel(val activity: MainActivity) : Callback<ArrayList<Donor>>, RecyclerViewViewModel(activity.application) {

    private val TAG = DonorsListViewModel::class.java.simpleName
    val liveDonorsDataObject: MutableLiveData<ArrayList<Donor>> = MutableLiveData()
    private lateinit var donorsDataObject: DonorsDataObject
    private val context: Context = getApplication<Application>().applicationContext
    override var adapter: DonorsAdapter = DonorsAdapter(activity, context)
    private var disposable: Disposable? = null
    override val itemDecorator: RecyclerView.ItemDecoration? = null
    private lateinit var donorList: ArrayList<Donor>
    private val donorsDataObjectObservable: ReplaySubject<ArrayList<Donor>> = ReplaySubject.create()
    private val donorsService: APIInterface = DBAPIClient.client

    override fun setLayoutManager(): RecyclerView.LayoutManager {
        return object : LinearLayoutManager(context) {
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return true
            }
        }
    }

    init {
        disposable = donorsDataObjectObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe{
                adapter.addAll(it)
            }
        loadData()
    }

    override fun onCleared() {
        disposable?.let {
            it.dispose()
            disposable = null
        }
    }

    private fun loadData() {
        val rand = Random(System.currentTimeMillis())
        val page = rand.nextInt(500)
        val callBack: Call<ArrayList<Donor>> = donorsService.getDonors(Constants.API_KEY, Constants.LANGUAGE, page)
        callBack.enqueue(this)
    }

    override fun onResponse(call: Call<ArrayList<Donor>>, response: Response<ArrayList<Donor>>) {
        LogUtils.W("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("RetroFit response "))
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

}