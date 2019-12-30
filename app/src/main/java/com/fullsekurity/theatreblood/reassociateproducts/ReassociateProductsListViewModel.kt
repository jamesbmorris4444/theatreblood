package com.fullsekurity.theatreblood.reassociateproducts

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.ActivityCallbacks
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewViewModel
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.repository.storage.DonorWithProducts
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import javax.inject.Inject

class ReassociateProductsListViewModelFactory(private val activityCallbacks: ActivityCallbacks) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReassociateProductsListViewModel(activityCallbacks) as T
    }
}

class ReassociateProductsListViewModel(private val activityCallbacks: ActivityCallbacks) : RecyclerViewViewModel(activityCallbacks.fetchActivity().application) {

    private val tag = ReassociateProductsListViewModel::class.java.simpleName
    override var adapter: ReassociateProductsAdapter = ReassociateProductsAdapter(activityCallbacks)
    override val itemDecorator: RecyclerView.ItemDecoration? = null
    val errorVisibility: ObservableField<Int> = ObservableField(View.GONE)
    private var productsListIsVisible = true
    private var editTextNameVisibility = View.VISIBLE
    private var newDonorVisibility = View.GONE
    private var submitVisibility = View.VISIBLE
    private var incorrectDonorVisibility = View.GONE
    private var numberOfItemsDisplayed = -1
    private var incorrectDonorIdentified = false
    private var editTextNameInput = ""
    private lateinit var donorAndProductsList: List<DonorWithProducts>

    @Inject
    lateinit var uiViewModel: UIViewModel
    @Inject
    lateinit var repository: Repository

    init {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(activityCallbacks.fetchActivity()))
            .build()
            .inject(this)
        adapter.uiViewModel = uiViewModel
    }

    override fun setLayoutManager(): RecyclerView.LayoutManager {
        return object : LinearLayoutManager(getApplication<Application>().applicationContext) {
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return false
            }
        }
    }

    fun initializeView() {
        val list: MutableList<ReassociateProductsSearchData> = mutableListOf()
        list.add(ReassociateProductsSearchData(
            hintTextName = getApplication<Application>().applicationContext.getString(R.string.donor_incorrect_search_string),
            editTextNameVisibility = editTextNameVisibility,
            newDonorVisibility = newDonorVisibility,
            submitVisibility = submitVisibility,
            editTextNameInput = editTextNameInput)
        )
        adapter.addAll(list)
    }

    private fun showDonorsAndProducts(donorsAndProductsList: List<DonorWithProducts>) {
        if (donorsAndProductsList.isNotEmpty()) {
            this.donorAndProductsList = donorsAndProductsList
            val list: MutableList<Any> = mutableListOf()
            list.add(ReassociateProductsSearchData(
                hintTextName = getApplication<Application>().applicationContext.getString(R.string.donor_incorrect_search_string),
                editTextNameVisibility = editTextNameVisibility,
                newDonorVisibility = newDonorVisibility,
                submitVisibility = submitVisibility,
                editTextNameInput = editTextNameInput)
            )
            for (index in donorsAndProductsList.indices) {
                val donor = donorsAndProductsList[index].donor
                donor.inReassociate = true
                list.add(donor)
                for (product in donorsAndProductsList[index].products) {
                    product.editAndDeleteButtonVisibility = View.GONE
                    list.add(product)
                }
            }
            adapter.addAll(list)
        }
        errorVisibility.set(if (donorsAndProductsList.isEmpty()) View.VISIBLE else View.GONE)
        numberOfItemsDisplayed = donorsAndProductsList.size
        setNewDonorVisibility("NONEMPTY")
    }

    private fun setNewDonorVisibility(key: String) {
        if (key.isNotEmpty() && numberOfItemsDisplayed == 0 && incorrectDonorIdentified) {
            newDonorVisibility = View.VISIBLE
        } else {
            newDonorVisibility = View.GONE
        }
    }

    fun handleReassociateIncorrectDonorClick(incorrectDonor: Donor) {
        incorrectDonorIdentified = true
        adapter.clearAll()
        val donorsWithProducts = findDonorInDonorsAndProductsList(incorrectDonor)
        if (donorsWithProducts != null) {
            val list: MutableList<Any> = mutableListOf()
            list.add(incorrectDonor)
            for (product in donorsWithProducts.products) {
                product.editAndDeleteButtonVisibility = View.GONE
                list.add(product)
            }
            list.add(ReassociateProductsSearchData(
                hintTextName = getApplication<Application>().applicationContext.getString(R.string.donor_correct_search_string),
                editTextNameVisibility = editTextNameVisibility,
                newDonorVisibility = newDonorVisibility,
                submitVisibility = submitVisibility,
                editTextNameInput = editTextNameInput)
            )
            adapter.addAll(list)
        }

    }

    fun findDonorInDonorsAndProductsList(donor: Donor): DonorWithProducts? {
        for (index in donorAndProductsList.indices) {
            val donorInList = donorAndProductsList[index].donor
            if (donor.title == donorInList.title && donor.posterPath == donorInList.posterPath && donor.voteCount == donorInList.voteCount && donor.releaseDate == donorInList.releaseDate) {
                return donorAndProductsList[index]
            }
        }
        return null
    }

    fun onTextNameChanged(key: CharSequence, start: Int, before: Int, count: Int) {
        editTextNameInput = key.toString()
        setNewDonorVisibility(key.toString())
        submitVisibility =View.VISIBLE
        // within "string", the "count" characters beginning at index "start" have just replaced old text that had length "before"
    }

    fun handleReassociateSearchClick(view: View) {
        repository.handleReassociateSearchClick(view, editTextNameInput, this::showDonorsAndProducts)
    }

    fun handleReassociateNewDonorClick(view: View) {
        repository.handleReassociateNewDonorClick(view)
    }

}
