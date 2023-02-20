package com.fullsekurity.theatreblood.viewdonorlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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
import com.fullsekurity.theatreblood.databinding.ViewDonorListFragmentBinding
import com.fullsekurity.theatreblood.donateproducts.DonateProductsAdapter
import com.fullsekurity.theatreblood.donateproducts.DonateProductsListViewModel
import com.fullsekurity.theatreblood.managedonor.ManageDonorViewModel
import com.fullsekurity.theatreblood.reassociateproducts.ReassociateProductsListViewModel
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewFragment
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.Constants
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.Utils
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class ViewDonorListFragment : RecyclerViewFragment(), Callbacks {

    private lateinit var viewDonorListListViewModel: ViewDonorListListViewModel
    private lateinit var lottieBackgroundView: LottieAnimationView
    private lateinit var binding: ViewDonorListFragmentBinding
    private lateinit var mainActivity: MainActivity

    override var adapter: DonateProductsAdapter = DonateProductsAdapter(this, null)
    override val itemDecorator: RecyclerView.ItemDecoration? = null

    companion object {
        fun newInstance(): ViewDonorListFragment {
            return ViewDonorListFragment()
        }
    }

    @Inject
    lateinit var uiViewModel: UIViewModel
    @Inject
    lateinit var repository: Repository

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

    override fun onAttach(context: Context) {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(activity as MainActivity))
            .build()
            .inject(this)
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).toolbar.title = Constants.VIEW_DONOR_LIST_TITLE
        showAllDonors(binding.root)
    }

    private fun showAllDonors(view: View) {
        repository.handleSearchClick(view, "", this::showDonors)
    }

    private fun showDonors(donorList: List<Donor>) {
        viewDonorListListViewModel.listIsVisible.set(donorList.isNotEmpty())
        val newDonorList = donorList.sortedBy { donor -> Utils.donorComparisonByString(donor) }
        adapter.addAllFiltered(newDonorList, ViewDonorListListViewModel.DonorListDiffCallback(adapter.itemList, newDonorList))
        viewDonorListListViewModel.numberOfItemsDisplayed = donorList.size
        viewDonorListListViewModel.setNewDonorVisibility("NONEMPTY")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.view_donor_list_fragment, container, false) as ViewDonorListFragmentBinding
        binding.lifecycleOwner = this
        viewDonorListListViewModel = ViewModelProvider(this, ViewDonorListListViewModelFactory(this)).get(ViewDonorListListViewModel::class.java)
        binding.viewDonorListListViewModel = viewDonorListListViewModel
        binding.viewDonorListFragment = this
        binding.uiViewModel = uiViewModel
        adapter.uiViewModel = uiViewModel
        uiViewModel.currentTheme = (activity as MainActivity).currentTheme
        //lottieBackgroundView = binding.root.findViewById(R.id.background_lottie)
        //uiViewModel.lottieAnimation(lottieBackgroundView, uiViewModel.backgroundLottieJsonFileName, LottieDrawable.INFINITE)
        binding.root.findViewById<TextInputLayout>(R.id.edit_text_input_name).setHintTextAppearance(uiViewModel.editTextDisplayModifyHintStyle)
        repository.getLiveDonorListEvent().observe(viewLifecycleOwner) { donorList -> showDonors(donorList) }
        viewDonorListListViewModel.getLiveFilterDonorListEvent().observe(viewLifecycleOwner) { patternOfSubpatterns -> adapter.filter.filter(patternOfSubpatterns) }
        setDropdowns()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as MainActivity
    }

    private fun setDropdowns() {
        fetchDropdown(R.id.abo_rh_dropdown)?.let {
            val aboRhDropdownView: Spinner = it
            aboRhDropdownView.background = uiViewModel.editTextBackground.get()
            val aboRhDropdownArray = resources.getStringArray(R.array.abo_rh_array_with_no_value)
            val aboRhAdapter = ManageDonorViewModel.CustomSpinnerAdapter(requireContext(), uiViewModel, aboRhDropdownArray)
            aboRhDropdownView.adapter = aboRhAdapter
            aboRhDropdownView.setSelection(0)
            aboRhDropdownView.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val temp = if (position > 0) parent.getItemAtPosition(position) as String else ""
                    viewDonorListListViewModel.currentAboRhSelectedValue = temp.ifEmpty { "<>" }
                    viewDonorListListViewModel.patternOfSubpatterns = Utils.newPatternOfSubpatterns(viewDonorListListViewModel.patternOfSubpatterns, 1, viewDonorListListViewModel.currentAboRhSelectedValue)
                    adapter.filter.filter(viewDonorListListViewModel.patternOfSubpatterns)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) { }
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

    override fun fetchDropdown(resId: Int) : Spinner? {
        return fetchRootView().findViewById(resId)
    }

    override fun fetchCreateProductsListViewModel() : CreateProductsListViewModel? { return null }
    override fun fetchDonateProductsListViewModel() : DonateProductsListViewModel? { return null }
    override fun fetchReassociateProductsListViewModel() : ReassociateProductsListViewModel? { return null }

    override fun fetchViewDonorListViewModel() : ViewDonorListListViewModel? {
        return viewDonorListListViewModel
    }

}