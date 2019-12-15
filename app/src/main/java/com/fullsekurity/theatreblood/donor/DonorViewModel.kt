package com.fullsekurity.theatreblood.donor

import android.app.DatePickerDialog
import android.view.View
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.Utils
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@Suppress("UNCHECKED_CAST")
class DonorViewModelFactory(private val activity: MainActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DonorViewModel(activity) as T
    }
}

@Suppress("UNCHECKED_CAST")
class DonorViewModel(val activity: MainActivity) : AndroidViewModel(activity.application) {

    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)
    private var dateFormatter = SimpleDateFormat("MM/dd/yyyy", Locale.US)
    private lateinit var rootView: View
    private lateinit var maleRadioButton: RadioButton
    private lateinit var femaleRadioButton: RadioButton

    @Inject
    lateinit var uiViewModel: UIViewModel

    init {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(activity))
            .build()
            .inject(this)
    }

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

    var editTextDisplayModifyDob: ObservableField<String> = ObservableField("")
    fun onTextDobChanged(string: CharSequence, start: Int, before: Int, count: Int) { }
    var hintTextDob: ObservableField<String> = ObservableField(activity.getString(R.string.donor_dob))
    var editTextDobVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    var hintTextGender: ObservableField<String> = ObservableField(activity.getString(R.string.donor_gender))
    var radioButtonsGenderVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

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

    fun onCalendarClicked(view: View) {
        Utils.hideKeyboard(view)
        dateDialog()
    }

    private fun dateDialog() {
        val listener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                calendar.set(year, monthOfYear, dayOfMonth)
                editTextDisplayModifyDob.set(dateFormatter.format(calendar.time))
            }
        }
        DatePickerDialog(activity, listener, year, month, day).show()
    }

    fun onGenderChanged(radioGroup: RadioGroup?, id: Int) {
        if (id == R.id.radio_male) {
            maleRadioButton.isChecked = true
        } else {
            femaleRadioButton.isChecked = true
        }
    }

    fun setRootView(view: View) {
        rootView = view
    }

    fun setDonor(donor: Donor) {
        rootView.findViewById<TextInputLayout>(R.id.edit_text_display_last_name).setHintTextAppearance(uiViewModel.hintStyle)
        rootView.findViewById<TextInputLayout>(R.id.edit_text_display_first_name).setHintTextAppearance(uiViewModel.hintStyle)
        rootView.findViewById<TextInputLayout>(R.id.edit_text_display_middle_name).setHintTextAppearance(uiViewModel.hintStyle)
        rootView.findViewById<TextInputLayout>(R.id.edit_text_display_dob).setHintTextAppearance(uiViewModel.hintStyle)

        editTextDisplayModifyLastName.set(donor.title)
        editTextDisplayModifyFirstName.set(donor.posterPath.substring(0,donor.posterPath.length / 2))
        editTextDisplayModifyMiddleName.set(donor.releaseDate)
        editTextDisplayModifyDob.set(donor.releaseDate)
        maleRadioButton = rootView.findViewById(R.id.radio_male)
        femaleRadioButton = rootView.findViewById(R.id.radio_female)
        if (donor.adult) {
            maleRadioButton.isChecked = true
        } else {
            femaleRadioButton.isChecked = true
        }
    }

}