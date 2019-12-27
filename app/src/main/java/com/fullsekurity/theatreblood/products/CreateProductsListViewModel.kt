package com.fullsekurity.theatreblood.products

import android.app.Application
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
import com.fullsekurity.theatreblood.donors.Donor
import com.fullsekurity.theatreblood.donors.Product
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewViewModel
import com.fullsekurity.theatreblood.repository.Repository
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
    private var dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.US)
    val donorName: ObservableField<String> = ObservableField("")
    val editOrDoneText: ObservableField<String> = ObservableField("")
    val clearButtonVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)
    val confirmButtonVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)
    val editOrDoneButtonVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)
    val completeButtonVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)
    private val productList: MutableList<Product> = mutableListOf()

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
        editOrDoneText.set(getApplication<Application>().applicationContext.getString(R.string.button_edit))
        activityCallbacks.fetchActivity().createProductsListViewModel = this
    }

    override fun setLayoutManager(): RecyclerView.LayoutManager {
        return object : LinearLayoutManager(activityCallbacks.fetchActivity().applicationContext) {
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return false
            }
        }
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
        donorName.set("DONOR: " + donor.title + ", " + donor.posterPath)
        donorBloodType.set(donor.backdropPath)
    }

    fun onGridElement11Clicked(view: View) {
        activityCallbacks.fetchActivity().barcodeScanner(11)
    }

    fun onGridElement12Clicked(view: View) {
        activityCallbacks.fetchActivity().barcodeScanner(12)
    }

    fun onGridElement21Clicked(view: View) {
        activityCallbacks.fetchActivity().barcodeScanner(21)
    }

    fun onGridElement22Clicked(view: View) {
        activityCallbacks.fetchActivity().barcodeScanner(22)
    }

    fun onClearClicked(view: View) {
        editTextProductDin.set("")
        editTextProductCode.set("")
        editTextProductExpDate.set("")
    }

    fun onConfirmClicked(view: View) {
        var product = Product()
        editTextProductDin.get()?.let {
            product.din = it
        }
        donorBloodType.get()?.let {
            product.aboRh = it
        }
        editTextProductCode.get()?.let {
            product.productCode = it
        }
        editTextProductExpDate.get()?.let {
            product.expirationDate = it
        }
        productList.add(product)
        adapter.addAll(productList)
    }

    fun onCompleteClicked(view: View) {
        //repository.insertProductsIntoDatabase(repository.insertedBloodDatabase, productList)
    }

    fun onCreateProductsItemClicked(view: View) {
        val position = view.tag as Int
    }

    fun onCreateProductsDeleteClicked(view: View) {
        val position = view.tag as Int
        productList.removeAt(position)
        adapter.addAll(productList)
    }

    fun onCreateProductsEditClicked(view: View) {
        val position = view.tag as Int
        editTextProductDin.set(productList[position].din)
        editTextProductCode.set(productList[position].productCode)
        editTextProductExpDate.set(productList[position].expirationDate)
        productList.removeAt(position)
        adapter.addAll(productList)
    }

}