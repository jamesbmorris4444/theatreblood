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

class DonorsAdapter(val activity: MainActivity, context: Context) : RecyclerViewFilterAdapter<DonorsDataModel, DonorsItemViewModel>(context) {

    private var donorList: List<DonorsDataModel> = arrayListOf()
    private var itemsFilter: ItemsFilter? = null

    override fun getFilter(): ItemsFilter {
        itemsFilter?.let {
            return it
        }
        return ItemsFilter()
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder<DonorsDataModel, DonorsItemViewModel>,
        position: Int
    ) { super.onBindViewHolder(holder, position) }

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
    ) : ItemViewHolder<DonorsDataModel, DonorsItemViewModel> (
        itemView,
        viewModel,
        viewDataBinding
    )

}
