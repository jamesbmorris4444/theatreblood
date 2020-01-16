package com.fullsekurity.theatreblood.viewdonorlist

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.Callbacks
import com.fullsekurity.theatreblood.donateproducts.DonateProductsAdapter
import com.fullsekurity.theatreblood.managedonor.ManageDonorViewModel
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewViewModel
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.Utils
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import javax.inject.Inject

class ViewDonorListListViewModelFactory(private val callbacks: Callbacks) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewDonorListListViewModel(callbacks) as T
    }
}

class ViewDonorListListViewModel(private val callbacks: Callbacks) : RecyclerViewViewModel(callbacks.fetchActivity().application) {

    private val tag = ViewDonorListListViewModel::class.java.simpleName
    override var adapter: DonateProductsAdapter = DonateProductsAdapter(callbacks)
    override val itemDecorator: RecyclerView.ItemDecoration? = null
    val listIsVisible: ObservableField<Boolean> = ObservableField(true)
    val newDonorVisible: ObservableField<Int> = ObservableField(View.GONE)
    val submitVisible: ObservableField<Int> = ObservableField(View.GONE)
    private var numberOfItemsDisplayed = -1
    private var patternOfSubpatterns: String = "<>|<>"

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

    fun showAllDonors(view: View) {
        repository.handleSearchClick(view, "", this::showDonors)
    }

    fun showDonors(donorList: List<Donor>) {
        listIsVisible.set(donorList.isNotEmpty())
        adapter.addAll(donorList.sortedBy { donor -> Utils.donorComparisonByString(donor) })
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

    // observable used for two-way donations binding. Values set into this field will show in view.
    // Text typed into EditText in view will be stored into this field after each character is typed.
    var editTextNameInput: ObservableField<String> = ObservableField("")
    fun onTextNameChanged(newText: CharSequence, start: Int, before: Int, count: Int) {
        patternOfSubpatterns = Utils.newPatternOfSubpatterns(patternOfSubpatterns, 0, if (newText.toString().isEmpty()) "<>" else newText.toString())
        adapter.filter.filter(patternOfSubpatterns)
        // within "string", the "count" characters beginning at index "start" have just replaced old text that had length "before"
    }
    var hintTextName: ObservableField<String> = ObservableField(getApplication<Application>().applicationContext.getString(
        R.string.donor_search_string))
    var editTextNameVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    // ABO/Rh
    var hintTextAboRh: ObservableField<String> = ObservableField(getApplication<Application>().applicationContext.getString(R.string.donor_abo_rh))
    var dropdownAboRhVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)
    var currentAboRhSelectedValue: String = ""

    fun setDropdowns() {
        callbacks.fetchDropdown(R.id.abo_rh_dropdown)?.let {
            val aboRhDropdownView: Spinner = it
            aboRhDropdownView.background = uiViewModel.editTextBackground.get()
            val aboRhDropdownArray = getApplication<Application>().applicationContext.resources.getStringArray(R.array.abo_rh_array_with_no_value)
            val aboRhAdapter = ManageDonorViewModel.CustomSpinnerAdapter(callbacks.fetchActivity(), uiViewModel, aboRhDropdownArray)
            aboRhDropdownView.adapter = aboRhAdapter
            aboRhDropdownView.setSelection(0)
            aboRhDropdownView.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val temp = if (position > 0) parent.getItemAtPosition(position) as String else ""
                    currentAboRhSelectedValue = if (temp.isEmpty()) "<>" else temp
                    patternOfSubpatterns = Utils.newPatternOfSubpatterns(patternOfSubpatterns, 1, currentAboRhSelectedValue)
                    adapter.filter.filter(patternOfSubpatterns)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) { }
            }
        }
    }


}
