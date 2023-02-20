package com.fullsekurity.theatreblood.donateproducts

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.Callbacks
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.createproducts.CreateProductsListViewModel
import com.fullsekurity.theatreblood.databinding.DonateProductsFragmentBinding
import com.fullsekurity.theatreblood.reassociateproducts.ReassociateProductsListViewModel
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewFragment
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.Constants
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.Utils
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import com.fullsekurity.theatreblood.viewdonorlist.ViewDonorListListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class DonateProductsFragment : RecyclerViewFragment(), Callbacks {

    private lateinit var donateProductsListViewModel: DonateProductsListViewModel
    private lateinit var lottieBackgroundView: LottieAnimationView
    private lateinit var binding: DonateProductsFragmentBinding
    private lateinit var mainActivity: MainActivity
    private var transitionToCreateDonation = true

    private val listener = object : DonateProductsClickListener {
        override fun onItemClick(view: View, donorWithRemovedProduct: Donor?, position: Int) {
            Utils.hideKeyboard(view)
            fetchActivity().loadDonorFragment(adapter.itemList[position], fetchActivity().transitionToCreateDonation)
        }
    }
    override var adapter: DonateProductsAdapter = DonateProductsAdapter(this, listener)
    override val itemDecorator: RecyclerView.ItemDecoration? = null

    companion object {
        fun newInstance(transitionToCreateDonation: Boolean): DonateProductsFragment {
            val bundle = Bundle()
            bundle.putBoolean("transitionToCreateDonationArgument", transitionToCreateDonation)
            val fragment = DonateProductsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private fun readBundle(bundle: Bundle?) {
        bundle?.let {
            transitionToCreateDonation = it.getBoolean("transitionToCreateDonationArgument")
        }
    }

    @Inject
    lateinit var uiViewModel: UIViewModel
    @Inject
    lateinit var repository: Repository

    override fun onAttach(context: Context) {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(activity as MainActivity))
            .build()
            .inject(this)
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).toolbar.title = if (transitionToCreateDonation) Constants.DONATE_PRODUCTS_TITLE else Constants.MANAGE_DONOR_TITLE
        donateProductsListViewModel.initialize(binding.root)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        readBundle(arguments)
        binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.donate_products_fragment, container, false) as DonateProductsFragmentBinding
        binding.lifecycleOwner = this
        donateProductsListViewModel = ViewModelProvider(this, DonateProductsListViewModelFactory(this)).get(DonateProductsListViewModel::class.java)
        binding.donateProductsListViewModel = donateProductsListViewModel
        binding.uiViewModel = uiViewModel
        binding.donateProductsFragment = this
        adapter.uiViewModel = uiViewModel
        donateProductsListViewModel.transitionToCreateDonation = transitionToCreateDonation
        uiViewModel.currentTheme = (activity as MainActivity).currentTheme
        //lottieBackgroundView = binding.root.findViewById(R.id.background_lottie)
        //uiViewModel.lottieAnimation(lottieBackgroundView, uiViewModel.backgroundLottieJsonFileName, LottieDrawable.INFINITE)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun onSearchClicked(view: View) {
        Utils.hideKeyboard(view)
        repository.handleSearchClick(view, donateProductsListViewModel.editTextNameInput.get() ?: "", this::showDonors)
    }

    private fun showDonors(donorList: List<Donor>) {
        donateProductsListViewModel.listIsVisible.set(donorList.isNotEmpty())
        val newDonorList = donorList.sortedBy { donor -> Utils.donorComparisonByString(donor) }
        adapter.addAll(newDonorList,
            DonateProductsListViewModel.DonorListDiffCallback(
                adapter.itemList,
                newDonorList
            )
        )
        donateProductsListViewModel.numberOfItemsDisplayed = donorList.size
        donateProductsListViewModel.setNewDonorVisibility("NONEMPTY")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as MainActivity
    }

    override fun setLayoutManager(): RecyclerView.LayoutManager {
        return object : LinearLayoutManager(requireContext()) {
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return true
            }
        }
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

    override fun fetchFragment(): Fragment {
        return this
    }

    override fun fetchRadioButton(resId: Int): RadioButton {
        return fetchRootView().findViewById(resId)
    }

    override fun fetchDropdown(resId: Int) : Spinner? { return null }
    override fun fetchCreateProductsListViewModel() : CreateProductsListViewModel? { return null }

    override fun fetchDonateProductsListViewModel() : DonateProductsListViewModel? {
        return donateProductsListViewModel
    }

    override fun fetchReassociateProductsListViewModel() : ReassociateProductsListViewModel? { return null }
    override fun fetchViewDonorListViewModel() : ViewDonorListListViewModel? { return null }

    interface DonateProductsClickListener {
        fun onItemClick(view: View, donorWithRemovedProduct: Donor?, position: Int)
    }

}