package com.fullsekurity.theatreblood.donors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.databinding.DonorsItemBinding
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewFilterAdapter
import com.fullsekurity.theatreblood.repository.storage.datamodel.Donor

class DonorsAdapter(val activity: MainActivity, context: Context) : RecyclerViewFilterAdapter<Donor, DonorsItemViewModel>(context) {

    private var donorList: List<Donor> = arrayListOf()
    private var itemsFilter: ItemsFilter? = null

    override fun getFilter(): ItemsFilter {
        itemsFilter?.let {
            return it
        }
        return ItemsFilter()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonorsViewHolder {
        val donorsItemBinding: DonorsItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.donors_item, parent, false)
        donorsItemBinding.lifecycleOwner = activity
        val donorsItemViewModel = ViewModelProviders.of(activity, DonorsItemViewModelFactory(activity.application)).get(DonorsItemViewModel::class.java)
        donorsItemBinding.donorsItemViewModel = donorsItemViewModel
        return DonorsViewHolder(donorsItemBinding.root, donorsItemViewModel, donorsItemBinding)
    }

    inner class DonorsViewHolder internal constructor(
        itemView: View,
        viewModel: DonorsItemViewModel,
        viewDataBinding: ViewDataBinding
    ) : ItemViewHolder<Donor, DonorsItemViewModel> (
        itemView,
        viewModel,
        viewDataBinding
    )

}
