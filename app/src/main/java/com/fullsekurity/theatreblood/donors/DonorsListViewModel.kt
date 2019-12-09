package com.fullsekurity.theatreblood.donors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewViewModel
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.network.APIClient
import com.fullsekurity.theatreblood.repository.network.APIInterface
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.utils.Constants
import com.fullsekurity.theatreblood.utils.ContextInjectorModule
import com.fullsekurity.theatreblood.utils.DaggerContextDependencyInjector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class DonorsListViewModelFactory(private val activity: MainActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DonorsListViewModel(activity) as T
    }
}

@Suppress("UNCHECKED_CAST")
class DonorsListViewModel(val activity: MainActivity) : Callback<ArrayList<Donor>>, RecyclerViewViewModel(activity.application) {

    private val TAG = DonorsListViewModel::class.java.simpleName
    override var adapter: DonorsAdapter = DonorsAdapter(activity)
    override val itemDecorator: RecyclerView.ItemDecoration? = null
    private val donorsService: APIInterface = APIClient.client
    private var disposable: Disposable? = null

    internal var repository: Repository? = null
        @Inject set

    init {
        DaggerContextDependencyInjector.builder()
            .contextInjectorModule(ContextInjectorModule(activity.applicationContext))
            .build()
            .inject(this)
        loadData()
    }

    override fun setLayoutManager(): RecyclerView.LayoutManager {
        return object : LinearLayoutManager(activity.applicationContext) {
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return true
            }
        }
    }

    private fun loadData2() {
//        val callBack: Call<ArrayList<Donor>> = donorsService.getDonors(Constants.API_KEY, Constants.LANGUAGE, "popularity.desc", "false", "false", 1)
//        callBack.enqueue(this)
    }

    private fun loadData() {
        LogUtils.W("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.DAO), String.format("HERE 1 "))
        disposable = donorsService.getDonors(Constants.API_KEY, Constants.LANGUAGE, 1)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                adapter.addAll(it.results)
            }
    }

    override fun onResponse(call: Call<ArrayList<Donor>>, response: Response<ArrayList<Donor>>) {
        if (response.isSuccessful) {
            response.body()?.let {
                adapter.addAll(it)
                for (donor in it) {
                    repository?.insertIntoDatabase(donor)
                    LogUtils.W("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.DAO), String.format("store donors: %s", donor.title))
                }

            }
        } else {
            LogUtils.W(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("RetroFit response error: %s", response.errorBody().toString()))
        }
    }

    override fun onFailure(call: Call<ArrayList<Donor>>, t: Throwable) {
        t.message?.let { LogUtils.E(LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), it, t) }
    }

}
