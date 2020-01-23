package com.fullsekurity.theatreblood.donateproducts

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.Callbacks
import com.fullsekurity.theatreblood.databinding.DonateProductsListItemBinding
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewFilterAdapter
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.Utils

class DonateProductsAdapter(private val callbacks: Callbacks) : RecyclerViewFilterAdapter<Donor, DonateProductsItemViewModel>() {

    private var adapterFilter: AdapterFilter? = null
    lateinit var uiViewModel: UIViewModel

    override fun getFilter(): AdapterFilter {
        adapterFilter?.let {
            return it
        }
        return AdapterFilter()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonorsViewHolder {
        val donateProductsListItemBinding: DonateProductsListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.donate_products_list_item, parent, false)
        val donateProductsItemViewModel = DonateProductsItemViewModel(callbacks)
        donateProductsListItemBinding.donateProductsItemViewModel = donateProductsItemViewModel
        donateProductsListItemBinding.uiViewModel = uiViewModel
        return DonorsViewHolder(donateProductsListItemBinding.root, donateProductsItemViewModel, donateProductsListItemBinding)
    }

    inner class DonorsViewHolder internal constructor(itemView: View, viewModel: DonateProductsItemViewModel, viewDataBinding: DonateProductsListItemBinding) :
        ItemViewHolder<Donor, DonateProductsItemViewModel> (itemView, viewModel, viewDataBinding)

    override fun onBindViewHolder(holder: ItemViewHolder<Donor, DonateProductsItemViewModel>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor(uiViewModel.recyclerViewAlternatingColor1))
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor(uiViewModel.recyclerViewAlternatingColor2))
        }
    }

    override fun itemFilterable(donor: Donor, patternOfSubpatterns: String): Boolean {
        var returnValue = true
        var constraint = Utils.getPatternOfSubpatterns(patternOfSubpatterns, 0)
        if (constraint != "<>") {
            val regexPattern: String
            val index = constraint.indexOf(',')
            if (index < 0) {
                regexPattern = "^$constraint.*"
            } else {
                val last = constraint.substring(0, index)
                val first = constraint.substring(index + 1)
                regexPattern = "^$last.*,$first.*"
            }
            val regex = Regex(regexPattern, setOf(RegexOption.IGNORE_CASE))
            val target = donor.lastName + "," + donor.firstName
            returnValue = returnValue && regex.matches(target)  // must match entire target string
        }
        if (!returnValue) {
            return false
        }
        constraint = Utils.getPatternOfSubpatterns(patternOfSubpatterns, 1)
        if (constraint != "<>") {
            returnValue = returnValue && constraint.equals(donor.aboRh, ignoreCase = true)
        }
        return returnValue
    }

}