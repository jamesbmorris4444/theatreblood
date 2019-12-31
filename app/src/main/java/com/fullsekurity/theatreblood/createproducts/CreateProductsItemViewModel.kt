package com.fullsekurity.theatreblood.createproducts

import android.view.View
import androidx.databinding.ObservableField
import com.fullsekurity.theatreblood.activity.ActivityCallbacks
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewItemViewModel
import com.fullsekurity.theatreblood.repository.storage.Product

@Suppress("UNCHECKED_CAST")
class CreateProductsItemViewModel(val activityCallbacks: ActivityCallbacks) : RecyclerViewItemViewModel<Product>() {

    private lateinit var product: Product

    val din: ObservableField<String> = ObservableField("")
    val aboRh: ObservableField<String> = ObservableField("")
    val productCode: ObservableField<String> = ObservableField("")
    val expirationDate: ObservableField<String> = ObservableField("")
    var editButtonVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)
    var deleteButtonVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    override fun setItem(item: Product) {
        product = item
        din.set(item.din)
        aboRh.set(item.aboRh)
        productCode.set(item.productCode)
        expirationDate.set(item.expirationDate)
        editButtonVisibility.set(item.editButtonVisibility)
        if (product.removedForReassociation) {
            deleteButtonVisibility.set(View.GONE)
        } else {
            deleteButtonVisibility.set(item.deleteButtonVisibility)
        }
    }

    fun onDeleteClicked(view: View) {
        if (product.inReassociate) {
            product.removedForReassociation = true
            LogUtils.D("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("JIMX  %b   %s", product.removedForReassociation, din))
            deleteButtonVisibility.set(View.GONE)
        } else {
            activityCallbacks.fetchActivity().onCreateProductsDeleteClicked(view)
        }
    }

    fun onEditClicked(view: View) {
        activityCallbacks.fetchActivity().onCreateProductsEditClicked(view)
    }

}