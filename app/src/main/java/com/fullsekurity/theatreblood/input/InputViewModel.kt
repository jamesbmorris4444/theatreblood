package com.fullsekurity.theatreblood.input

import android.app.Application
import android.content.Context
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fullsekurity.theatreblood.activity.MainActivity

@Suppress("UNCHECKED_CAST")
class InputViewModelFactory(private val activity: MainActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InputViewModel(activity) as T
    }
}

@Suppress("UNCHECKED_CAST")
class InputViewModel(val activity: MainActivity) : AndroidViewModel(activity.application) {
    private val liveInputDataObject: MutableLiveData<String> = MutableLiveData()
    private val context: Context = getApplication<Application>().applicationContext

    // observable used for two-way data binding. Values set into this field will show in view.
    // Text typed into EditText in view will be stored into this field after each character is typed.
    var editTextNameInput: ObservableField<String> = ObservableField("")

    var hintTextName: ObservableField<String> = ObservableField("")
    var editTextNameVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    fun onTextNameChanged(string: CharSequence, start: Int, before: Int, count: Int) {
        // within "string", the "count" characters beginning at index "start" have just replaced old text that had length "before"
    }

    fun onItemClick() {

    }
}