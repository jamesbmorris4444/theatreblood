package com.fullsekurity.theatreblood.createproducts

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
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.*
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.Callbacks
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.databinding.CreateProductsFragmentBinding
import com.fullsekurity.theatreblood.donateproducts.DonateProductsListViewModel
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.logger.LogUtils.TagFilter.API
import com.fullsekurity.theatreblood.reassociateproducts.ReassociateProductsListViewModel
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.Constants
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import com.fullsekurity.theatreblood.viewdonorlist.ViewDonorListListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class CreateProductsFragment : Fragment(), Callbacks {

    private lateinit var createProductsListViewModel: CreateProductsListViewModel
    private lateinit var donor: Donor
    private lateinit var binding: CreateProductsFragmentBinding
    private lateinit var mainActivity: MainActivity

    companion object {
        fun newInstance(donor: Donor): CreateProductsFragment {
            val bundle = Bundle()
            bundle.putSerializable("donorArgument", donor)
            val fragment = CreateProductsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private fun readBundle(bundle: Bundle?) {
        bundle?.let {
            donor = it.getSerializable("donorArgument") as Donor
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
        (activity as MainActivity).toolbar.title = Constants.CREATE_PRODUCTS_TITLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        readBundle(arguments)
        binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.create_products_fragment, container, false) as CreateProductsFragmentBinding
        binding.lifecycleOwner = this
        createProductsListViewModel = ViewModelProvider(this, CreateProductsListViewModelFactory(this)).get(CreateProductsListViewModel::class.java)
        binding.createProductsListViewModel = createProductsListViewModel
        binding.uiViewModel = uiViewModel
        uiViewModel.currentTheme = (activity as MainActivity).currentTheme
        createProductsListViewModel.setDonor(donor)
        setupLottieDrawables(binding.root)
        return binding.root
    }

    private fun setupLottieDrawables(view: View) {
        val lottieView1 = view.findViewById(R.id.calendar_icon_22) as LottieAnimationView
        val task1: LottieTask<LottieComposition> = LottieCompositionFactory.fromRawRes(context, R.raw.calendar_icon)
        val lottieDrawable1 = LottieDrawable()
        task1.addListener { result ->
            lottieDrawable1.composition = result
            lottieDrawable1.repeatCount = LottieDrawable.INFINITE
            lottieDrawable1.playAnimation()
            lottieDrawable1.scale = 0.35f
            lottieDrawable1.speed = 2.0f
            lottieView1.setImageDrawable(lottieDrawable1)
        }
        task1.addFailureListener { result ->
            LogUtils.E(LogUtils.FilterTags.withTags(API), "Lottie Drawable Failure", result)
        }

        val lottieView2 = view.findViewById(R.id.barcode_scanner_11) as LottieAnimationView
        val task2 = LottieCompositionFactory.fromRawRes(context, R.raw.barcode_scanner)
        val lottieDrawable2 = LottieDrawable()
        task2.addListener { result ->
            lottieDrawable2.composition = result
            lottieDrawable2.repeatCount = LottieDrawable.INFINITE
            lottieDrawable2.playAnimation()
            lottieDrawable2.scale = 0.12f
            lottieView2.setImageDrawable(lottieDrawable2)
        }
        task2.addFailureListener { result ->
            LogUtils.E(LogUtils.FilterTags.withTags(API), "Lottie Drawable Failure", result)
        }

        val lottieView3 = view.findViewById(R.id.barcode_scanner_21) as LottieAnimationView
        val task3 = LottieCompositionFactory.fromRawRes(context, R.raw.barcode_scanner)
        val lottieDrawable3 = LottieDrawable()
        task3.addListener { result ->
            lottieDrawable3.composition = result
            lottieDrawable3.repeatCount = LottieDrawable.INFINITE
            lottieDrawable3.playAnimation()
            lottieDrawable3.scale = 0.12f
            lottieView3.setImageDrawable(lottieDrawable3)
        }
        task3.addFailureListener { result ->
            LogUtils.E(LogUtils.FilterTags.withTags(API), "Lottie Drawable Failure", result)
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

    override fun fetchRadioButton(resId: Int): RadioButton {
        return fetchRootView().findViewById(resId)
    }

    override fun fetchDropdown(resId: Int) : Spinner? { return null }

    override fun fetchCreateProductsListViewModel() : CreateProductsListViewModel? {
        return createProductsListViewModel
    }

    override fun fetchDonateProductsListViewModel() : DonateProductsListViewModel? { return null }
    override fun fetchReassociateProductsListViewModel() : ReassociateProductsListViewModel? { return null }
    override fun fetchViewDonorListViewModel() : ViewDonorListListViewModel? { return null }

}