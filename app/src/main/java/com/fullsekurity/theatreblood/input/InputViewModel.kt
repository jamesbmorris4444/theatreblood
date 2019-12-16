package com.fullsekurity.theatreblood.input

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.DaggerRepositoryDependencyInjector
import com.fullsekurity.theatreblood.utils.RepositoryInjectorModule
import com.fullsekurity.theatreblood.utils.Utils
import com.google.android.material.textfield.TextInputLayout
import javax.inject.Inject

class InputViewModelFactory(private val activity: MainActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InputViewModel(activity) as T
    }
}

class InputViewModel(val activity: MainActivity) : AndroidViewModel(activity.application) {

    val donorSearchLiveData: MutableLiveData<List<Donor>> = MutableLiveData()
    private lateinit var rootView: View

    @Inject
    lateinit var repository: Repository

    init {
        DaggerRepositoryDependencyInjector.builder()
            .repositoryInjectorModule(RepositoryInjectorModule(activity))
            .build()
            .inject(this)
    }

    // observable used for two-way data binding. Values set into this field will show in view.
    // Text typed into EditText in view will be stored into this field after each character is typed.
    var editTextNameInput: ObservableField<String> = ObservableField("")
    fun onTextNameChanged(string: CharSequence, start: Int, before: Int, count: Int) {
        // within "string", the "count" characters beginning at index "start" have just replaced old text that had length "before"
    }

    var hintTextName: ObservableField<String> = ObservableField(activity.getString(R.string.donor_search_string))
    var editTextNameVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    fun onSubmitClicked(view: View) {
        loadData()
        Utils.hideKeyboard(view)
    }

    private fun loadData() {
        donorSearchLiveData.postValue(repository.donorsFromFullName(editTextNameInput.get() ?: ""))
    }

    fun setRootView(view: View, uiViewModel: UIViewModel) {
        rootView = view
        rootView.findViewById<TextInputLayout>(R.id.edit_text_input_name).setHintTextAppearance(uiViewModel.editTextDisplayModifyHintStyle)
    }

}