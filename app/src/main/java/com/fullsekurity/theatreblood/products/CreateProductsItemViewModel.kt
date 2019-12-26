package com.fullsekurity.theatreblood.products

import android.view.View
import androidx.databinding.ObservableField
import com.fullsekurity.theatreblood.activity.ActivityCallbacks
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewItemViewModel

@Suppress("UNCHECKED_CAST")
class CreateProductsItemViewModel(val activityCallbacks: ActivityCallbacks) : RecyclerViewItemViewModel<Product>() {

    private lateinit var product: Product

    val title: ObservableField<String> = ObservableField("")
    val posterPath: ObservableField<String> = ObservableField("")
    val originalLanguage: ObservableField<String> = ObservableField("")
    val releaseDate: ObservableField<String> = ObservableField("")

    override fun setItem(item: Product) {
        product = item
        title.set(item.title)
        posterPath.set(item.posterPath)
        originalLanguage.set(item.originalLanguage)
        releaseDate.set(item.releaseDate)
    }

    fun onItemClicked(view: View) {
        activityCallbacks.fetchActivity().onCreateProductsItemClicked(view)
    }

    fun onDeleteClicked(view: View) {
        activityCallbacks.fetchActivity().onCreateProductsDeleteClicked(view)
    }

    fun onEditClicked(view: View) {
        activityCallbacks.fetchActivity().onCreateProductsEditClicked(view)
    }

}