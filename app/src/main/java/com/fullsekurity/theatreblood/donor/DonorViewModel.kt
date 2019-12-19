package com.fullsekurity.theatreblood.donor

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.databinding.AborhDropdownItemBinding
import com.fullsekurity.theatreblood.modal.StandardModal
import com.fullsekurity.theatreblood.repository.Repository
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
    val submitOrUpdateText: ObservableField<String> = ObservableField("")
    private lateinit var donor: Donor
    private var lastNameChanged = false        // last name field was changed (new entry in database)
    private var firstNameChanged = false       // first name field was changed (new entry in database)
    private var middleNameChanged = false       // middle name field was changed (new entry in database)

    // At least one entry changed. If no entries ever change, do not add this donor to a staging database.
    // When the view is loaded the first time, onTextLastNameChanged is called, so ignore this call as a true user change.
    private var atLeastOneEntryChanged = false

    @Inject
    lateinit var uiViewModel: UIViewModel
    @Inject
    lateinit var repository: Repository

    init {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(activity))
            .build()
            .inject(this)
    }

    // last name
    // observable used for two-way data binding. Values set into this field will show in view.
    // Text typed into EditText in view will be stored into this field after each character is typed.
    var editTextDisplayModifyLastName: ObservableField<String> = ObservableField("")
    fun onTextLastNameChanged(string: CharSequence, start: Int, before: Int, count: Int) {
        if (lastNameChanged) {
            submitOrUpdateText.set(activity.getString(R.string.button_insert))
            atLeastOneEntryChanged = true
        } else {
            lastNameChanged = true
        }
        // within "string", the "count" characters beginning at index "start" have just replaced old text that had length "before"
    }
    var hintTextLastName: ObservableField<String> = ObservableField(activity.getString(R.string.donor_last_name))
    var editTextLastNameVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    // first name
    var editTextDisplayModifyFirstName: ObservableField<String> = ObservableField("")
    fun onTextFirstNameChanged(string: CharSequence, start: Int, before: Int, count: Int) {
        if (firstNameChanged) {
            submitOrUpdateText.set(activity.getString(R.string.button_insert))
            atLeastOneEntryChanged = true
        } else {
            firstNameChanged = true
        }
    }
    var hintTextFirstName: ObservableField<String> = ObservableField(activity.getString(R.string.donor_first_name))
    var editTextFirstNameVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    // middle name
    var editTextDisplayModifyMiddleName: ObservableField<String> = ObservableField("")
    fun onTextMiddleNameChanged(string: CharSequence, start: Int, before: Int, count: Int) {
        if (middleNameChanged) {
            atLeastOneEntryChanged = true
        } else {
            middleNameChanged = true
        }
    }
    var hintTextMiddleName: ObservableField<String> = ObservableField(activity.getString(R.string.donor_middle_name))
    var editTextMiddleNameVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    // date of birth
    var editTextDisplayModifyDob: ObservableField<String> = ObservableField("")
    var hintTextDob: ObservableField<String> = ObservableField(activity.getString(R.string.donor_dob))
    var editTextDobVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    fun onCalendarClicked(view: View) {
        Utils.hideKeyboard(view)
        dateDialog()
    }

    private fun dateDialog() {
        val listener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                calendar.set(year, monthOfYear, dayOfMonth)
                atLeastOneEntryChanged = true
                editTextDisplayModifyDob.set(dateFormatter.format(calendar.time))
            }
        }
        DatePickerDialog(activity, uiViewModel.datePickerColorStyle, listener, year, month, day).show()
    }

    // gender
    var hintTextGender: ObservableField<String> = ObservableField(activity.getString(R.string.donor_gender))
    var radioButtonsGenderVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    fun onGenderChanged(radioGroup: RadioGroup?, id: Int) {
        atLeastOneEntryChanged = !atLeastOneEntryChanged
        if (id == R.id.radio_male) {
            maleRadioButton.isChecked = true
        } else {
            femaleRadioButton.isChecked = true
        }
    }

    // ABO/Rh
    var hintTextAboRh: ObservableField<String> = ObservableField(activity.getString(R.string.donor_abo_rh))
    var dropdownAboRhVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)
    var currentAboRhSelectedValue: String = "NO ABO/Rh"
    // atLeastOneEntryChanged = true

    // Military Branch
    var hintTextMilitaryBranch: ObservableField<String> = ObservableField(activity.getString(R.string.donor_branch))
    var dropdownMilitaryBranchVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)
    var currentMilitaryBranchSelectedValue: String = "NO Military Branch"
    // atLeastOneEntryChanged = true

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

    fun onSubmitUpdateClicked(view: View) {
        // update new values into donor

        // change last name
        editTextDisplayModifyLastName.get()?.let { editTextDisplayModifyLastName ->
            donor.title = editTextDisplayModifyLastName
        }

        // change first name
        editTextDisplayModifyFirstName.get()?.let { editTextDisplayModifyFirstName ->
            donor.posterPath = editTextDisplayModifyFirstName
        }

        // change middle name
        editTextDisplayModifyMiddleName.get()?.let { editTextDisplayModifyMiddleName ->
            donor.voteCount = editTextDisplayModifyMiddleName.toInt()
        }

        // change doate of birth
        editTextDisplayModifyDob.get()?.let { editTextDisplayModifyDob ->
            donor.releaseDate = editTextDisplayModifyDob
        }

        // change gender
        if (maleRadioButton.isChecked) {
            donor.overview = activity.getString(R.string.donor_male)
        } else {
            donor.overview = activity.getString(R.string.donor_female)
        }

//        donor.???? = currentAboRhSelectedValue
//        donor.???? = currentMilitaryBranchSelectedValue

        if (atLeastOneEntryChanged) {
            if (submitOrUpdateText.get() == activity.getString(R.string.button_insert)) {
                repository.insertIntoDatabase(repository.insertedBloodDatabase, donor)
            } else {
                repository.insertIntoDatabase(repository.modifiedBloodDatabase, donor)
            }
        } else {
            StandardModal(
                activity,
                modalType = StandardModal.ModalType.STANDARD,
                titleText = activity.getString(R.string.std_modal_no_change_in_database_title),
                positiveText = activity.getString(R.string.std_modal_ok),
                dialogFinishedListener = object : StandardModal.DialogFinishedListener {
                    override fun onPositive(password: String) {
                        loadCreateProductsFragment()
                    }
                    override fun onNegative() { }
                    override fun onNeutral() { }
                    override fun onBackPressed() {
                        loadCreateProductsFragment()
                    }
                }
            ).show(activity.supportFragmentManager, "MODAL")
        }

    }

    private fun loadCreateProductsFragment() {
        activity.loadCreateProductsFragment(donor)
    }

    fun setRootView(view: View) {
        rootView = view
    }

    fun setDonor(donor: Donor) {
        this.donor = donor
        submitOrUpdateText.set(if (donor.title.isEmpty()) activity.getString(R.string.button_insert) else activity.getString(R.string.button_update))
        rootView.findViewById<TextInputLayout>(R.id.edit_text_display_last_name).setHintTextAppearance(uiViewModel.editTextDisplayModifyHintStyle)
        rootView.findViewById<TextInputLayout>(R.id.edit_text_display_first_name).setHintTextAppearance(uiViewModel.editTextDisplayModifyHintStyle)
        rootView.findViewById<TextInputLayout>(R.id.edit_text_display_middle_name).setHintTextAppearance(uiViewModel.editTextDisplayModifyHintStyle)
        rootView.findViewById<TextInputLayout>(R.id.edit_text_display_dob).setHintTextAppearance(uiViewModel.editTextDisplayModifyHintStyle)

        editTextDisplayModifyLastName.set(donor.title)
        editTextDisplayModifyFirstName.set(donor.posterPath)
        editTextDisplayModifyMiddleName.set(donor.voteCount.toString())
        editTextDisplayModifyDob.set(donor.releaseDate)
        
        maleRadioButton = rootView.findViewById(R.id.radio_male)
        femaleRadioButton = rootView.findViewById(R.id.radio_female)
        if (donor.adult) {
            maleRadioButton.isChecked = true
        } else {
            femaleRadioButton.isChecked = true
        }

        val aboRhDropdownView: Spinner = rootView.findViewById(R.id.abo_rh_dropdown)
        aboRhDropdownView.background = uiViewModel.editTextDisplayModifyBackground.get()
        val aboRhDropdownArray = activity.resources.getStringArray(R.array.abo_rh_array)
        val aboRhAdapter = CustomSpinnerAdapter(activity, uiViewModel, aboRhDropdownArray)
        aboRhDropdownView.adapter = aboRhAdapter
        aboRhDropdownView.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                currentAboRhSelectedValue = parent.getItemAtPosition(position) as String
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }

        val militaryBranchDropdownView: Spinner = rootView.findViewById(R.id.military_branch_dropdown)
        militaryBranchDropdownView.background = uiViewModel.editTextDisplayModifyBackground.get()
        val militaryBranchDropdownArray = activity.resources.getStringArray(R.array.military_branch_array)
        val militaryBranchAdapter = CustomSpinnerAdapter(activity, uiViewModel, militaryBranchDropdownArray)
        militaryBranchDropdownView.adapter = militaryBranchAdapter
        militaryBranchDropdownView.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                currentMilitaryBranchSelectedValue = parent.getItemAtPosition(position) as String
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    class CustomSpinnerAdapter(val context: Context, val uiViewModel: UIViewModel, private val aboRhList: Array<String>) : BaseAdapter(), SpinnerAdapter {

        override fun getCount(): Int {
            return aboRhList.size
        }

        override fun getItem(position: Int): String {
            return aboRhList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        data class ViewHolder (
            val textView: TextView
        )

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            val viewHolder: ViewHolder
            var convertViewShadow = convertView
            if (convertView == null) {
                val binding: AborhDropdownItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.aborh_dropdown_item, parent, false)
                binding.uiViewModel = uiViewModel
                convertViewShadow = binding.root
                val textView = convertViewShadow.findViewById<View>(R.id.abo_rh_item) as TextView
                viewHolder = ViewHolder(textView)
                convertViewShadow.tag = viewHolder
            } else {
                viewHolder = convertView.tag as ViewHolder
            }
            viewHolder.textView.text = aboRhList[position]
            return convertViewShadow
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val binding: AborhDropdownItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.aborh_dropdown_item, parent, false)
            val view = binding.root
            binding.uiViewModel = uiViewModel
            val textView = view.findViewById(R.id.abo_rh_item) as TextView
            textView.text = aboRhList[position]
            return view
        }

    }

}