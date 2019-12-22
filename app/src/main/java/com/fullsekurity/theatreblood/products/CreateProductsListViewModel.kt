package com.fullsekurity.theatreblood.products

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.ActivityCallbacks
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewViewModel
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.repository.storage.Product
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import javax.inject.Inject


@Suppress("UNCHECKED_CAST")
class CreateProductsListViewModelFactory(private val activity: MainActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreateProductsListViewModel(activity) as T
    }
}

@Suppress("UNCHECKED_CAST")
class CreateProductsListViewModel(private val activityCallbacks: ActivityCallbacks) : RecyclerViewViewModel(activityCallbacks.fetchActivity().application) {

    private val tag = CreateProductsListViewModel::class.java.simpleName
    override var adapter: CreateProductsAdapter = CreateProductsAdapter(activityCallbacks)
    override val itemDecorator: RecyclerView.ItemDecoration? = null
    private var numberOfItemsDisplayed = -1

    val gridText11: ObservableField<String> = ObservableField("HELLO")
    val gridText12: ObservableField<String> = ObservableField("")
    val gridText21: ObservableField<String> = ObservableField("")
    val gridText22: ObservableField<String> = ObservableField("")

    val gridText11Visible: ObservableField<Int> = ObservableField(View.GONE)
    val gridText12Visible: ObservableField<Int> = ObservableField(View.GONE)
    val gridText21Visible: ObservableField<Int> = ObservableField(View.GONE)
    val gridText22Visible: ObservableField<Int> = ObservableField(View.GONE)

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
        return object : LinearLayoutManager(activityCallbacks.fetchActivity().applicationContext) {
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return true
            }
        }
    }

    private fun showProducts(productList: List<Product>) {
        adapter.addAll(productList)
        numberOfItemsDisplayed = productList.size
        setNewDonorVisibility("NONEMPTY")
    }

    private fun setNewDonorVisibility(key: String) {
//        if (key.isNotEmpty() && numberOfItemsDisplayed == 0) {
//            newDonorVisible.set(View.VISIBLE)
//        } else {
//            newDonorVisible.set(View.GONE)
//        }
    }

    // observable used for two-way data binding. Values set into this field will show in view.
    // Text typed into EditText in view will be stored into this field after each character is typed.
    var editTextProductDin: ObservableField<String> = ObservableField("")
    fun onTextDinChanged(key: CharSequence, start: Int, before: Int, count: Int) {
//        if (key.isEmpty()) {
//            newDonorVisible.set(View.GONE)
//            submitVisible.set(View.GONE)
//            numberOfItemsDisplayed = -1
//        } else {
//            setNewDonorVisibility(key.toString())
//            submitVisible.set(View.VISIBLE)
//        }
        // within "string", the "count" characters beginning at index "start" have just replaced old text that had length "before"
    }
    var hintTextDin: ObservableField<String> = ObservableField(activityCallbacks.fetchActivity().getString(R.string.donor_search_string))
    var editTextDinVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    fun setDonor(donor: Donor) {

    }

    fun onGridElement11Clicked(view: View) {
        activityCallbacks.fetchActivity().barcodeScanner(11, this)
    }

    fun onGridElement12Clicked(view: View) {
        activityCallbacks.fetchActivity().barcodeScanner(12, this)
    }

    fun onGridElement21Clicked(view: View) {
        activityCallbacks.fetchActivity().barcodeScanner(21, this)
    }

    fun onGridElement22Clicked(view: View) {
        activityCallbacks.fetchActivity().barcodeScanner(22, this)
    }

}