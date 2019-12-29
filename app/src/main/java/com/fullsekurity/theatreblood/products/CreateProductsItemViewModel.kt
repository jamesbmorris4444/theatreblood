package com.fullsekurity.theatreblood.products

import android.view.View
import androidx.databinding.ObservableField
import com.fullsekurity.theatreblood.activity.ActivityCallbacks
import com.fullsekurity.theatreblood.repository.storage.Product
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewItemViewModel

@Suppress("UNCHECKED_CAST")
class CreateProductsItemViewModel(val activityCallbacks: ActivityCallbacks) : RecyclerViewItemViewModel<Product>() {

    private lateinit var product: Product

    val din: ObservableField<String> = ObservableField("")
    val aboRh: ObservableField<String> = ObservableField("")
    val productCode: ObservableField<String> = ObservableField("")
    val expirationDate: ObservableField<String> = ObservableField("")

    override fun setItem(item: Product) {
        product = item
        din.set(item.din)
        aboRh.set(item.aboRh)
        productCode.set(item.productCode)
        expirationDate.set(item.expirationDate)
    }

    fun onDeleteClicked(view: View) {
        activityCallbacks.fetchActivity().onCreateProductsDeleteClicked(view)
    }

    fun onEditClicked(view: View) {
        activityCallbacks.fetchActivity().onCreateProductsEditClicked(view)
    }

}