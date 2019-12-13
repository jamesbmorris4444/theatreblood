package com.fullsekurity.theatreblood.donor

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.repository.network.APIClient
import com.fullsekurity.theatreblood.repository.network.APIInterface
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.utils.Utils
import io.reactivex.disposables.Disposable

@Suppress("UNCHECKED_CAST")
class DonorViewModelFactory(private val activity: MainActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DonorViewModel(activity) as T
    }
}

@Suppress("UNCHECKED_CAST")
class DonorViewModel(val activity: MainActivity) : AndroidViewModel(activity.application) {

    val donorUpdateLiveData: MutableLiveData<Donor> = MutableLiveData()
    private val donorsService: APIInterface = APIClient.client
    private var disposable: Disposable? = null

    // observable used for two-way data binding. Values set into this field will show in view.
    // Text typed into EditText in view will be stored into this field after each character is typed.
    var editTextReleaseDateInput: ObservableField<String> = ObservableField("")


    var hintTextReleaseDate: ObservableField<String> = ObservableField("Enter Donor ReleaseDate")
    var editTextReleaseDateVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    fun onTextReleaseDateChanged(string: CharSequence, start: Int, before: Int, count: Int) {
        // within "string", the "count" characters beginning at index "start" have just replaced old text that had length "before"
    }

    // observable used for two-way data binding. Values set into this field will show in view.
    // Text typed into EditText in view will be stored into this field after each character is typed.
    var editTextNameInput: ObservableField<String> = ObservableField("")

    var hintTextName: ObservableField<String> = ObservableField("Enter Donor Name")
    var editTextNameVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    fun onTextNameChanged(string: CharSequence, start: Int, before: Int, count: Int) {
        // within "string", the "count" characters beginning at index "start" have just replaced old text that had length "before"
    }

    fun onSubmitClicked(view: View) {
//        loadData()
        Utils.hideKeyboard(view)
    }

    fun setDonor(donor: Donor) {
        editTextNameInput.set(donor.title)
        editTextReleaseDateInput.set(donor.releaseDate)
    }

    private fun loadData() {
//        val progressBar = activity.main_progress_bar
//        progressBar.visibility = View.VISIBLE
//        disposable = donorsService.getDonors(Constants.API_KEY, Constants.LANGUAGE, 5)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe ({ donorResponse ->
//                donorUpdateLiveData.postValue(donorResponse.results)
//                progressBar.visibility = View.GONE
//            },
//                {
//                        throwable -> LogUtils.E(LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), "Exception getting Donor in DonorsListViewModel", throwable)
//                })
    }

}