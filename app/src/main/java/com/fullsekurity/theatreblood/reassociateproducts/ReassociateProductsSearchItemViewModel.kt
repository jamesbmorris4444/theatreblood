package com.fullsekurity.theatreblood.reassociateproducts

import android.view.View
import androidx.databinding.ObservableField
import com.fullsekurity.theatreblood.activity.Callbacks
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewItemViewModel

class ReassociateProductsSearchItemViewModel(private val callbacks: Callbacks) : RecyclerViewItemViewModel<ReassociateProductsSearchData>() {

    val hintTextName: ObservableField<String> = ObservableField("")
    val editTextNameVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)
    val newDonorVisibility: ObservableField<Int> = ObservableField(View.GONE)
    val submitVisibility: ObservableField<Int> = ObservableField(View.GONE)
    val editTextNameInput: ObservableField<String> = ObservableField("")

    override fun setItem(item: ReassociateProductsSearchData) {
        hintTextName.set(item.hintTextName)
        editTextNameVisibility.set(item.editTextNameVisibility)
        newDonorVisibility.set(item.newDonorVisibility)
        submitVisibility.set(item.submitVisibility)
        editTextNameInput.set(item.editTextNameInput)
    }

    fun onSearchClicked(view: View) {
        callbacks.fetchReassociateProductsListViewModel()?.handleReassociateSearchClick(view)
    }

    fun onNewDonorClicked(view: View) {
        callbacks.fetchActivity().reassociateOnNewDonorClicked(view)
        callbacks.fetchReassociateProductsListViewModel()?.repository?.newDonorInProgress = true
    }

    fun onTextNameChanged(key: CharSequence, start: Int, before: Int, count: Int) {
        callbacks.fetchReassociateProductsListViewModel()?.onTextNameChanged(key.toString(), 0, 0, 0)
    }

}