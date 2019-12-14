package com.fullsekurity.theatreblood.donor

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
import com.fullsekurity.theatreblood.databinding.DonorScreenBinding
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import javax.inject.Inject

class DonorFragment : Fragment() {

    private lateinit var viewModel: DonorViewModel
    private lateinit var donor: Donor

    companion object {
        fun newInstance(donor: Donor): DonorFragment {
            val fragment = DonorFragment()
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
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(activity as MainActivity))
            .build()
            .inject(this)
        val binding: DonorScreenBinding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.donor_screen, container, false) as DonorScreenBinding
        binding.lifecycleOwner = this
        viewModel = ViewModelProviders.of(this, DonorViewModelFactory(activity as MainActivity)).get(DonorViewModel::class.java)
        binding.donorViewModel = viewModel
        binding.uiViewModel = uiViewModel
        uiViewModel.currentTheme = (activity as MainActivity).currentTheme
        viewModel.setDonor(donor)
        return binding.root
    }

}