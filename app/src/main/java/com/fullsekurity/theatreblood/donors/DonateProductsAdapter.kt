package com.fullsekurity.theatreblood.donors

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.ActivityCallbacks
import com.fullsekurity.theatreblood.databinding.DonorsItemBinding
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewFilterAdapter
import com.fullsekurity.theatreblood.ui.UIViewModel

class DonateProductsAdapter(private val activityCallbacks: ActivityCallbacks) : RecyclerViewFilterAdapter<Donor, DonateProductsItemViewModel>(activityCallbacks.fetchActivity().applicationContext) {

    private var itemsFilter: ItemsFilter? = null
    lateinit var uiViewModel: UIViewModel

    override fun getFilter(): ItemsFilter {
        itemsFilter?.let {
            return it
        }
        return ItemsFilter()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonorsViewHolder {
        val donorsItemBinding: DonorsItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.donors_item, parent, false)
        val donorsItemViewModel = DonateProductsItemViewModel(activityCallbacks)
        donorsItemBinding.donorsItemViewModel = donorsItemViewModel
        donorsItemBinding.uiViewModel = uiViewModel
        return DonorsViewHolder(donorsItemBinding.root, donorsItemViewModel, donorsItemBinding)
    }

    inner class DonorsViewHolder internal constructor(itemView: View, viewModel: DonateProductsItemViewModel, viewDataBinding: DonorsItemBinding) :
        ItemViewHolder<Donor, DonateProductsItemViewModel> (itemView, viewModel, viewDataBinding)

    override fun onBindViewHolder(holder: ItemViewHolder<Donor, DonateProductsItemViewModel>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor(uiViewModel.recyclerViewAlternatingColor1))
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor(uiViewModel.recyclerViewAlternatingColor2))
        }
    }

}