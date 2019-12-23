package com.fullsekurity.theatreblood.products

import android.app.DatePickerDialog
import android.view.View
import android.widget.DatePicker
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
import com.fullsekurity.theatreblood.utils.Utils
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import java.text.SimpleDateFormat
import java.util.*
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
    private lateinit var donor: Donor
    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)
    private var dateFormatter = SimpleDateFormat("MM/dd/yyyy", Locale.US)


    val gridText22: ObservableField<String> = ObservableField("")


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
    var hintTextDin: ObservableField<String> = ObservableField(activityCallbacks.fetchActivity().getString(R.string.product_din_hint_string))
    var editTextDinVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    var editTextProductCode: ObservableField<String> = ObservableField("")
    fun onTextCodeChanged(key: CharSequence, start: Int, before: Int, count: Int) {
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
    var hintTextCode: ObservableField<String> = ObservableField(activityCallbacks.fetchActivity().getString(R.string.product_code_hint_string))
    var editTextCodeVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    var editTextProductExpDate: ObservableField<String> = ObservableField("")
    fun onTextExpDateChanged(key: CharSequence, start: Int, before: Int, count: Int) {
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
    var hintTextExpDate: ObservableField<String> = ObservableField(activityCallbacks.fetchActivity().getString(R.string.product_expiration_date_hint_string))
    var editTextExpDateVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    fun onCalendarClicked(view: View) {
        Utils.hideKeyboard(view)
        dateDialog()
    }

    private fun dateDialog() {
        val listener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                calendar.set(year, monthOfYear, dayOfMonth)
                editTextProductExpDate.set(dateFormatter.format(calendar.time))
            }
        }
        DatePickerDialog(activityCallbacks.fetchActivity(), uiViewModel.datePickerColorStyle, listener, year, month, day).show()
    }

    var donorBloodType: ObservableField<String> = ObservableField("")

    fun setDonor(donor: Donor) {
        this.donor = donor
        donorBloodType.set(donor.backdropPath)
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