package com.fullsekurity.theatreblood.donors

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewViewModel
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.Utils
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class DonateProductsListViewModelFactory(private val activity: MainActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DonateProductsListViewModel(activity) as T
    }
}

@Suppress("UNCHECKED_CAST")
class DonateProductsListViewModel(val activity: MainActivity) : RecyclerViewViewModel(activity.application) {

    private val tag = DonateProductsListViewModel::class.java.simpleName
    override var adapter: DonateProductsAdapter = DonateProductsAdapter(activity)
    override val itemDecorator: RecyclerView.ItemDecoration? = null
    val listIsVisible: ObservableField<Boolean> = ObservableField(true)
    val newDonorVisible: ObservableField<Int> = ObservableField(View.GONE)
    val submitVisible: ObservableField<Int> = ObservableField(View.GONE)
    private lateinit var rootView: View
    private var numberOfItemsDisplayed = -1

    @Inject
    lateinit var uiViewModel: UIViewModel
    @Inject
    lateinit var repository: Repository

    init {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(activity))
            .build()
            .inject(this)
        adapter.uiViewModel = uiViewModel
    }

    override fun setLayoutManager(): RecyclerView.LayoutManager {
        return object : LinearLayoutManager(activity.applicationContext) {
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return true
            }
        }
    }

    private fun showDonors(donorList: List<Donor>) {
        listIsVisible.set(donorList.isNotEmpty())
        adapter.addAll(donorList)
        numberOfItemsDisplayed = donorList.size
        setNewDonorVisibility("NONEMPTY")
    }

    private fun setNewDonorVisibility(key: String) {
        if (key.isNotEmpty() && numberOfItemsDisplayed == 0) {
            newDonorVisible.set(View.VISIBLE)
        } else {
            newDonorVisible.set(View.GONE)
        }
    }

    // observable used for two-way data binding. Values set into this field will show in view.
    // Text typed into EditText in view will be stored into this field after each character is typed.
    var editTextNameInput: ObservableField<String> = ObservableField("")
    fun onTextNameChanged(key: CharSequence, start: Int, before: Int, count: Int) {
        if (key.isEmpty()) {
            newDonorVisible.set(View.GONE)
            submitVisible.set(View.GONE)
            numberOfItemsDisplayed = -1
        } else {
            setNewDonorVisibility(key.toString())
            submitVisible.set(View.VISIBLE)
        }
        // within "string", the "count" characters beginning at index "start" have just replaced old text that had length "before"
    }
    var hintTextName: ObservableField<String> = ObservableField(activity.getString(R.string.donor_search_string))
    var editTextNameVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    fun onSubmitClicked(view: View) {
        var disposable: Disposable? = null
        val fullNameResponseList = listOf(
            repository.donorsFromFullName(repository.modifiedBloodDatabase, editTextNameInput.get() ?: ""),
            repository.donorsFromFullName(repository.insertedBloodDatabase, editTextNameInput.get() ?: ""),
            repository.donorsFromFullName(repository.mainBloodDatabase, editTextNameInput.get() ?: "")
        )
        disposable = Single.zip(fullNameResponseList) { args -> listOf(args) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ responseList ->
                val response = responseList[0]
                for (donor in response[0] as List<Donor>) {
                    if (donor.posterPath.length > 11) {
                        donor.posterPath = donor.posterPath.substring(1,11).toUpperCase(Locale.getDefault())
                        donor.backdropPath = donor.backdropPath.substring(1,11).toUpperCase(Locale.getDefault())
                    }
                }
                for (donor in response[1] as List<Donor>) {
                    if (donor.posterPath.length > 11) {
                        donor.posterPath = donor.posterPath.substring(1,11).toUpperCase(Locale.getDefault())
                        donor.backdropPath = donor.backdropPath.substring(1,11).toUpperCase(Locale.getDefault())
                    }
                }
                for (donor in response[2] as List<Donor>) {
                    if (donor.posterPath.length > 11) {
                        donor.posterPath = donor.posterPath.substring(1,11).toUpperCase(Locale.getDefault())
                        donor.backdropPath = donor.backdropPath.substring(1,11).toUpperCase(Locale.getDefault())
                    }
                }
                val combinedList = (response[0] as List<Donor>).union(response[1] as List<Donor>).union(response[2] as List<Donor>).distinctBy { it.title + it.posterPath }
                showDonors(combinedList.toList())
                Utils.hideKeyboard(view)
            }, { response -> val c = response })
    }

    fun onNewDonorClicked(view: View) {
        activity.loadDonorFragment(Donor())
    }

    fun setRootView(view: View) {
        rootView = view
        rootView.findViewById<TextInputLayout>(R.id.edit_text_input_name).setHintTextAppearance(uiViewModel.editTextDisplayModifyHintStyle)
    }

}
