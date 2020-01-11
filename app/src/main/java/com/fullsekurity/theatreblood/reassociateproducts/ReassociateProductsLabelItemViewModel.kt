package com.fullsekurity.theatreblood.reassociateproducts

import android.view.View
import androidx.databinding.ObservableField
import com.fullsekurity.theatreblood.activity.Callbacks
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewItemViewModel

class ReassociateProductsLabelItemViewModel(private val callbacks: Callbacks) : RecyclerViewItemViewModel<ReassociateProductsLabelData>() {

    val title: ObservableField<String> = ObservableField("")
    val incorrectDonorVisibility: ObservableField<Int> = ObservableField(View.GONE)

    override fun setItem(item: ReassociateProductsLabelData) {
        title.set(item.title)
        incorrectDonorVisibility.set(item.incorrectDonorVisibility)
    }

}