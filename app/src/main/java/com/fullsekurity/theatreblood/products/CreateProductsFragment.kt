package com.fullsekurity.theatreblood.products

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.ActivityCallbacks
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.databinding.CreateProductsScreenBinding
import com.fullsekurity.theatreblood.donors.Donor
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.Constants
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class CreateProductsFragment : Fragment(), ActivityCallbacks {

    private lateinit var createProductsListViewModel: CreateProductsListViewModel
    private lateinit var donor: Donor
    private lateinit var binding: CreateProductsScreenBinding

    companion object {
        fun newInstance(donor: Donor): CreateProductsFragment {
            val fragment = CreateProductsFragment()
            fragment.donor = donor
            return fragment
        }
    }

    @Inject
    lateinit var uiViewModel: UIViewModel

    override fun onAttach(context: Context) {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(activity as MainActivity))
            .build()
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.create_products_screen, container, false) as CreateProductsScreenBinding
        binding.lifecycleOwner = this
        createProductsListViewModel = ViewModelProviders.of(this, CreateProductsListViewModelFactory(activity as MainActivity)).get(CreateProductsListViewModel::class.java)
        binding.createProductsListViewModel = createProductsListViewModel
        binding.uiViewModel = uiViewModel
        uiViewModel.currentTheme = (activity as MainActivity).currentTheme
        createProductsListViewModel.setDonor(donor)
        (activity as MainActivity).toolbar.title = Constants.CREATE_PRODUCTS_TITLE
        return binding.root
    }

    override fun fetchActivity(): MainActivity {
        return activity as MainActivity
    }

    override fun fetchRootView(): View {
        return binding.root
    }

    override fun fetchRadioButton(resId: Int): RadioButton {
        return fetchRootView().findViewById(resId)
    }

}