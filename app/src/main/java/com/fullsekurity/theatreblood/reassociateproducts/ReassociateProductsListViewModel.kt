package com.fullsekurity.theatreblood.reassociateproducts

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.ActivityCallbacks
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewViewModel
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.repository.storage.Product
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import javax.inject.Inject

class ReassociateProductsListViewModelFactory(private val activityCallbacks: ActivityCallbacks) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReassociateProductsListViewModel(activityCallbacks) as T
    }
}

class ReassociateProductsListViewModel(private val activityCallbacks: ActivityCallbacks) : RecyclerViewViewModel(activityCallbacks.fetchActivity().application) {

    private val tag = ReassociateProductsListViewModel::class.java.simpleName
    override var adapter: ReassociateProductsAdapter = ReassociateProductsAdapter(activityCallbacks)
    override val itemDecorator: RecyclerView.ItemDecoration? = null
    val listIsVisible: ObservableField<Boolean> = ObservableField(true)
    val productsListIsVisible: ObservableField<Boolean> = ObservableField(true)
    val newDonorVisible: ObservableField<Int> = ObservableField(View.GONE)
    val submitVisible: ObservableField<Int> = ObservableField(View.GONE)
    val incorrectDonorVisibility: ObservableField<Int> = ObservableField(View.GONE)
    private var numberOfItemsDisplayed = -1
    private var incorrectDonorIdentified = false

    @Inject
    lateinit var uiViewModel: UIViewModel
    @Inject
    lateinit var repository: Repository

    init {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(activityCallbacks.fetchActivity()))
            .build()
            .inject(this)
        adapter.uiViewModel = uiViewModel
    }

    override fun setLayoutManager(): RecyclerView.LayoutManager {
        return object : LinearLayoutManager(getApplication<Application>().applicationContext) {
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return false
            }
        }
    }

    fun initializeView() {
        val list: MutableList<ReassociateProductsSearchData> = mutableListOf()
        list.add(ReassociateProductsSearchData("HINT", View.VISIBLE, View.GONE, View.GONE, "TEXT"))
        adapter.addAll(list)
    }

    private fun showDonors(donorList: List<Donor>) {
        listIsVisible.set(donorList.isNotEmpty())
        adapter.addAll(donorList)
        numberOfItemsDisplayed = donorList.size
        setNewDonorVisibility("NONEMPTY")
    }

    private fun showProducts(productList: List<Product>) {
        productsListIsVisible.set(productList.isNotEmpty())
        //productListAdapter.addAll(productList)
    }

    private fun setNewDonorVisibility(key: String) {
        if (key.isNotEmpty() && numberOfItemsDisplayed == 0 && incorrectDonorIdentified) {
            newDonorVisible.set(View.VISIBLE)
        } else {
            newDonorVisible.set(View.GONE)
        }
    }

    fun incorrectDonorIdentified(donor: Donor) {
        incorrectDonorIdentified = true
        //reassociateProductsDonorItemViewModel.setItem(donor)
        incorrectDonorVisibility.set(View.VISIBLE)
        adapter.clearAll()
        editTextNameInput.set("")
        repository.getAllNewProductsForDonor(donor, this::showProducts)
    }

    // observable used for two-way data binding. Values set into this field will show in view.
    // Text typed into EditText in view will be stored into this field after each character is typed.
    var editTextNameInput: ObservableField<String> = ObservableField("")
    fun onTextNameChanged(key: CharSequence, start: Int, before: Int, count: Int) {
        if (key.isEmpty()) {
            newDonorVisible.set(View.GONE)
            submitVisible.set(View.GONE)
            numberOfItemsDisplayed = -1
        } else {
            setNewDonorVisibility(key.toString())
            submitVisible.set(View.VISIBLE)
        }
        // within "string", the "count" characters beginning at index "start" have just replaced old text that had length "before"
    }
    var hintTextName: ObservableField<String> = ObservableField(getApplication<Application>().applicationContext.getString(R.string.donor_search_string))
    var editTextNameVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    @Suppress("UNCHECKED_CAST")
    fun onSearchClicked(view: View) {
        repository.handleSearchClick(view, editTextNameInput.get() ?: "", this::showDonors)
    }

    fun onNewDonorClicked(view: View) {
        activityCallbacks.fetchActivity().loadDonorFragment(Donor())
    }

}
