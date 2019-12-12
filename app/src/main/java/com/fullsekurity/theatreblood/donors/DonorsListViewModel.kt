package com.fullsekurity.theatreblood.donors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewViewModel
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.network.APIClient
import com.fullsekurity.theatreblood.repository.network.APIInterface
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.utils.ContextInjectorModule
import com.fullsekurity.theatreblood.utils.DaggerContextDependencyInjector
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class DonorsListViewModelFactory(private val activity: MainActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DonorsListViewModel(activity) as T
    }
}

@Suppress("UNCHECKED_CAST")
class DonorsListViewModel(val activity: MainActivity) : RecyclerViewViewModel(activity.application) {

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

    fun showDonors(donorList: List<Donor>) {
        adapter.addAll(donorList)
    }

}
