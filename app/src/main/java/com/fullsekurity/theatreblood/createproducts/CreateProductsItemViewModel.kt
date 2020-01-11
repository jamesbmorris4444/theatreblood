package com.fullsekurity.theatreblood.createproducts

import android.view.View
import androidx.databinding.ObservableField
import com.fullsekurity.theatreblood.activity.Callbacks
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewItemViewModel
import com.fullsekurity.theatreblood.repository.storage.Product

@Suppress("UNCHECKED_CAST")
class CreateProductsItemViewModel(val callbacks: Callbacks) : RecyclerViewItemViewModel<Product>() {

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
        editButtonVisibility.set(if (product.inReassociate) View.GONE else View.VISIBLE)
        deleteButtonVisibility.set(if (product.inReassociate && product.removedForReassociation) View.GONE else View.VISIBLE)
    }

    fun onDeleteClicked(view: View) {
        if (product.inReassociate) {
            product.removedForReassociation = true
            deleteButtonVisibility.set(View.GONE)
        } else {
            callbacks.fetchCreateProductsListViewModel()?.onCreateProductsDeleteClicked(view)
        }
    }

    fun onEditClicked(view: View) {
        callbacks.fetchCreateProductsListViewModel()?.onCreateProductsEditClicked(view)
    }

}