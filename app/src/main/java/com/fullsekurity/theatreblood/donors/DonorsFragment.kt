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

class DonorsFragment : Fragment() {

    private lateinit var viewModel: DonorsListViewModel

    companion object {
        fun newInstance(): DonorsFragment { return DonorsFragment() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        DaggerMapperDependencyInjector.builder()
//            .mapperInjectorModule(MapperInjectorModule(context))
//            .build()
//            .inject(this)
        val binding: DonorsScreenBinding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.donors_screen, container, false) as DonorsScreenBinding
        binding.lifecycleOwner = this
        viewModel = ViewModelProviders.of(this, DonorsListViewModelFactory((activity as MainActivity).application)).get(DonorsListViewModel::class.java)
        binding.donorsListViewModel = viewModel
        //viewModel.liveDonorsDataObject.observe(this, Observer { viewModel.liveDataUpdate() })
        return binding.root
    }

}