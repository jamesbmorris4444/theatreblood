package com.fullsekurity.theatreblood.viewdonorlist

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.Callbacks
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.SingleLiveEvent
import com.fullsekurity.theatreblood.utils.Utils
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import javax.inject.Inject

class ViewDonorListListViewModelFactory(private val callbacks: Callbacks) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewDonorListListViewModel(callbacks) as T
    }
}

class ViewDonorListListViewModel(private val callbacks: Callbacks) : AndroidViewModel(callbacks.fetchActivity().application) {

    val listIsVisible: ObservableField<Boolean> = ObservableField(true)
    private val newDonorVisible: ObservableField<Int> = ObservableField(View.GONE)
    var numberOfItemsDisplayed = -1
    var patternOfSubpatterns: String = "<>|<>"
    private val liveFilterDonorListEvent: SingleLiveEvent<String> = SingleLiveEvent()
    fun getLiveFilterDonorListEvent(): SingleLiveEvent<String> { return liveFilterDonorListEvent }

    @Inject
    lateinit var uiViewModel: UIViewModel
    @Inject
    lateinit var repository: Repository

    init {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(callbacks.fetchActivity()))
            .build()
            .inject(this)

    }

    fun setNewDonorVisibility(key: String) {
        if (key.isNotEmpty() && numberOfItemsDisplayed == 0) {
            newDonorVisible.set(View.VISIBLE)
        } else {
            newDonorVisible.set(View.GONE)
        }
    }

    // observable used for two-way donations binding. Values set into this field will show in view.
    // Text typed into EditText in view will be stored into this field after each character is typed.
    var editTextNameInput: ObservableField<String> = ObservableField("")
    var hintTextName: ObservableField<String> = ObservableField(getApplication<Application>().applicationContext.getString(R.string.donor_search_string))
    var editTextNameVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)
    fun onTextNameChanged(newText: CharSequence, start: Int, before: Int, count: Int) {
        patternOfSubpatterns = Utils.newPatternOfSubpatterns(patternOfSubpatterns, 0, newText.toString().ifEmpty { "<>" })
        liveFilterDonorListEvent.value = patternOfSubpatterns
        // within "string", the "count" characters beginning at index "start" have just replaced old text that had length "before"
    }

    // ABO/Rh
    var hintTextAboRh: ObservableField<String> = ObservableField(getApplication<Application>().applicationContext.getString(R.string.donor_abo_rh))
    var dropdownAboRhVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)
    var currentAboRhSelectedValue: String = ""

    class DonorListDiffCallback(private val oldList: List<Donor>, private val newList: List<Donor>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].firstName == newList[newItemPosition].firstName && oldList[oldItemPosition].lastName == newList[newItemPosition].lastName
        }
        override fun areContentsTheSame(oldItemPosition: Int, newPosition: Int): Boolean {
            return areItemsTheSame(oldItemPosition, newPosition)
        }
    }


}
