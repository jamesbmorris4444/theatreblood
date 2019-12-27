package com.fullsekurity.theatreblood.products

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.ActivityCallbacks
import com.fullsekurity.theatreblood.databinding.ProductsItemBinding
import com.fullsekurity.theatreblood.donors.Product
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewFilterAdapter
import com.fullsekurity.theatreblood.ui.UIViewModel

class CreateProductsAdapter(val activityCallbacks: ActivityCallbacks) : RecyclerViewFilterAdapter<Product, CreateProductsItemViewModel>(activityCallbacks.fetchActivity().applicationContext) {

    private var itemsFilter: ItemsFilter? = null
    lateinit var uiViewModel: UIViewModel

    override fun getFilter(): ItemsFilter {
        itemsFilter?.let {
            return it
        }
        return ItemsFilter()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val productsItemBinding: ProductsItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.products_item, parent, false)
        val productsItemViewModel = CreateProductsItemViewModel(activityCallbacks)
        productsItemBinding.createProductsItemViewModel = productsItemViewModel
        productsItemBinding.uiViewModel = uiViewModel
        return ProductsViewHolder(productsItemBinding.root, productsItemViewModel, productsItemBinding)
    }

    inner class ProductsViewHolder internal constructor(itemView: View, viewModel: CreateProductsItemViewModel, viewDataBinding: ProductsItemBinding) :
        ItemViewHolder<Product, CreateProductsItemViewModel>(itemView, viewModel, viewDataBinding)

    override fun onBindViewHolder(holder: ItemViewHolder<Product, CreateProductsItemViewModel>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.tag = position // click anywhere in the item
        holder.itemView.findViewById<ImageView>(R.id.create_product_delete_button).tag = position // delete button
        holder.itemView.findViewById<ImageView>(R.id.create_product_edit_button).tag = position // edit button
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor(uiViewModel.recyclerViewAlternatingColor1))
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor(uiViewModel.recyclerViewAlternatingColor2))
        }
    }

}
