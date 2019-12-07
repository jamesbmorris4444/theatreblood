package com.fullsekurity.theatreblood.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.databinding.HomeScreenBinding


class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    companion object {
        fun newInstance(): HomeFragment { return HomeFragment() }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.unsubscribe()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: HomeScreenBinding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.home_screen, container, false) as HomeScreenBinding
        binding.lifecycleOwner = this
        viewModel = ViewModelProviders.of(this, HomeViewModelFactory(activity as com.fullsekurity.theatreblood.activity.MainActivity)).get(HomeViewModel::class.java)
        binding.homeViewModel = viewModel
        viewModel.getLiveHomeViewData().observe(this, Observer { viewModel.liveDataUpdate() })
        return binding.root
    }

}