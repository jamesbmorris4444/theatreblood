package com.fullsekurity.theatreblood.reassociateproducts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.Callbacks
import com.fullsekurity.theatreblood.createproducts.CreateProductsItemViewModel
import com.fullsekurity.theatreblood.databinding.CreateProductsListItemBinding
import com.fullsekurity.theatreblood.databinding.DonateProductsListItemBinding
import com.fullsekurity.theatreblood.databinding.ReassociateLabelItemBinding
import com.fullsekurity.theatreblood.databinding.ReassociateSearchItemBinding
import com.fullsekurity.theatreblood.donateproducts.DonateProductsItemViewModel
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewFilterAdapter
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewItemViewModel
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.repository.storage.Product
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.google.android.material.textfield.TextInputLayout

class ReassociateProductsAdapter(private val callbacks: Callbacks) : RecyclerViewFilterAdapter<Any, RecyclerViewItemViewModel<Any>>() {

    private var adapterFilter: AdapterFilter? = null
    lateinit var uiViewModel: UIViewModel
    private lateinit var searchRootView: View

    override fun getFilter(): AdapterFilter {
        adapterFilter?.let {
            return it
        }
        return AdapterFilter()
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
                val reassociateProductsLabelItemViewModel = ReassociateProductsLabelItemViewModel(callbacks)
                reassociateLabelItemBinding.reassociateProductsLabelItemViewModel = reassociateProductsLabelItemViewModel
                reassociateLabelItemBinding.uiViewModel = uiViewModel
                return ReassociateLabelViewHolder(reassociateLabelItemBinding.root, reassociateProductsLabelItemViewModel as RecyclerViewItemViewModel<Any>, reassociateLabelItemBinding)
            }
            ViewTypes.SEARCH.ordinal -> {
                val reassociateSearchItemBinding: ReassociateSearchItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.reassociate_search_item, parent, false)
                val reassociateProductsSearchItemViewModel = ReassociateProductsSearchItemViewModel(callbacks)
                reassociateSearchItemBinding.reassociateProductsSearchItemViewModel = reassociateProductsSearchItemViewModel
                reassociateSearchItemBinding.uiViewModel = uiViewModel
                searchRootView = reassociateSearchItemBinding.root
                reassociateSearchItemBinding.root.findViewById<TextInputLayout>(R.id.edit_text_input_name).setHintTextAppearance(uiViewModel.editTextDisplayModifyHintStyle)
                return ReassociateSearchViewHolder(reassociateSearchItemBinding.root, reassociateProductsSearchItemViewModel as RecyclerViewItemViewModel<Any>, reassociateSearchItemBinding)

            }
            ViewTypes.DONOR.ordinal -> {
                val donateProductsListItemBinding: DonateProductsListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.donate_products_list_item, parent, false)
                val donateProductsItemViewModel = DonateProductsItemViewModel(callbacks)
                donateProductsListItemBinding.donateProductsItemViewModel = donateProductsItemViewModel
                donateProductsListItemBinding.uiViewModel = uiViewModel
                return ReassociateDonorViewHolder(donateProductsListItemBinding.root, donateProductsItemViewModel as RecyclerViewItemViewModel<Any>, donateProductsListItemBinding)

            }
            ViewTypes.PRODUCT.ordinal -> {
                val createProductsListItemBinding: CreateProductsListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.create_products_list_item, parent, false)
                val createProductsItemViewModel = CreateProductsItemViewModel(callbacks)
                createProductsListItemBinding.createProductsItemViewModel = createProductsItemViewModel
                createProductsListItemBinding.uiViewModel = uiViewModel
                return ReassociateProductViewHolder(createProductsListItemBinding.root, createProductsItemViewModel as RecyclerViewItemViewModel<Any>, createProductsListItemBinding)

            }
            else -> {
                val reassociateLabelItemBinding: ReassociateLabelItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.reassociate_label_item, parent, false)
                val reassociateProductsLabelItemViewModel = ReassociateProductsLabelItemViewModel(callbacks)
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
    
    inner class ReassociateDonorViewHolder internal constructor(itemView: View, viewModel: RecyclerViewItemViewModel<Any>, viewDataBinding: DonateProductsListItemBinding) :
        ItemViewHolder<Any, RecyclerViewItemViewModel<Any>> (itemView, viewModel, viewDataBinding)

    inner class ReassociateProductViewHolder internal constructor(itemView: View, viewModel: RecyclerViewItemViewModel<Any>, viewDataBinding: CreateProductsListItemBinding) :
        ItemViewHolder<Any, RecyclerViewItemViewModel<Any>> (itemView, viewModel, viewDataBinding)

    override fun onBindViewHolder(holder: ItemViewHolder<Any, RecyclerViewItemViewModel<Any>>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder.itemViewType == ViewTypes.DONOR.ordinal) {
            holder.itemView.findViewById<ConstraintLayout>(R.id.donate_products_item_root_view).tag = position
        }
    }

    override fun itemFilterable(item: Any, constraint: String): Boolean {
        return true
    }

}