package com.fullsekurity.theatreblood.reassociateproducts

import android.view.View
import androidx.databinding.ObservableField
import com.fullsekurity.theatreblood.activity.ActivityCallbacks
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewItemViewModel

class ReassociateProductsSearchItemViewModel(private val activityCallbacks: ActivityCallbacks) : RecyclerViewItemViewModel<ReassociateProductsSearchData>() {

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
        activityCallbacks.fetchActivity().reassociateOnSearchClicked(view)
    }

    fun onNewDonorClicked(view: View) {
        activityCallbacks.fetchActivity().reassociateOnNewDonorClicked(view)
    }

    fun onTextNameChanged(key: CharSequence, start: Int, before: Int, count: Int) {
        activityCallbacks.fetchActivity().reassociateOnTextNameChanged(key.toString())
    }

}