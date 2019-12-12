package com.fullsekurity.theatreblood.donors

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
import com.fullsekurity.theatreblood.databinding.DonorsScreenBinding
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import javax.inject.Inject

class DonorsFragment : Fragment() {

    private lateinit var viewModel: DonorsListViewModel

    companion object {
        fun newInstance(): DonorsFragment { return DonorsFragment() }
    }

    @Inject
    lateinit var uiViewModel: UIViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(activity as MainActivity))
            .build()
            .inject(this)
        val binding: DonorsScreenBinding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.donors_screen, container, false) as DonorsScreenBinding
        binding.lifecycleOwner = this
        viewModel = ViewModelProviders.of(this, DonorsListViewModelFactory(activity as MainActivity)).get(DonorsListViewModel::class.java)
        binding.donorsListViewModel = viewModel
        uiViewModel.currentTheme = (activity as MainActivity).currentTheme
        return binding.root
    }

//    override fun onResume() {
//        super.onResume()
//        uiViewModel.currentTheme = (activity as MainActivity).currentTheme
//    }

}