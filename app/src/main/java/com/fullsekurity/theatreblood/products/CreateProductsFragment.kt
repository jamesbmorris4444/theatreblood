package com.fullsekurity.theatreblood.products

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.databinding.CreateProductsScreenBinding
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import javax.inject.Inject

class CreateProductsFragment : Fragment() {

    private lateinit var createProductsListViewModel: CreateProductsListViewModel

    companion object {
        fun newInstance(): CreateProductsFragment { return CreateProductsFragment() }
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
        val binding: CreateProductsScreenBinding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.create_products_screen, container, false) as CreateProductsScreenBinding
        binding.lifecycleOwner = this
        createProductsListViewModel = ViewModelProviders.of(this, CreateProductsListViewModelFactory(activity as MainActivity)).get(CreateProductsListViewModel::class.java)
        binding.createProductsListViewModel = createProductsListViewModel
        binding.uiViewModel = uiViewModel
        uiViewModel.currentTheme = (activity as MainActivity).currentTheme
        createProductsListViewModel.setRootView(binding.root)
        return binding.root
    }

}