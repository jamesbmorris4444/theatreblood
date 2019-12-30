package com.fullsekurity.theatreblood.reassociateproducts

import android.view.View
import androidx.databinding.ObservableField
import com.fullsekurity.theatreblood.activity.ActivityCallbacks
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewItemViewModel

class ReassociateProductsLabelItemViewModel(private val activityCallbacks: ActivityCallbacks) : RecyclerViewItemViewModel<ReassociateProductsLabelData>() {

    val title: ObservableField<String> = ObservableField("")
    val incorrectDonorVisibility: ObservableField<Int> = ObservableField(View.GONE)

    override fun setItem(item: ReassociateProductsLabelData) {
        title.set(item.title)
        incorrectDonorVisibility.set(item.incorrectDonorVisibility)
    }

    fun onTextNameChanged(key: CharSequence, start: Int, before: Int, count: Int) {
        // within "string", the "count" characters beginning at index "start" have just replaced old text that had length "before"
    }

}