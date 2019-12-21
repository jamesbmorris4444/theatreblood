package com.fullsekurity.theatreblood.donors

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.databinding.DonateProductsScreenBinding
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import javax.inject.Inject

class DonateProductsFragment : Fragment() {

    private lateinit var donateProductsListViewModel: DonateProductsListViewModel
    private lateinit var lottieBackgroundView: LottieAnimationView

    companion object {
        fun newInstance(): DonateProductsFragment { return DonateProductsFragment() }
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
        val binding: DonateProductsScreenBinding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.donate_products_screen, container, false) as DonateProductsScreenBinding
        binding.lifecycleOwner = this
        donateProductsListViewModel = ViewModelProviders.of(this, DonateProductsListViewModelFactory(activity as MainActivity)).get(DonateProductsListViewModel::class.java)
        binding.donateProductsListViewModel = donateProductsListViewModel
        binding.uiViewModel = uiViewModel
        uiViewModel.currentTheme = (activity as MainActivity).currentTheme
        donateProductsListViewModel.setRootView(binding.root)
        //lottieBackgroundView = binding.root.findViewById(R.id.background_lottie)
        //uiViewModel.lottieAnimation(lottieBackgroundView, uiViewModel.backgroundLottieJsonFileName, LottieDrawable.INFINITE)
        return binding.root
    }

}