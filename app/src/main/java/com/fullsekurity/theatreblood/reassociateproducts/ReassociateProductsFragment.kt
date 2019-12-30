package com.fullsekurity.theatreblood.reassociateproducts

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
import com.airbnb.lottie.LottieAnimationView
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.ActivityCallbacks
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.databinding.ReassociateProductsScreenBinding
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.Constants
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class ReassociateProductsFragment : Fragment(), ActivityCallbacks {

    lateinit var reassociateProductsListViewModel: ReassociateProductsListViewModel
    private lateinit var lottieBackgroundView: LottieAnimationView
    private lateinit var binding: ReassociateProductsScreenBinding

    companion object {
        fun newInstance(): ReassociateProductsFragment { return ReassociateProductsFragment() }
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

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).toolbar.title = Constants.REASSOCIATE_DONATION_TITLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.reassociate_products_screen, container, false) as ReassociateProductsScreenBinding
        binding.lifecycleOwner = this
        reassociateProductsListViewModel = ViewModelProviders.of(this, ReassociateProductsListViewModelFactory(this)).get(ReassociateProductsListViewModel::class.java)
        binding.reassociateProductsListViewModel = reassociateProductsListViewModel
        binding.uiViewModel = uiViewModel
        uiViewModel.currentTheme = (activity as MainActivity).currentTheme
        //lottieBackgroundView = binding.root.findViewById(R.id.background_lottie)
        //uiViewModel.lottieAnimation(lottieBackgroundView, uiViewModel.backgroundLottieJsonFileName, LottieDrawable.INFINITE)
        reassociateProductsListViewModel.initializeView()
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