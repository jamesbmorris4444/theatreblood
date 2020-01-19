package com.fullsekurity.theatreblood.reassociateproducts

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.Callbacks
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewViewModel
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.repository.storage.DonorWithProducts
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.Utils
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import javax.inject.Inject

class ReassociateProductsListViewModelFactory(private val callbacks: Callbacks) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReassociateProductsListViewModel(callbacks) as T
    }
}

class ReassociateProductsListViewModel(private val callbacks: Callbacks) : RecyclerViewViewModel(callbacks.fetchActivity().application) {

    private val tag = ReassociateProductsListViewModel::class.java.simpleName
    override var adapter: ReassociateProductsAdapter = ReassociateProductsAdapter(callbacks)
    override val itemDecorator: RecyclerView.ItemDecoration? = null
    val errorVisibility: ObservableField<Int> = ObservableField(View.GONE)
    val errorMessage: ObservableField<String> = ObservableField("")
    private var editTextNameInput = ""
    private var editTextNameVisibility = View.VISIBLE
    private var newDonorVisibility = View.GONE
    private var submitVisibility = View.VISIBLE
    private var incorrectDonorIdentified = false
    private lateinit var incorrectDonor: Donor
    private lateinit var incorrectDonorWithProducts: DonorWithProducts
    private lateinit var donorWithProductsList: List<DonorWithProducts>

    @Inject
    lateinit var uiViewModel: UIViewModel
    @Inject
    lateinit var repository: Repository

    init {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(callbacks.fetchActivity()))
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
                return true
            }
        }
    }

    // The flow through the reassociated donations is as follows
    //
    //   1.  user enters reassociated donations fragment
    //   2.  initializeView()
    //   3.  user enters text and clicks on search button
    //   4.  handleReassociateSearchClick(view), incorrectIdentifierIdentified = false at entry
    //   5.  display all donors in the staging database and their new products that have been entered recently (showDonorsAndProducts)
    //   6.  user clicks on a donor item to select the incorrect donor
    //   7.  handleReassociateIncorrectDonorClick(incorrectDonor), incorrectIdentifierIdentified = false at entry (showIncorrectDonorAndProductsWithSearchBox)
    //   8.  show incorrect donor selected and products for this donor, and show search box for selecting correct donor at the bottom of the screen
    //   9.  user enters text and clicks on search button to find correct donor
    //   10. handleReassociateSearchClick(view), incorrectIdentifierIdentified = true at entry
    //   11. display potential correct donors and associated products (showCorrectDonorsAndProducts)
    //   12. user clicks on correct donor item, and products from incorrect donor are moved to correct donor (moveProductsToCorrectDonor)

    fun initializeView() {
        if (repository.newDonor != null) {
            // re-initialize view after creating a new donor while trying to reassociate to a donor, the new donor is now stored in fetchActivity().newDonor
            repository.retrieveDonorFromNameAndDate(
                callbacks.fetchActivity().fetchRootView().findViewById(R.id.main_progress_bar),
                repository.newDonor as Donor,
                this::completeReassociationToNewDonor)
        } else {
            // initial entry
            // show
            //    search box
            incorrectDonorIdentified = false
            errorMessage.set(getApplication<Application>().applicationContext.getString(R.string.search_no_elements))
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
    }

    private fun completeReassociationToNewDonor(newDonor: Donor) {
        incorrectDonorIdentified = true
        val list: MutableList<Any> = mutableListOf()
        list.add(ReassociateProductsLabelData(title = "Incorrect Donor", incorrectDonorVisibility = View.VISIBLE))
        list.add(incorrectDonor)
        for (product in incorrectDonorWithProducts.products) {
            list.add(product)
        }
        newDonor.inReassociate = true
        list.add(newDonor)
        adapter.addAll(list)
        repository.newDonor = null
    }

    fun handleReassociateSearchClick(view: View) {
        if (incorrectDonorIdentified) {
            // click on correct donor search box
            repository.handleReassociateSearchClick(view, editTextNameInput, this::showCorrectDonorsAndProducts)
        } else {
            // click on incorrect donor search box
            repository.handleReassociateSearchClick(view, editTextNameInput, this::showAllDonorsAndProducts)
        }
        Utils.hideKeyboard(view)
    }

    private fun showAllDonorsAndProducts(donorsWithProductsList: List<DonorWithProducts>) {
        // show
        //    search box
        //    donor
        //    donor products
        //       ...
        //    donor
        //    donor products
        if (donorsWithProductsList.isEmpty()) {
            errorVisibility.set(View.VISIBLE)
            errorMessage.set(getApplication<Application>().applicationContext.getString(R.string.search_no_elements))
        } else {
            errorVisibility.set(View.GONE)
            this.donorWithProductsList = donorsWithProductsList
            val list: MutableList<Any> = mutableListOf()
            list.add(ReassociateProductsSearchData(
                hintTextName = getApplication<Application>().applicationContext.getString(R.string.donor_incorrect_search_string),
                editTextNameVisibility = editTextNameVisibility,
                newDonorVisibility = newDonorVisibility,
                submitVisibility = submitVisibility,
                editTextNameInput = editTextNameInput)
            )
            for (index in donorsWithProductsList.indices) {
                val donor = donorsWithProductsList[index].donor
                donor.inReassociate = true
                list.add(donor)
                for (product in donorsWithProductsList[index].products) {
                    product.inReassociate = true
                    list.add(product)
                }
            }
            adapter.addAll(list)
        }
    }

    fun handleReassociateDonorClick(view: View, donor: Donor) {
        val position = view.tag as Int
        if (incorrectDonorIdentified) {
            if (position == 1) {
                // this is a click on the incorrect donor, which has already been identified
                // start over
                initializeView()
            } else {
                // this is a click on the correct donor that will complete the re-association to the new donor
                // move products to correct donor
                // show initial view search box
                moveProductsToCorrectDonor(incorrectDonor, donor)
            }
        } else {
            // this is a click on the incorrect donor, which has not yet been identified
            // show
            //   incorrect donor label
            //   incorrect donor
            //   incorrect donor products
            //   search box
            incorrectDonorIdentified = true
            incorrectDonor = donor
            findDonorInDonorsAndProductsList(incorrectDonor)?. let {
                incorrectDonorWithProducts = it
            }
            showIncorrectDonorAndProductsWithSearchBox()
        }
    }

    private fun showIncorrectDonorAndProductsWithSearchBox() {
        if (incorrectDonorIdentified) {
            errorVisibility.set(View.GONE)
            val list: MutableList<Any> = mutableListOf()
            addHeaderToList(list, incorrectDonorWithProducts)
            adapter.addAll(list)
        } else {
            errorVisibility.set(View.VISIBLE)
            errorMessage.set(getApplication<Application>().applicationContext.getString(R.string.search_no_incorrect_donors))
        }
    }

    private fun addHeaderToList(list: MutableList<Any>, donorWithProducts: DonorWithProducts) {
        list.add(ReassociateProductsLabelData(title = "Incorrect Donor", incorrectDonorVisibility = View.VISIBLE))
        list.add(incorrectDonor)
        for (product in donorWithProducts.products) {
            list.add(product)
        }
        list.add(ReassociateProductsSearchData(
            hintTextName = getApplication<Application>().applicationContext.getString(R.string.donor_correct_search_string),
            editTextNameVisibility = editTextNameVisibility,
            newDonorVisibility = newDonorVisibility,
            submitVisibility = submitVisibility,
            editTextNameInput = editTextNameInput)
        )
    }

    private fun showCorrectDonorsAndProducts(donorsAndProductsList: List<DonorWithProducts>) {
        val list: MutableList<Any> = mutableListOf()
        if (donorsAndProductsList.isEmpty()) {
            newDonorVisibility = View.VISIBLE
            addHeaderToList(list, incorrectDonorWithProducts)
        } else {
            newDonorVisibility = View.GONE
            addHeaderToList(list, incorrectDonorWithProducts)
            for (index in donorsAndProductsList.indices) {
                val donor = donorsAndProductsList[index].donor
                donor.inReassociate = true
                if (!Utils.donorEquals(donor, incorrectDonor)) {
                    list.add(donor)
                    for (product in donorsAndProductsList[index].products) {
                        product.inReassociate = true
                        list.add(product)
                    }
                }
            }
        }
        adapter.addAll(list)
    }

    private fun moveProductsToCorrectDonor(incorrectDonor: Donor, correctDonor: Donor) {
        val donorWithProducts = findDonorInDonorsAndProductsList(incorrectDonor)
        if (donorWithProducts == null) {
            newDonorVisibility = View.VISIBLE
        } else {
            newDonorVisibility = View.GONE
            for (product in donorWithProducts.products) {
                if (!product.removedForReassociation) {
                    product.donorId = correctDonor.id
                }
            }
            repository.insertReassociatedProductsIntoDatabase(repository.stagingBloodDatabase, donorWithProducts.products, this::initializeView)
        }
    }

    private fun findDonorInDonorsAndProductsList(donor: Donor): DonorWithProducts? {
        for (index in donorWithProductsList.indices) {
            val donorInList = donorWithProductsList[index].donor
            if (Utils.donorEquals(donor, donorInList)) {
                return donorWithProductsList[index]
            }
        }
        return null
    }

    fun onTextNameChanged(key: CharSequence, start: Int, before: Int, count: Int) {
        editTextNameInput = key.toString()
        submitVisibility = View.VISIBLE
        // within "string", the "count" characters beginning at index "start" have just replaced old text that had length "before"
    }

}
