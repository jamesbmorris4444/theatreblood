package com.fullsekurity.theatreblood.reassociateproducts

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.Callbacks
import com.fullsekurity.theatreblood.createproducts.CreateProductsItemViewModel
import com.fullsekurity.theatreblood.createproducts.CreateProductsListViewModel
import com.fullsekurity.theatreblood.databinding.CreateProductsListItemBinding
import com.fullsekurity.theatreblood.databinding.DonateProductsListItemBinding
import com.fullsekurity.theatreblood.databinding.ReassociateLabelItemBinding
import com.fullsekurity.theatreblood.databinding.ReassociateSearchItemBinding
import com.fullsekurity.theatreblood.donateproducts.DonateProductsItemViewModel
import com.fullsekurity.theatreblood.donateproducts.DonateProductsListViewModel
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewFilterAdapter
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewItemViewModel
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.repository.storage.Product
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import com.google.android.material.textfield.TextInputLayout
import javax.inject.Inject

class ReassociateProductsAdapter(private val callbacks: Callbacks,
                                 private val listener: ReassociateProductsListViewModel.ReassociateProductsClickListener,
                                 private val donateListener: DonateProductsListViewModel.DonateProductsClickListener,
                                 private val createListener: CreateProductsListViewModel.CreateProductsClickListener
                                 ) : RecyclerViewFilterAdapter<Any, RecyclerViewItemViewModel<Any>>() {

    private var adapterFilter: AdapterFilter? = null
    lateinit var uiViewModel: UIViewModel
    private lateinit var searchRootView: View

    @Inject
    lateinit var repository: Repository

    init {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(callbacks.fetchActivity()))
            .build()
            .inject(this)
    }

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
        when (getItemViewType(position)) {
            ViewTypes.LABEL.ordinal -> { }
            ViewTypes.SEARCH.ordinal -> { }
            ViewTypes.DONOR.ordinal -> {
                if (getItemViewType(0) == ViewTypes.LABEL.ordinal) {
                    holder.itemView.setOnClickListener {
                        donateListener.onItemClick(holder.itemView, null, position)
                    }
                } else {
                    holder.itemView.setOnClickListener {
                        var donorClicked: Donor? = null
                        val foundDonor = itemList.find { item ->
                            if (item is Donor) {
                                var foundProductRemoved = false
                                repository.donorsWithProductsListForReassociate.find { donorWithProducts ->
                                    foundProductRemoved = donorWithProducts.products.find { product -> product.removedForReassociation } != null
                                    if (foundProductRemoved) {
                                        donorClicked = donorWithProducts.donor
                                    }
                                    foundProductRemoved
                                }
                                foundProductRemoved
                            } else {
                                false
                            }
                        }
                        foundDonor?.let {
                            donorClicked?.let { donor ->
                                donateListener.onItemClick(holder.itemView, donor, position)
                            }
                        }
                    }
                }
            }
            ViewTypes.PRODUCT.ordinal -> {
                if (getItemViewType(0) == ViewTypes.LABEL.ordinal) {
                    val deleteView = holder.itemView.findViewById<ImageView>(R.id.create_product_delete_button)
                    deleteView.visibility = View.GONE
                } else {
                    val deleteView = holder.itemView.findViewById<ImageView>(R.id.create_product_delete_button)
                    deleteView?.setOnClickListener { // delete button
                        createListener.onItemClick(deleteView, position, false)
                    }
                    deleteView.visibility = View.VISIBLE
                    (itemList[position] as Product).removedForReassociation = false
                }
            }
            else -> { }
        }
        val submitView = holder.itemView.findViewById<ImageView>(R.id.submit_button_icon)
        val newDonorView = holder.itemView.findViewById<ImageView>(R.id.new_donor_button_icon)
        submitView?.setOnClickListener { // search button
            listener.onItemClick(submitView, position, true)
        }
        newDonorView?.setOnClickListener { // edit button
            listener.onItemClick(newDonorView, position, false)
        }
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor(uiViewModel.recyclerViewAlternatingColor1))
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor(uiViewModel.recyclerViewAlternatingColor2))
        }
    }

    override fun itemFilterable(item: Any, constraint: String): Boolean {
        return true
    }

}