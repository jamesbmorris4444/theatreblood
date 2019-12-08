package com.fullsekurity.theatreblood.donors

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@Suppress("UNCHECKED_CAST")
class DonorsListViewModelFactory(private val activity: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DonorsListViewModel(activity) as T
    }
}

@Suppress("UNCHECKED_CAST")
class DonorsListViewModel(val activity: Application) : RecyclerViewViewModel(activity) {

    private val TAG = DonorsListViewModel::class.java.simpleName
    private val donorsDataModel = DonorsDataModel()
    val liveDonorsDataObject: MutableLiveData<DonorsDataObject> = MutableLiveData()
    private lateinit var donorsDataObject: DonorsDataObject
    private val context: Context = getApplication<Application>().applicationContext
    override var adapter: DonorsAdapter = DonorsAdapter((activity as MainActivity), context)
    private var disposable: Disposable? = null
    override val itemDecorator: RecyclerView.ItemDecoration? = null

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
        disposable = donorsDataModel.getDonorsDataObject()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe{
                donorsDataObject -> liveDonorsDataObject.postValue(donorsDataObject)
                this.donorsDataObject = donorsDataObject
            }
        donorsDataModel.loadData()
    }

    override fun onCleared() {
        disposable?.let {
            it.dispose()
            disposable = null
        }
    }

    fun getLiveDonorsDataObject(): LiveData<DonorsDataObject> {
        return liveDonorsDataObject
    }
}