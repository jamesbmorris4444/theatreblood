package com.fullsekurity.theatreblood.createproducts

import android.app.DatePickerDialog
import android.view.View
import android.widget.DatePicker
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.Callbacks
import com.fullsekurity.theatreblood.modal.StandardModal
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.repository.storage.Product
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.Constants
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.Utils
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@Suppress("UNCHECKED_CAST")
class CreateProductsListViewModelFactory(private val callbacks: Callbacks) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreateProductsListViewModel(callbacks) as T
    }
}

@Suppress("UNCHECKED_CAST")
class CreateProductsListViewModel(private val callbacks: Callbacks) : AndroidViewModel(callbacks.fetchActivity().application) {

    private lateinit var donor: Donor
    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)
    private var dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.US)
    val donorName: ObservableField<String> = ObservableField("")
    val clearButtonVisibility: ObservableField<Int> = ObservableField(View.GONE)
    val confirmButtonVisibility: ObservableField<Int> = ObservableField(View.GONE)
    val completeButtonVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)
    var confirmNeeded = false
    val productList: MutableList<Product> = mutableListOf()

    @Inject
    lateinit var uiViewModel: UIViewModel
    @Inject
    lateinit var repository: Repository

    init {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(callbacks.fetchActivity()))
            .build()
            .inject(this)
        callbacks.fetchActivity().createProductsListViewModel = this
    }
    
    var editTextProductDin: ObservableField<String> = ObservableField("")
    fun onTextDinChanged(key: CharSequence, start: Int, before: Int, count: Int) {
        if (key.isNotEmpty()) {
            clearButtonVisibility.set(View.VISIBLE)
            confirmButtonVisibility.set(View.VISIBLE)
            confirmNeeded = true
        }
        // within "key", the "count" characters beginning at index "start" have just replaced old text that had length "before"
    }
    var hintTextDin: ObservableField<String> = ObservableField(callbacks.fetchActivity().getString(R.string.product_din_hint_string))

    var editTextProductCode: ObservableField<String> = ObservableField("")
    fun onTextCodeChanged(key: CharSequence, start: Int, before: Int, count: Int) {
        if (key.isNotEmpty()) {
            clearButtonVisibility.set(View.VISIBLE)
            confirmButtonVisibility.set(View.VISIBLE)
            confirmNeeded = true
        }
    }
    var hintTextCode: ObservableField<String> = ObservableField(callbacks.fetchActivity().getString(R.string.product_code_hint_string))

    var editTextProductExpDate: ObservableField<String> = ObservableField("")
    fun onTextExpDateChanged(key: CharSequence, start: Int, before: Int, count: Int) {
        if (key.isNotEmpty()) {
            clearButtonVisibility.set(View.VISIBLE)
            confirmButtonVisibility.set(View.VISIBLE)
            confirmNeeded = true
        }
    }
    var hintTextExpDate: ObservableField<String> = ObservableField(callbacks.fetchActivity().getString(R.string.product_expiration_date_hint_string))

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
        DatePickerDialog(callbacks.fetchActivity(), uiViewModel.datePickerColorStyle, listener, year, month, day).show()
    }

    var donorBloodType: ObservableField<String> = ObservableField("")

    fun setDonor(donor: Donor) {
        this.donor = donor
        donorName.set("DONOR: " + donor.lastName + ", " + donor.firstName)
        donorBloodType.set(donor.aboRh)
    }

    fun onGridElement11Clicked(view: View) {
        callbacks.fetchActivity().barcodeScanner(11)
    }

    fun onGridElement12Clicked(view: View) {
        callbacks.fetchActivity().barcodeScanner(12)
    }

    fun onGridElement21Clicked(view: View) {
        callbacks.fetchActivity().barcodeScanner(21)
    }

    fun onGridElement22Clicked(view: View) {
        callbacks.fetchActivity().barcodeScanner(22)
    }

    fun onClearClicked(view: View) {
        editTextProductDin.set("")
        editTextProductCode.set("")
        editTextProductExpDate.set("")
        clearButtonVisibility.set(View.GONE)
        confirmButtonVisibility.set(View.GONE)
        confirmNeeded = false
    }

    fun processNewProduct() {
        val product = Product()
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
        product.donorId = donor.id
        productList.add(product)
    }

    fun onCompleteClicked(view: View) {
        Utils.hideKeyboard(view)
        if (confirmNeeded) {
            StandardModal(
                callbacks,
                modalType = StandardModal.ModalType.STANDARD,
                titleText = callbacks.fetchActivity().getString(R.string.std_modal_noconfirm_title),
                bodyText = callbacks.fetchActivity().getString(R.string.std_modal_noconfirm_body),
                positiveText = callbacks.fetchActivity().getString(R.string.std_modal_yes),
                negativeText = callbacks.fetchActivity().getString(R.string.std_modal_no),
                dialogFinishedListener = object : StandardModal.DialogFinishedListener {
                    override fun onPositive(string: String) {
                        processNewProduct()
                        addDonorWithProductsToModifiedDatabase()
                    }
                    override fun onNegative() { }
                    override fun onNeutral() { }
                    override fun onBackPressed() {
                        processNewProduct()
                        addDonorWithProductsToModifiedDatabase()
                    }
                }
            ).show(callbacks.fetchActivity().supportFragmentManager, "MODAL")
        } else {
            if (productList.size > 0) {
                addDonorWithProductsToModifiedDatabase()
            } else {
                callbacks.fetchActivity().supportFragmentManager.popBackStack(Constants.ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                callbacks.fetchActivity().loadDonateProductsFragment(true)
            }
        }
        confirmNeeded = false
    }

    private fun addDonorWithProductsToModifiedDatabase() {
        for (productIndex in productList.indices) {
            productList[productIndex].donorId = donor.id
        }
        repository.insertDonorAndProductsIntoDatabase(repository.stagingBloodDatabase, donor, productList, this::showStagingDatabaseEntries)
    }

    private fun showStagingDatabaseEntries() {
        repository.getListOfDonorsAndProducts(Utils::donorsAndProductsList)
    }

    class ProductListDiffCallback(private val oldList: List<Product>, private val newList: List<Product>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].din == newList[newItemPosition].din && oldList[oldItemPosition].productCode == newList[newItemPosition].productCode
        }
        override fun areContentsTheSame(oldItemPosition: Int, newPosition: Int): Boolean {
            return areItemsTheSame(oldItemPosition, newPosition)
        }
    }

}