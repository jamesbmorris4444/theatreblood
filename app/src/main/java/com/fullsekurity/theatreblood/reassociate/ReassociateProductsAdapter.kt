package com.fullsekurity.theatreblood.reassociate

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.ActivityCallbacks
import com.fullsekurity.theatreblood.databinding.ReassociateItemBinding
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewFilterAdapter
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.ui.UIViewModel

class ReassociateProductsAdapter(private val activityCallbacks: ActivityCallbacks) : RecyclerViewFilterAdapter<Donor, ReassociateProductsItemViewModel>(activityCallbacks.fetchActivity().applicationContext) {

    private var itemsFilter: ItemsFilter? = null
    lateinit var uiViewModel: UIViewModel

    override fun getFilter(): ItemsFilter {
        itemsFilter?.let {
            return it
        }
        return ItemsFilter()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReassociateViewHolder {
        val reassociateItemBinding: ReassociateItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.reassociate_item, parent, false)
        val reassociateItemViewModel = ReassociateProductsItemViewModel(activityCallbacks)
        reassociateItemBinding.reassociateProductsItemViewModel = reassociateItemViewModel
        reassociateItemBinding.uiViewModel = uiViewModel
        return ReassociateViewHolder(reassociateItemBinding.root, reassociateItemViewModel, reassociateItemBinding)
    }

    inner class ReassociateViewHolder internal constructor(itemView: View, viewModel: ReassociateProductsItemViewModel, viewDataBinding: ReassociateItemBinding) :
        ItemViewHolder<Donor, ReassociateProductsItemViewModel> (itemView, viewModel, viewDataBinding)

    override fun onBindViewHolder(holder: ItemViewHolder<Donor, ReassociateProductsItemViewModel>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor(uiViewModel.recyclerViewAlternatingColor1))
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor(uiViewModel.recyclerViewAlternatingColor2))
        }
    }

}