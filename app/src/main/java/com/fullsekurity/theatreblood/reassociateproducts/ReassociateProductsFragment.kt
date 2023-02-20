package com.fullsekurity.theatreblood.reassociateproducts

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
import com.fullsekurity.theatreblood.createproducts.CreateProductsFragment
import com.fullsekurity.theatreblood.createproducts.CreateProductsListViewModel
import com.fullsekurity.theatreblood.databinding.ReassociateProductsFragmentBinding
import com.fullsekurity.theatreblood.donateproducts.DonateProductsFragment
import com.fullsekurity.theatreblood.donateproducts.DonateProductsListViewModel
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewFragment
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.repository.storage.Product
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.Constants
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.Utils
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import com.fullsekurity.theatreblood.viewdonorlist.ViewDonorListListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class ReassociateProductsFragment : RecyclerViewFragment(), Callbacks {

    lateinit var reassociateProductsListViewModel: ReassociateProductsListViewModel
    private lateinit var lottieBackgroundView: LottieAnimationView
    private lateinit var binding: ReassociateProductsFragmentBinding
    private lateinit var mainActivity: MainActivity

    companion object {
        fun newInstance(): ReassociateProductsFragment { return ReassociateProductsFragment() }
    }

    @Inject
    lateinit var uiViewModel: UIViewModel
    @Inject
    lateinit var repository: Repository

    private var listener = object :
        ReassociateProductsListViewModel.ReassociateProductsClickListener {
        override fun onItemClick(view: View, position: Int, search: Boolean) {
            if (search) {
                reassociateProductsListViewModel.handleReassociateSearchClick(view)
            } else {
                fetchActivity().reassociateOnNewDonorClicked(view)
                repository.newDonorInProgress = true
            }
        }
    }
    private var donateListener = object : DonateProductsFragment.DonateProductsClickListener {
        override fun onItemClick(view: View, donorWithRemovedProduct: Donor?, position: Int) {
            Utils.hideKeyboard(view)
            LogUtils.D("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("donateListener listener=%d      %s", position, donorWithRemovedProduct))
            if (donorWithRemovedProduct == null) {
                reassociateProductsListViewModel.handleReassociateDonorClick(adapter.itemList[position] as Donor, position)
            } else {
                if (donorWithRemovedProduct.firstName == (adapter.itemList[position] as Donor).firstName && donorWithRemovedProduct.lastName == (adapter.itemList[position] as Donor).lastName) {
                    reassociateProductsListViewModel.handleReassociateDonorClick(adapter.itemList[position] as Donor, position)
                }
            }
        }
    }
    private var createListener = object : CreateProductsFragment.CreateProductsClickListener {
        override fun onItemClick(view: View, position: Int, editor: Boolean) {
            (adapter.itemList[position] as Product).removedForReassociation = true
            view.visibility = View.GONE
        }
    }
    override lateinit var adapter: ReassociateProductsAdapter
    override val itemDecorator: RecyclerView.ItemDecoration? = null

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
        if (repository.newDonor != null) {
            // re-entry to reassociateProductsListViewModel after creating a new donor, which is now stored in repository.newDonor
            reassociateProductsListViewModel.initializeView()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.reassociate_products_fragment, container, false) as ReassociateProductsFragmentBinding
        binding.lifecycleOwner = this
        reassociateProductsListViewModel = ViewModelProvider(this, ReassociateProductsListViewModelFactory(this)).get(ReassociateProductsListViewModel::class.java)
        binding.reassociateProductsListViewModel = reassociateProductsListViewModel
        binding.reassociateProductsFragment = this
        binding.uiViewModel = uiViewModel
        uiViewModel.currentTheme = (activity as MainActivity).currentTheme
        //lottieBackgroundView = binding.root.findViewById(R.id.background_lottie)
        //uiViewModel.lottieAnimation(lottieBackgroundView, uiViewModel.backgroundLottieJsonFileName, LottieDrawable.INFINITE)
        reassociateProductsListViewModel.initializeView()
        reassociateProductsListViewModel.getLiveReassociateListEvent().observe(viewLifecycleOwner) { list -> adapter.addAll(list, ReassociateProductsListViewModel.ReassociateProductaListDiffCallback(adapter, adapter.itemList, list)) }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as MainActivity
        adapter = ReassociateProductsAdapter(this, listener, donateListener, createListener)
        adapter.uiViewModel = uiViewModel
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
    override fun fetchDonateProductsListViewModel() : DonateProductsListViewModel? { return null }

    override fun fetchReassociateProductsListViewModel() : ReassociateProductsListViewModel? {
        return reassociateProductsListViewModel
    }

    override fun fetchViewDonorListViewModel() : ViewDonorListListViewModel? { return null }

}