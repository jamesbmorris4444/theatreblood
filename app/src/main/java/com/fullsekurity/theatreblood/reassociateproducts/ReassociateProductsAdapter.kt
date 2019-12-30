package com.fullsekurity.theatreblood.reassociateproducts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.ActivityCallbacks
import com.fullsekurity.theatreblood.createproducts.CreateProductsItemViewModel
import com.fullsekurity.theatreblood.databinding.DonorsItemBinding
import com.fullsekurity.theatreblood.databinding.ProductsItemBinding
import com.fullsekurity.theatreblood.databinding.ReassociateLabelItemBinding
import com.fullsekurity.theatreblood.databinding.ReassociateSearchItemBinding
import com.fullsekurity.theatreblood.donateproducts.DonateProductsItemViewModel
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewFilterAdapter
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewItemViewModel
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.repository.storage.Product
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.google.android.material.textfield.TextInputLayout

class ReassociateProductsAdapter(private val activityCallbacks: ActivityCallbacks) : RecyclerViewFilterAdapter<Any, RecyclerViewItemViewModel<Any>>(activityCallbacks.fetchActivity().applicationContext) {

    private var itemsFilter: ItemsFilter? = null
    lateinit var uiViewModel: UIViewModel
    private lateinit var searchRootView: View

    override fun getFilter(): ItemsFilter {
        itemsFilter?.let {
            return it
        }
        return ItemsFilter()
    }

    enum class ViewTypes {
        LABEL,
        SEARCH,
        DONOR,
        PRODUCT
    }

    override fun getItemViewType(position: Int): Int {
        return when(itemList[position]) {
            is ReassociateProductsLabelData -> ViewTypes.LABEL.ordinal
            is ReassociateProductsSearchData -> ViewTypes.SEARCH.ordinal
            is Donor -> ViewTypes.DONOR.ordinal
            is Product -> ViewTypes.PRODUCT.ordinal
            else -> ViewTypes.LABEL.ordinal
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder<Any, RecyclerViewItemViewModel<Any>> {
        when (viewType) {
            ViewTypes.LABEL.ordinal -> {
                val reassociateLabelItemBinding: ReassociateLabelItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.reassociate_label_item, parent, false)
                val reassociateProductsLabelItemViewModel = ReassociateProductsLabelItemViewModel(activityCallbacks)
                reassociateLabelItemBinding.reassociateProductsLabelItemViewModel = reassociateProductsLabelItemViewModel
                reassociateLabelItemBinding.uiViewModel = uiViewModel
                return ReassociateLabelViewHolder(reassociateLabelItemBinding.root, reassociateProductsLabelItemViewModel as RecyclerViewItemViewModel<Any>, reassociateLabelItemBinding)
            }
            ViewTypes.SEARCH.ordinal -> {
                val reassociateSearchItemBinding: ReassociateSearchItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.reassociate_search_item, parent, false)
                val reassociateProductsSearchItemViewModel = ReassociateProductsSearchItemViewModel(activityCallbacks)
                reassociateSearchItemBinding.reassociateProductsSearchItemViewModel = reassociateProductsSearchItemViewModel
                reassociateSearchItemBinding.uiViewModel = uiViewModel
                searchRootView = reassociateSearchItemBinding.root
                reassociateSearchItemBinding.root.findViewById<TextInputLayout>(R.id.edit_text_input_name).setHintTextAppearance(uiViewModel.editTextDisplayModifyHintStyle)
                return ReassociateSearchViewHolder(reassociateSearchItemBinding.root, reassociateProductsSearchItemViewModel as RecyclerViewItemViewModel<Any>, reassociateSearchItemBinding)

            }
            ViewTypes.DONOR.ordinal -> {
                val donorsItemBinding: DonorsItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.donors_item, parent, false)
                val donateProductsItemViewModel = DonateProductsItemViewModel(activityCallbacks)
                donorsItemBinding.donateProductsItemViewModel = donateProductsItemViewModel
                donorsItemBinding.uiViewModel = uiViewModel
                return ReassociateDonorViewHolder(donorsItemBinding.root, donateProductsItemViewModel as RecyclerViewItemViewModel<Any>, donorsItemBinding)

            }
            ViewTypes.PRODUCT.ordinal -> {
                val productsItemBinding: ProductsItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.products_item, parent, false)
                val createProductsItemViewModel = CreateProductsItemViewModel(activityCallbacks)
                productsItemBinding.createProductsItemViewModel = createProductsItemViewModel
                productsItemBinding.uiViewModel = uiViewModel
                return ReassociateProductViewHolder(productsItemBinding.root, createProductsItemViewModel as RecyclerViewItemViewModel<Any>, productsItemBinding)

            }
            else -> {
                val reassociateLabelItemBinding: ReassociateLabelItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.reassociate_label_item, parent, false)
                val reassociateProductsLabelItemViewModel = ReassociateProductsLabelItemViewModel(activityCallbacks)
                reassociateLabelItemBinding.reassociateProductsLabelItemViewModel = reassociateProductsLabelItemViewModel
                reassociateLabelItemBinding.uiViewModel = uiViewModel
                return ReassociateLabelViewHolder(reassociateLabelItemBinding.root, reassociateProductsLabelItemViewModel as RecyclerViewItemViewModel<Any>, reassociateLabelItemBinding)
            }
        }
    }

    inner class ReassociateLabelViewHolder internal constructor(itemView: View, viewModel: RecyclerViewItemViewModel<Any>, viewDataBinding: ReassociateLabelItemBinding) :
        ItemViewHolder<Any, RecyclerViewItemViewModel<Any>> (itemView, viewModel, viewDataBinding)

    inner class ReassociateSearchViewHolder internal constructor(itemView: View, viewModel: RecyclerViewItemViewModel<Any>, viewDataBinding: ReassociateSearchItemBinding) :
        ItemViewHolder<Any, RecyclerViewItemViewModel<Any>> (itemView, viewModel, viewDataBinding)
    
    inner class ReassociateDonorViewHolder internal constructor(itemView: View, viewModel: RecyclerViewItemViewModel<Any>, viewDataBinding: DonorsItemBinding) :
        ItemViewHolder<Any, RecyclerViewItemViewModel<Any>> (itemView, viewModel, viewDataBinding)

    inner class ReassociateProductViewHolder internal constructor(itemView: View, viewModel: RecyclerViewItemViewModel<Any>, viewDataBinding: ProductsItemBinding) :
        ItemViewHolder<Any, RecyclerViewItemViewModel<Any>> (itemView, viewModel, viewDataBinding)

    override fun onBindViewHolder(holder: ItemViewHolder<Any, RecyclerViewItemViewModel<Any>>, position: Int) {
//        if (getItemViewType(position) == ViewTypes.SEARCH.ordinal) {
//            holder.itemView.findViewById<ImageView>(R.id.create_product_delete_button).tag = position // delete button
//            holder.itemView.findViewById<ImageView>(R.id.create_product_edit_button).tag = position // edit button
//        }
        super.onBindViewHolder(holder, position)
//        if (position % 2 == 1) {
//            holder.itemView.setBackgroundColor(Color.parseColor(uiViewModel.recyclerViewAlternatingColor1))
//        } else {
//            holder.itemView.setBackgroundColor(Color.parseColor(uiViewModel.recyclerViewAlternatingColor2))
//        }
    }

}