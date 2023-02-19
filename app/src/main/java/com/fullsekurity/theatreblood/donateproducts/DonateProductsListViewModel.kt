package com.fullsekurity.theatreblood.donateproducts

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.Callbacks
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewViewModel
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.Utils
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import javax.inject.Inject

class DonateProductsListViewModelFactory(private val callbacks: Callbacks) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DonateProductsListViewModel(callbacks) as T
    }
}

class DonateProductsListViewModel(private val callbacks: Callbacks) : RecyclerViewViewModel(callbacks.fetchActivity().application) {

    private val listener = object : DonateProductsClickListener {
        override fun onItemClick(view: View, donorWithRemovedProduct: Donor?, position: Int) {
            Utils.hideKeyboard(view)
            callbacks.fetchActivity().loadDonorFragment(adapter.itemList[position], callbacks.fetchActivity().transitionToCreateDonation)
        }
    }
    override var adapter: DonateProductsAdapter = DonateProductsAdapter(callbacks, listener)
    override val itemDecorator: RecyclerView.ItemDecoration? = null
    val listIsVisible: ObservableField<Boolean> = ObservableField(true)
    val newDonorVisible: ObservableField<Int> = ObservableField(View.GONE)
    val submitVisible: ObservableField<Int> = ObservableField(View.GONE)
    private var numberOfItemsDisplayed = -1
    var transitionToCreateDonation = true

    @Inject
    lateinit var uiViewModel: UIViewModel
    @Inject
    lateinit var repository: Repository

    init {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(callbacks.fetchActivity()))
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
                return true
            }
        }
    }

    private fun showDonors(donorList: List<Donor>) {
        listIsVisible.set(donorList.isNotEmpty())
        val newDonorList = donorList.sortedBy { donor -> Utils.donorComparisonByString(donor) }
        adapter.addAll(newDonorList, DonorListDiffCallback(adapter.itemList, newDonorList))
        numberOfItemsDisplayed = donorList.size
        setNewDonorVisibility("NONEMPTY")
    }

    private fun setNewDonorVisibility(key: String) {
        if (key.isNotEmpty() && numberOfItemsDisplayed == 0) {
            newDonorVisible.set(View.VISIBLE)
        } else {
            newDonorVisible.set(View.GONE)
        }
    }

    fun initialize(view: View) {
        val textInputLayout: TextInputLayout = view.findViewById(R.id.edit_text_input_name)
        val textInputEditText: TextInputEditText = view.findViewById(R.id.edit_text_input_name_editText)
        textInputLayout.setHintTextAppearance(uiViewModel.editTextDisplayModifyHintStyle)
        textInputEditText.requestFocus()
        Utils.showKeyboard(textInputEditText)
    }

    // observable used for two-way donations binding. Values set into this field will show in view.
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

    fun onSearchClicked(view: View) {
        Utils.hideKeyboard(view)
        repository.handleSearchClick(view, editTextNameInput.get() ?: "", this::showDonors)
    }

    fun onNewDonorClicked(view: View) {
        callbacks.fetchActivity().loadDonorFragment(null, transitionToCreateDonation)
        repository.newDonorInProgress = true
        repository.newDonor = null
    }

    class DonorListDiffCallback(private val oldList: List<Donor>, private val newList: List<Donor>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].firstName == newList[newItemPosition].firstName && oldList[oldItemPosition].lastName == newList[newItemPosition].lastName
        }
        override fun areContentsTheSame(oldItemPosition: Int, newPosition: Int): Boolean {
            return areItemsTheSame(oldItemPosition, newPosition)
        }
    }

    interface DonateProductsClickListener {
        fun onItemClick(view: View, donorWithRemovedProduct: Donor?, position: Int)
    }

}