package com.fullsekurity.theatreblood.createproducts

import androidx.databinding.ObservableField
import com.fullsekurity.theatreblood.activity.Callbacks
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewItemViewModel
import com.fullsekurity.theatreblood.repository.storage.Product

@Suppress("UNCHECKED_CAST")
class CreateProductsItemViewModel(val callbacks: Callbacks) : RecyclerViewItemViewModel<Product>() {

    lateinit var product: Product

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

}