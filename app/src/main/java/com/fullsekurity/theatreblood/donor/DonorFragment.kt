package com.fullsekurity.theatreblood.donor

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.*
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.Callbacks
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.createproducts.CreateProductsListViewModel
import com.fullsekurity.theatreblood.databinding.ManageDonorScreenBinding
import com.fullsekurity.theatreblood.donateproducts.DonateProductsListViewModel
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.logger.LogUtils.TagFilter.LOT
import com.fullsekurity.theatreblood.reassociateproducts.ReassociateProductsListViewModel
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.Constants
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import com.fullsekurity.theatreblood.viewdonorlist.ViewDonorListListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class DonorFragment : Fragment(), Callbacks {

    private lateinit var donorViewModel: DonorViewModel
    private lateinit var donor: Donor
    private lateinit var binding: ManageDonorScreenBinding
    private lateinit var mainActivity: MainActivity
    private var transitionToCreateDonation = true

    companion object {
        fun newInstance(donor: Donor, transitionToCreateDonation: Boolean): DonorFragment {
            val fragment = DonorFragment()
            fragment.donor = donor
            fragment.transitionToCreateDonation = transitionToCreateDonation
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

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).toolbar.title = if (transitionToCreateDonation) Constants.CREATE_PRODUCTS_TITLE else Constants.MANAGE_DONOR_TITLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(activity as MainActivity))
            .build()
            .inject(this)
        binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.manage_donor_screen, container, false) as ManageDonorScreenBinding
        binding.lifecycleOwner = this
        donorViewModel = ViewModelProviders.of(this, DonorViewModelFactory(this)).get(DonorViewModel::class.java)
        binding.donorViewModel = donorViewModel
        binding.uiViewModel = uiViewModel
        uiViewModel.currentTheme = (activity as MainActivity).currentTheme
        if (!::donor.isInitialized) {
            donor = Donor()
        }
        donorViewModel.setDonor(donor)
        donorViewModel.initializeDonorValues(donor)
        donorViewModel.transitionToCreateDonation = transitionToCreateDonation
        setupLottieDrawables(binding.root)
        return binding.root
    }

    private fun setupLottieDrawables(view: View) {
        val rootView = view.findViewById(R.id.calendar_icon) as LottieAnimationView
        val task: LottieTask<LottieComposition> = LottieCompositionFactory.fromRawRes(context, R.raw.calendar_icon)
        val lottieDrawable = LottieDrawable()
        task.addListener { result ->
            lottieDrawable.composition = result
            lottieDrawable.repeatCount = LottieDrawable.INFINITE
            lottieDrawable.playAnimation()
            lottieDrawable.scale = 0.35f
            lottieDrawable.speed = 2.0f
            rootView.setImageDrawable(lottieDrawable)
        }
        task.addFailureListener { result ->
            LogUtils.E(LogUtils.FilterTags.withTags(LOT), "Lottie Drawable Failure", result)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as MainActivity
    }

    override fun fetchActivity(): MainActivity {
        return if (::mainActivity.isInitialized) {
            mainActivity
        } else {
            activity as MainActivity
        }
    }

    override fun fetchRootView(): View {
        return binding.root
    }

    override fun fetchRadioButton(resId:Int): RadioButton {
        return fetchRootView().findViewById(resId)
    }

    override fun fetchDropdown(resId: Int) : Spinner? {
        return fetchRootView().findViewById(resId)
    }

    override fun fetchCreateProductsListViewModel() : CreateProductsListViewModel? { return null }
    override fun fetchDonateProductsListViewModel() : DonateProductsListViewModel? { return null }
    override fun fetchReassociateProductsListViewModel() : ReassociateProductsListViewModel? { return null }
    override fun fetchViewDonorListViewModel() : ViewDonorListListViewModel? { return null }

}