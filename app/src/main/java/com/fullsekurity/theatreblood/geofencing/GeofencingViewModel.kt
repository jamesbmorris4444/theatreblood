package com.fullsekurity.theatreblood.geofencing

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.Callbacks
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.storage.GeofencingData
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class GeofencingViewModelFactory(private val callbacks: Callbacks) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GeofencingViewModel(callbacks) as T
    }
}

@Suppress("UNCHECKED_CAST")
class GeofencingViewModel(private val callbacks: Callbacks) : AndroidViewModel(callbacks.fetchActivity().application) {

    @Inject
    lateinit var uiViewModel: UIViewModel
    @Inject
    lateinit var repository: Repository
    private var geofencingDataList: MutableList<GeofencingData> = mutableListOf()
    var geofencingLiveData = MutableLiveData<MutableList<GeofencingData>>()

    init {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(callbacks.fetchActivity()))
            .build()
            .inject(this)
    }

    // latitude
    // observable used for two-way donations binding. Values set into this field will show in view.
    // Text typed into EditText in view will be stored into this field after each character is typed.
    var editTextDisplayLatitude: ObservableField<String> = ObservableField("")
    fun onTextLatitudeChanged(string: CharSequence, start: Int, before: Int, count: Int) {
        // within "string", the "count" characters beginning at index "start" have just replaced old text that had length "before"
    }
    var hintTextLatitude: ObservableField<String> = ObservableField(getApplication<Application>().applicationContext.getString(R.string.latitude_hint))
    var editTextLatitudeVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    // longitude
    var editTextDisplayLongitude: ObservableField<String> = ObservableField("")
    fun onTextLongitudeChanged(string: CharSequence, start: Int, before: Int, count: Int) {
    }
    var hintTextLongitude: ObservableField<String> = ObservableField(getApplication<Application>().applicationContext.getString(R.string.longitude_hint))
    var editTextLongitudeVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    // radius
    var editTextDisplayRadius: ObservableField<String> = ObservableField("")
    fun onTextRadiusChanged(string: CharSequence, start: Int, before: Int, count: Int) {
    }
    var hintTextRadius: ObservableField<String> = ObservableField(getApplication<Application>().applicationContext.getString(R.string.radius_hint))
    var editTextRadiusVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    fun onSubmitClicked(view: View) {
        val latitude = "32.865665"
        val longitude = "-96.929779"
        val radius = "400"
//        editTextDisplayLatitude.get()?.let { editTextDisplayLatitude ->
//            latitude = editTextDisplayLatitude
//        }
//        editTextDisplayLongitude.get()?.let { editTextDisplayLongitude ->
//            longitude = editTextDisplayLongitude
//        }
//        editTextDisplayRadius.get()?.let { editTextDisplayRadius ->
//            radius = editTextDisplayRadius
//        }
        geofencingDataList.add(GeofencingData(latitude.toDouble(), longitude.toDouble(), radius.toFloat()))
        geofencingLiveData.postValue(geofencingDataList)
    }

}