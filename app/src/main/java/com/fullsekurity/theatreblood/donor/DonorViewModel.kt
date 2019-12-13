package com.fullsekurity.theatreblood.donor

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.utils.Utils

@Suppress("UNCHECKED_CAST")
class DonorViewModelFactory(private val activity: MainActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DonorViewModel(activity) as T
    }
}

@Suppress("UNCHECKED_CAST")
class DonorViewModel(val activity: MainActivity) : AndroidViewModel(activity.application) {

    val donorUpdateLiveData: MutableLiveData<Donor> = MutableLiveData()

    // observable used for two-way data binding. Values set into this field will show in view.
    // Text typed into EditText in view will be stored into this field after each character is typed.
    var editTextDisplayModifyLastName: ObservableField<String> = ObservableField("")
    fun onTextLastNameChanged(string: CharSequence, start: Int, before: Int, count: Int) {
        // within "string", the "count" characters beginning at index "start" have just replaced old text that had length "before"
    }
    var hintTextLastName: ObservableField<String> = ObservableField(activity.getString(R.string.donor_last_name))
    var editTextLastNameVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    var editTextDisplayModifyFirstName: ObservableField<String> = ObservableField("")
    fun onTextFirstNameChanged(string: CharSequence, start: Int, before: Int, count: Int) { }
    var hintTextFirstName: ObservableField<String> = ObservableField(activity.getString(R.string.donor_first_name))
    var editTextFirstNameVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    var editTextDisplayModifyMiddleName: ObservableField<String> = ObservableField("")
    fun onTextMiddleNameChanged(string: CharSequence, start: Int, before: Int, count: Int) { }
    var hintTextMiddleName: ObservableField<String> = ObservableField(activity.getString(R.string.donor_middle_name))
    var editTextMiddleNameVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)


//    <string name="donor_search_string">Enter Search String</string>
//    <string name="donor_last_name">Enter Last Name</string>
//    <string name="donor_middle_name">Enter Middle Name</string>
//    <string name="donor_first_name">Enter First Name</string>
//    <string name="donor_dob">Enter DOB</string>
//    <string name="donor_gender">Enter Gender</string>
//    <string name="donor_abo_rh">Enter ABO/Rh</string>
//    <string name="donor_branch">Enter Branch</string>
//    <string name="donor_nationality">Enter Nationality</string>
//    <string name="donor_military_unit">Enter Military Unit</string>
//    <string name="donor_today_date">Enter Date</string>
//    <string name="dd_572_completed">DD-572 Completed?</string>
//    <string name="ttd_samples_collected">TTD Samples Collected?</string>

    fun onSubmitClicked(view: View) {
//        loadData()
        Utils.hideKeyboard(view)
    }

    fun setDonor(donor: Donor) {
        editTextDisplayModifyLastName.set(donor.title)
        editTextDisplayModifyFirstName.set(donor.releaseDate)
        editTextDisplayModifyMiddleName.set(donor.posterPath)
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