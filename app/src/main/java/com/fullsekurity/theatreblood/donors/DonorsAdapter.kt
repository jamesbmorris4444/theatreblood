package com.fullsekurity.theatreblood.donors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.databinding.DonorsItemBinding
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewFilterAdapter
import com.fullsekurity.theatreblood.repository.storage.Donor

class DonorsAdapter(val activity: MainActivity) : RecyclerViewFilterAdapter<Donor, DonorsItemViewModel>(activity.applicationContext) {

    private var itemsFilter: ItemsFilter? = null

    override fun getFilter(): ItemsFilter {
        itemsFilter?.let {
            return it
        }
        return ItemsFilter()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonorsViewHolder {
        val donorsItemBinding: DonorsItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.donors_item, parent, false)
        val donorsItemViewModel = DonorsItemViewModel(activity.application)
        donorsItemBinding.donorsItemViewModel = donorsItemViewModel
        return DonorsViewHolder(donorsItemBinding.root, donorsItemViewModel, donorsItemBinding)
    }

    inner class DonorsViewHolder internal constructor(
        itemView: View,
        viewModel: DonorsItemViewModel,
        viewDataBinding: DonorsItemBinding
    ) : ItemViewHolder<Donor, DonorsItemViewModel> (
        itemView,
        viewModel,
        viewDataBinding
    )

}
