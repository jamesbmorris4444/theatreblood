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
    private lateinit var aboRhDropdownView: Spinner

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

    var hintTextAboRh: ObservableField<String> = ObservableField(activity.getString(R.string.donor_abo_rh))
    var dropdownAboRhVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)
    var currentAboRhSelectedValue: String = "NO ABO/Rh"

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
        DatePickerDialog(activity, uiViewModel.datePickerColorStyle, listener, year, month, day).show()
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
        rootView.findViewById<TextInputLayout>(R.id.edit_text_display_last_name).setHintTextAppearance(uiViewModel.editTextDisplayModifyHintStyle)
        rootView.findViewById<TextInputLayout>(R.id.edit_text_display_first_name).setHintTextAppearance(uiViewModel.editTextDisplayModifyHintStyle)
        rootView.findViewById<TextInputLayout>(R.id.edit_text_display_middle_name).setHintTextAppearance(uiViewModel.editTextDisplayModifyHintStyle)
        rootView.findViewById<TextInputLayout>(R.id.edit_text_display_dob).setHintTextAppearance(uiViewModel.editTextDisplayModifyHintStyle)

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

        aboRhDropdownView = rootView.findViewById(R.id.abo_rh_dropdown)
        aboRhDropdownView.background = uiViewModel.editTextDisplayModifyBackground.get()
        val dropdownArray = activity.resources.getStringArray(R.array.abo_rh_array)
        val aboRhAdapter = CustomSpinnerAdapter(activity, uiViewModel, dropdownArray)
        aboRhDropdownView.adapter = aboRhAdapter
        aboRhDropdownView.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                currentAboRhSelectedValue = parent.getItemAtPosition(position) as String
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