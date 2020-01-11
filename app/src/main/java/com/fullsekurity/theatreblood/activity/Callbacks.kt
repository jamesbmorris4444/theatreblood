package com.fullsekurity.theatreblood.activity

import android.view.View
import android.widget.RadioButton
import android.widget.Spinner
import com.fullsekurity.theatreblood.createproducts.CreateProductsListViewModel
import com.fullsekurity.theatreblood.donateproducts.DonateProductsListViewModel
import com.fullsekurity.theatreblood.reassociateproducts.ReassociateProductsListViewModel
import com.fullsekurity.theatreblood.viewdonorlist.ViewDonorListListViewModel

interface Callbacks {
    fun fetchActivity(): MainActivity
    fun fetchRootView() : View
    fun fetchRadioButton(resId: Int) : RadioButton?
    fun fetchDropdown(resId: Int) : Spinner?
    fun fetchCreateProductsListViewModel() : CreateProductsListViewModel?
    fun fetchDonateProductsListViewModel() : DonateProductsListViewModel?
    fun fetchReassociateProductsListViewModel() : ReassociateProductsListViewModel?
    fun fetchViewDonorListViewModel() : ViewDonorListListViewModel?
}