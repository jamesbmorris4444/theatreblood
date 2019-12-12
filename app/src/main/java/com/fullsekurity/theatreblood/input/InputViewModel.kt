package com.fullsekurity.theatreblood.input

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.repository.network.APIClient
import com.fullsekurity.theatreblood.repository.network.APIInterface
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@Suppress("UNCHECKED_CAST")
class InputViewModelFactory(private val activity: MainActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InputViewModel(activity) as T
    }
}

@Suppress("UNCHECKED_CAST")
class InputViewModel(val activity: MainActivity) : AndroidViewModel(activity.application) {

    val donorSearchLiveData: MutableLiveData<List<Donor>> = MutableLiveData()
    private val donorsService: APIInterface = APIClient.client
    private var disposable: Disposable? = null

    // observable used for two-way data binding. Values set into this field will show in view.
    // Text typed into EditText in view will be stored into this field after each character is typed.
    var editTextNameInput: ObservableField<String> = ObservableField("")

    var hintTextName: ObservableField<String> = ObservableField("Enter Donor Name")
    var editTextNameVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    fun onTextNameChanged(string: CharSequence, start: Int, before: Int, count: Int) {
        // within "string", the "count" characters beginning at index "start" have just replaced old text that had length "before"
    }

    fun onItemClick() {
        loadData()
    }

    private fun loadData() {
        disposable = donorsService.getDonors(Constants.API_KEY, Constants.LANGUAGE, "popularity.desc", "false", "false", 1)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({ donorResponse ->
                donorSearchLiveData.postValue(donorResponse.results)
            },
            {
                throwable -> LogUtils.E(LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), "Exception getting Donor in DonorsListViewModel", throwable)
            })
    }

}