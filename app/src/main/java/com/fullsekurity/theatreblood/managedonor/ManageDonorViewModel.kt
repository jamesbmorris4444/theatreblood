package com.fullsekurity.theatreblood.managedonor

import android.app.Application
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
import com.fullsekurity.theatreblood.activity.Callbacks
import com.fullsekurity.theatreblood.databinding.AborhAndBranchDropdownItemBinding
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
class ManageDonorViewModelFactory(private val callbacks: Callbacks) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ManageDonorViewModel(callbacks) as T
    }
}

@Suppress("UNCHECKED_CAST")
class ManageDonorViewModel(private val callbacks: Callbacks) : AndroidViewModel(callbacks.fetchActivity().application) {

    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)
    private var dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.US)
    private lateinit var donor: Donor
    var transitionToCreateDonation = true
    
    private lateinit var originalLastName: String
    private lateinit var originalFirstName: String
    private lateinit var originalMiddleName: String
    private lateinit var originalDob: String
    private var originalGender: Boolean = true
    private lateinit var originalAboRh: String
    private lateinit var originalBranch: String

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

    // last name
    // observable used for two-way donations binding. Values set into this field will show in view.
    // Text typed into EditText in view will be stored into this field after each character is typed.
    var editTextDisplayModifyLastName: ObservableField<String> = ObservableField("")
    fun onTextLastNameChanged(string: CharSequence, start: Int, before: Int, count: Int) {
        // within "string", the "count" characters beginning at index "start" have just replaced old text that had length "before"
    }
    var hintTextLastName: ObservableField<String> = ObservableField(getApplication<Application>().applicationContext.getString(R.string.donor_last_name))
    var editTextLastNameVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    // first name
    var editTextDisplayModifyFirstName: ObservableField<String> = ObservableField("")
    fun onTextFirstNameChanged(string: CharSequence, start: Int, before: Int, count: Int) {
    }
    var hintTextFirstName: ObservableField<String> = ObservableField(getApplication<Application>().applicationContext.getString(R.string.donor_first_name))
    var editTextFirstNameVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    // middle name
    var editTextDisplayModifyMiddleName: ObservableField<String> = ObservableField("")
    fun onTextMiddleNameChanged(string: CharSequence, start: Int, before: Int, count: Int) {
    }
    var hintTextMiddleName: ObservableField<String> = ObservableField(getApplication<Application>().applicationContext.getString(R.string.donor_middle_name))
    var editTextMiddleNameVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    // date of birth
    var editTextDisplayModifyDob: ObservableField<String> = ObservableField("")
    var hintTextDob: ObservableField<String> = ObservableField(getApplication<Application>().applicationContext.getString(R.string.donor_dob))
    var editTextDobVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

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
        DatePickerDialog(callbacks.fetchActivity(), uiViewModel.datePickerColorStyle, listener, year, month, day).show()
    }

    // gender
    var hintTextGender: ObservableField<String> = ObservableField(getApplication<Application>().applicationContext.getString(R.string.donor_gender))
    var radioButtonsGenderVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    fun onGenderChanged(radioGroup: RadioGroup, id: Int) {
        if (id == R.id.radio_male) {
            callbacks.fetchRadioButton(R.id.radio_male)?.isChecked = true
        } else {
            callbacks.fetchRadioButton(R.id.radio_female)?.isChecked = true
        }
    }

    // ABO/Rh
    var hintTextAboRh: ObservableField<String> = ObservableField(getApplication<Application>().applicationContext.getString(R.string.donor_abo_rh))
    var dropdownAboRhVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)
    var currentAboRhSelectedValue: String = "NO ABO/Rh"

    // Military Branch
    var hintTextMilitaryBranch: ObservableField<String> = ObservableField(getApplication<Application>().applicationContext.getString(R.string.donor_branch))
    var dropdownMilitaryBranchVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)
    var currentMilitaryBranchSelectedValue: String = "NO Military Branch"

    fun initializeDonorValues(donor: Donor) {
        originalLastName = donor.lastName
        originalFirstName = donor.firstName
        originalMiddleName = donor.middleName
        originalDob = donor.dob
        originalGender = donor.gender
        originalAboRh = donor.aboRh
        originalBranch = donor.branch
    }

    fun onUpdateClicked(view: View) {
        // update new values into donor

        // change last name
        editTextDisplayModifyLastName.get()?.let { editTextDisplayModifyLastName ->
            donor.lastName = editTextDisplayModifyLastName
        }

        // change first name
        editTextDisplayModifyFirstName.get()?.let { editTextDisplayModifyFirstName ->
            donor.firstName = editTextDisplayModifyFirstName
        }

        // change middle name
        editTextDisplayModifyMiddleName.get()?.let { editTextDisplayModifyMiddleName ->
            donor.middleName = editTextDisplayModifyMiddleName
        }

        // change date of birth
        editTextDisplayModifyDob.get()?.let { editTextDisplayModifyDob ->
            donor.dob = editTextDisplayModifyDob
        }

        // change gender
        callbacks.fetchRadioButton(R.id.radio_male)?.let {
            donor.gender = it.isChecked
        }

        // change ABO/Rh
        donor.aboRh = currentAboRhSelectedValue

        // change branch
        donor.branch = currentMilitaryBranchSelectedValue

        val atLeastOneEntryChanged =
            donor.lastName != originalLastName ||
            donor.firstName != originalFirstName ||
            donor.middleName != originalMiddleName ||
            donor.dob != originalDob ||
            donor.gender != originalGender ||
            donor.aboRh != originalAboRh ||
            donor.branch != originalBranch

        if (atLeastOneEntryChanged && isDonorValid(donor)) {
            repository.insertDonorIntoDatabase(repository.stagingBloodDatabase, donor, transitionToCreateDonation)
            if (repository.newDonorInProgress) {
                repository.newDonor = donor
                if (transitionToCreateDonation) {
                    // retrieve the new donor from the staging database in order to set its id
                    repository.retrieveDonorFromNameAndDate(
                        callbacks.fetchActivity().fetchRootView().findViewById(R.id.main_progress_bar),
                        donor,
                        this::completeProcessingOfNewDonor)
                }
            }
        } else {
            StandardModal(
                callbacks,
                modalType = StandardModal.ModalType.STANDARD,
                titleText = getApplication<Application>().applicationContext.getString(R.string.std_modal_no_change_in_database_title),
                positiveText = getApplication<Application>().applicationContext.getString(R.string.std_modal_ok),
                dialogFinishedListener = object : StandardModal.DialogFinishedListener {
                    override fun onPositive(password: String) {
                        if (transitionToCreateDonation) {
                            loadCreateProductsFragment()
                        } else {
                            callbacks.fetchActivity().onBackPressed()
                        }
                    }
                    override fun onNegative() { }
                    override fun onNeutral() { }
                    override fun onBackPressed() {
                        callbacks.fetchActivity().onBackPressed()
                    }
                }
            ).show(callbacks.fetchActivity().supportFragmentManager, "MODAL")
        }
        repository.newDonorInProgress = false
    }

    private fun completeProcessingOfNewDonor(newDonor: Donor) {
        donor.id = newDonor.id
        repository.newDonor = null
    }

    private fun isDonorValid(donor: Donor): Boolean {
        return donor.lastName.isNotEmpty() && donor.firstName.isNotEmpty() && donor.dob.isNotEmpty()
    }

    private fun loadCreateProductsFragment() {
        callbacks.fetchActivity().loadCreateProductsFragment(donor)
    }

    fun setDonor(donor: Donor) {
        this.donor = donor
        callbacks.fetchRootView().findViewById<TextInputLayout>(R.id.edit_text_display_last_name).setHintTextAppearance(uiViewModel.editTextDisplayModifyHintStyle)
        callbacks.fetchRootView().findViewById<TextInputLayout>(R.id.edit_text_display_first_name).setHintTextAppearance(uiViewModel.editTextDisplayModifyHintStyle)
        callbacks.fetchRootView().findViewById<TextInputLayout>(R.id.edit_text_display_middle_name).setHintTextAppearance(uiViewModel.editTextDisplayModifyHintStyle)
        callbacks.fetchRootView().findViewById<TextInputLayout>(R.id.edit_text_display_dob).setHintTextAppearance(uiViewModel.editTextDisplayModifyHintStyle)

        editTextDisplayModifyLastName.set(donor.lastName)
        editTextDisplayModifyFirstName.set(donor.firstName)
        editTextDisplayModifyMiddleName.set(donor.middleName)
        editTextDisplayModifyDob.set(donor.dob)

        if (donor.gender) {
            callbacks.fetchRadioButton(R.id.radio_male)?.isChecked = true
        } else {
            callbacks.fetchRadioButton(R.id.radio_female)?.isChecked = true
        }

        callbacks.fetchDropdown(R.id.abo_rh_dropdown)?.let {
            val aboRhDropdownView: Spinner = it
            val aboRhDropdownArray = getApplication<Application>().applicationContext.resources.getStringArray(R.array.abo_rh_array)
            val aboRhAdapter = CustomSpinnerAdapter(callbacks.fetchActivity(), uiViewModel, aboRhDropdownArray)
            aboRhDropdownView.adapter = aboRhAdapter
            aboRhDropdownView.setSelection(getDropdownSelection(donor.aboRh, aboRhDropdownArray))
            aboRhDropdownView.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    currentAboRhSelectedValue = parent.getItemAtPosition(position) as String
                }
                override fun onNothingSelected(parent: AdapterView<*>?) { }
            }
        }

        callbacks.fetchDropdown(R.id.military_branch_dropdown)?.let {
            val militaryBranchDropdownView: Spinner = it
            val militaryBranchDropdownArray = getApplication<Application>().applicationContext.resources.getStringArray(R.array.military_branch_array)
            val militaryBranchAdapter = CustomSpinnerAdapter(callbacks.fetchActivity(), uiViewModel, militaryBranchDropdownArray)
            militaryBranchDropdownView.adapter = militaryBranchAdapter
            militaryBranchDropdownView.setSelection(getDropdownSelection(donor.branch, militaryBranchDropdownArray))
            militaryBranchDropdownView.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    currentMilitaryBranchSelectedValue = parent.getItemAtPosition(position) as String
                }
                override fun onNothingSelected(parent: AdapterView<*>?) { }
            }
        }
    }

    private fun getDropdownSelection(selectionString: String, selectionArray: Array<String>): Int {
        for (index in selectionArray.indices) {
            if (selectionString == selectionArray[index]) {
                return index
            }
        }
        return 0
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
                val binding: AborhAndBranchDropdownItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.aborh_and_branch_dropdown_item, parent, false)
                binding.uiViewModel = uiViewModel
                convertViewShadow = binding.root
                val textView = convertViewShadow.findViewById<View>(R.id.dropdown_item) as TextView
                viewHolder = ViewHolder(textView)
                convertViewShadow.tag = viewHolder
            } else {
                viewHolder = convertView.tag as ViewHolder
            }
            viewHolder.textView.text = aboRhList[position]
            return convertViewShadow
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val binding: AborhAndBranchDropdownItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.aborh_and_branch_dropdown_item, parent, false)
            val view = binding.root
            binding.uiViewModel = uiViewModel
            val textView = view.findViewById(R.id.dropdown_item) as TextView
            textView.text = aboRhList[position]
            return view
        }

    }

}