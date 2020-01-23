package com.fullsekurity.theatreblood.createproducts

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.Callbacks
import com.fullsekurity.theatreblood.databinding.CreateProductsListItemBinding
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewFilterAdapter
import com.fullsekurity.theatreblood.repository.storage.Product
import com.fullsekurity.theatreblood.ui.UIViewModel

class CreateProductsAdapter(val callbacks: Callbacks) : RecyclerViewFilterAdapter<Product, CreateProductsItemViewModel>() {

    private var adapterFilter: AdapterFilter? = null
    lateinit var uiViewModel: UIViewModel

    override fun getFilter(): AdapterFilter {
        adapterFilter?.let {
            return it
        }
        return AdapterFilter()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val productsItemBinding: CreateProductsListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.create_products_list_item, parent, false)
        val productsItemViewModel = CreateProductsItemViewModel(callbacks)
        productsItemBinding.createProductsItemViewModel = productsItemViewModel
        productsItemBinding.uiViewModel = uiViewModel
        return ProductsViewHolder(productsItemBinding.root, productsItemViewModel, productsItemBinding)
    }

    inner class ProductsViewHolder internal constructor(itemView: View, viewModel: CreateProductsItemViewModel, viewDataBinding: CreateProductsListItemBinding) :
        ItemViewHolder<Product, CreateProductsItemViewModel>(itemView, viewModel, viewDataBinding)

    override fun onBindViewHolder(holder: ItemViewHolder<Product, CreateProductsItemViewModel>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.findViewById<ImageView>(R.id.create_product_delete_button).tag = position // delete button
        holder.itemView.findViewById<ImageView>(R.id.create_product_edit_button).tag = position // edit button
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor(uiViewModel.recyclerViewAlternatingColor1))
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor(uiViewModel.recyclerViewAlternatingColor2))
        }
    }

    override fun itemFilterable(item: Product, constraint: String): Boolean {
        return true
    }

}
