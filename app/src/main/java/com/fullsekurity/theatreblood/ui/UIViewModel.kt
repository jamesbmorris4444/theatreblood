

package com.fullsekurity.theatreblood.ui

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.utils.*
import com.fullsekurity.theatreblood.utils.Constants.STANDARD_LEFT_AND_RIGHT_MARGIN
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class UIViewModelFactory(private val activity: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UIViewModel(activity) as T
    }
}

class UIViewModel(val activity: Application) : AndroidViewModel(activity) {

    // See DashboardDataClassHome class for description of fields
    val standardDialogWidth: ObservableField<Int> = ObservableField(0)
    val standardDialogDividerColor: ObservableField<String> = ObservableField("#ffffff")
    val standardDialogPasswordHintColor: ObservableField<String> = ObservableField("#ffffff")
    val standardDialogBackground: ObservableField<Drawable> = ObservableField()
    val standardDialogTopSpacerHeight: ObservableField<Int> = ObservableField(0)
    val standardDialogSubTitleHeight: ObservableField<Int> = ObservableField(0)
    val standardDialogTitleTextColor: ObservableField<String> = ObservableField("#ffffff")
    val standardDialogTitleTextSize: ObservableField<Float> = ObservableField(0f)
    val standardDialogTitleTextTypeface: ObservableField<String> = ObservableField("")
    val standardDialogIconHeight: ObservableField<Int> = ObservableField(0)
    val standardDialogIconWidth: ObservableField<Int> = ObservableField(0)
    val standardDialogSubIconHeight: ObservableField<Int> = ObservableField(0)
    val standardDialogSubBodyHeight: ObservableField<Int> = ObservableField(0)
    val standardDialogBodyTextColor: ObservableField<String> = ObservableField("#ffffff")
    val standardDialogBodyTextSize: ObservableField<Float> = ObservableField(0f)
    val standardDialogBodyTextTypeface: ObservableField<String> = ObservableField("")
    val standardDialogButtonHeight: ObservableField<Int> = ObservableField(0)
    val standardDialogButtonTextColor: ObservableField<String> = ObservableField("#ffffff")
    val standardDialogButtonTextSize: ObservableField<Float> = ObservableField(0f)
    val standardDialogButtonTextTypeface: ObservableField<String> = ObservableField("")
    val standardDialogPasswordHeight: ObservableField<Int> = ObservableField(0)
    val standardDialogPasswordTextColor: ObservableField<String> = ObservableField("#ffffff")
    val standardDialogPasswordTextSize: ObservableField<Float> = ObservableField(0f)
    val standardDialogPasswordTextTypeface: ObservableField<String> = ObservableField("")
    val standardDialogPasswordToggleColor: ObservableField<String> = ObservableField("#ffffff")
    val standardDialogListCenterTextColor: ObservableField<String> = ObservableField("#ffffff")
    val standardDialogListCenterTextSize: ObservableField<Float> = ObservableField(0f)
    val standardDialogListCenterTextTypeface: ObservableField<String> = ObservableField("")
    val standardDialogListCenterTextMarginLeft: ObservableField<Int> = ObservableField(0)
    val standardDialogListAmountTextColor: ObservableField<String> = ObservableField("#ffffff")
    val standardDialogListAmountTextSize: ObservableField<Float> = ObservableField(0f)
    val standardDialogListAmountTextTypeface: ObservableField<String> = ObservableField("")
    val standardDialogListLongMarginLeft: ObservableField<Int> = ObservableField(0)
    val standardDialogDashedLine: ObservableField<Drawable> = ObservableField()
    val standardDialogListTextColor: ObservableField<String> = ObservableField("#ffffff")
    val standardDialogListTextSize: ObservableField<Float> = ObservableField(0f)
    val standardDialogListTextTypeface: ObservableField<String> = ObservableField("")
    val standardDialogListSmallTextColor: ObservableField<String> = ObservableField("#ffffff")
    val standardDialogListSmallTextSize: ObservableField<Float> = ObservableField(0f)
    val standardDialogListSmallTextTypeface: ObservableField<String> = ObservableField("")
    val standardDialogListTitleMarginTop: ObservableField<Int> = ObservableField(0)
    val standardDialogListTitleHeight: ObservableField<Int> = ObservableField(0)
    val standardDialogListLine1MarginTop: ObservableField<Int> = ObservableField(0)
    val standardDialogListLine2MarginTop: ObservableField<Int> = ObservableField(0)
    val standardDialogListFooterTextColor: ObservableField<String> = ObservableField("#ffffff")
    val standardDialogListFooterTextSize: ObservableField<Float> = ObservableField(0f)
    val standardDialogListFooterTextTypeface: ObservableField<String> = ObservableField("")

    var standardLeftAndRightMargin: ObservableField<Int> = ObservableField(0)
    var standardDialogInternalWidth: ObservableField<Int> = ObservableField(0)

    private val context: Context = getApplication<Application>().applicationContext
    val modalCloseErrorIcon: Drawable? = ContextCompat.getDrawable(context, R.drawable.mo_close_error)

    @Inject
    lateinit var colorMapper: ColorMapper
    @Inject
    lateinit var textSizeMapper: TextSizeMapper
    @Inject
    lateinit var typefaceMapper: TypefaceMapper

    var currentTheme: MainActivity.UITheme = MainActivity.UITheme.NOT_ASSIGNED
        set(value) {
            if (value != currentTheme) {
                uiDataModel.loadData(value)
            }
            field = value
        }
    private val uiDataModel = UIDataModel()
    val liveUIDataClass: MutableLiveData<UIDataClass> = MutableLiveData()
    private var disposable: Disposable? = null
    private lateinit var UIDataClass: UIDataClass

    init {
        DaggerMapperDependencyInjector.builder()
                .mapperInjectorModule(MapperInjectorModule(context))
                .build()
                .inject(this)
        disposable = uiDataModel.uiDataClassObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { uiViewData ->
                    liveUIDataClass.postValue(uiViewData)
                    this.UIDataClass = uiViewData
                }
        standardLeftAndRightMargin.set(convertDpToPixels(STANDARD_LEFT_AND_RIGHT_MARGIN))
        standardDialogInternalWidth.set(computeStandardInternalWidth())
    }

    private fun convertDpToPixels(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
    }

    private fun computeStandardInternalWidth(): Int {
        // |<--standard margin-->|<--standard margin-->|<--standard internal width-->|<--standard margin-->|<--standard margin-->|
        // |<---------------------------------------------------total width----------------------------------------------------->|

        val screenWidth = context.resources.displayMetrics.widthPixels
        var internalWidth = screenWidth - 320 // internal margin in the extremely rare case that standardLeftAndRightMargin.get() is null
        standardLeftAndRightMargin.get()?.let {
            internalWidth = screenWidth - 4 * it
        }
        return internalWidth
    }

    override fun onCleared() {
        disposable?.let {
            it.dispose()
            disposable = null
        }
    }

    fun liveDataUpdate() {
        standardDialogBackground.set(ContextCompat.getDrawable(context, UIDataClass.standardDialogBackground))
        standardDialogDashedLine.set(ContextCompat.getDrawable(context, UIDataClass.standardDialogDashedLine))
        standardDialogWidth.set(convertDpToPixels(UIDataClass.standardDialogWidth))
        standardDialogDividerColor.set(colorMapper.map(currentTheme, UIDataClass.standardDialogDividerColor))
        standardDialogPasswordHintColor.set(colorMapper.map(currentTheme, UIDataClass.standardDialogPasswordHintColor))
        standardDialogTopSpacerHeight.set(convertDpToPixels(UIDataClass.standardDialogTopSpacerHeight))
        standardDialogSubTitleHeight.set(convertDpToPixels(UIDataClass.standardDialogSubTitleHeight))
        standardDialogTitleTextColor.set(colorMapper.map(currentTheme, UIDataClass.standardDialogTitleTextColor))
        standardDialogTitleTextSize.set(textSizeMapper.map(currentTheme, UIDataClass.standardDialogTitleTextSize))
        standardDialogTitleTextTypeface.set(typefaceMapper.map(currentTheme, UIDataClass.standardDialogTitleTextSize))
        standardDialogIconHeight.set(convertDpToPixels(UIDataClass.standardDialogIconHeight))
        standardDialogIconWidth.set(convertDpToPixels(UIDataClass.standardDialogIconWidth))
        standardDialogSubIconHeight.set(convertDpToPixels(UIDataClass.standardDialogSubIconHeight))
        standardDialogSubBodyHeight.set(convertDpToPixels(UIDataClass.standardDialogSubBodyHeight))
        standardDialogBodyTextColor.set(colorMapper.map(currentTheme, UIDataClass.standardDialogBodyTextColor))
        standardDialogBodyTextSize.set(textSizeMapper.map(currentTheme, UIDataClass.standardDialogBodyTextSize))
        standardDialogBodyTextTypeface.set(typefaceMapper.map(currentTheme, UIDataClass.standardDialogBodyTextSize))
        standardDialogButtonHeight.set(convertDpToPixels(UIDataClass.standardDialogButtonHeight))
        standardDialogButtonTextColor.set(colorMapper.map(currentTheme, UIDataClass.standardDialogButtonTextColor))
        standardDialogButtonTextSize.set(textSizeMapper.map(currentTheme, UIDataClass.standardDialogButtonTextSize))
        standardDialogButtonTextTypeface.set(typefaceMapper.map(currentTheme, UIDataClass.standardDialogButtonTextSize))
        standardDialogPasswordHeight.set(convertDpToPixels(UIDataClass.standardDialogPasswordHeight))
        standardDialogPasswordTextColor.set(colorMapper.map(currentTheme, UIDataClass.standardDialogPasswordTextColor))
        standardDialogPasswordTextSize.set(textSizeMapper.map(currentTheme, UIDataClass.standardDialogPasswordTextSize))
        standardDialogPasswordTextTypeface.set(typefaceMapper.map(currentTheme, UIDataClass.standardDialogPasswordTextSize))
        standardDialogPasswordToggleColor.set(colorMapper.map(currentTheme, UIDataClass.standardDialogPasswordToggleColor))
        standardDialogListCenterTextColor.set(colorMapper.map(currentTheme, UIDataClass.standardDialogListCenterTextColor))
        standardDialogListCenterTextSize.set(textSizeMapper.map(currentTheme, UIDataClass.standardDialogListCenterTextSize))
        standardDialogListCenterTextTypeface.set(typefaceMapper.map(currentTheme, UIDataClass.standardDialogListCenterTextSize))
        standardDialogListCenterTextMarginLeft.set(convertDpToPixels(UIDataClass.standardDialogListCenterTextMarginLeft))
        standardDialogListAmountTextColor.set(colorMapper.map(currentTheme, UIDataClass.standardDialogListAmountTextColor))
        standardDialogListAmountTextSize.set(textSizeMapper.map(currentTheme, UIDataClass.standardDialogListAmountTextSize))
        standardDialogListAmountTextTypeface.set(typefaceMapper.map(currentTheme, UIDataClass.standardDialogListAmountTextSize))
        standardDialogListLongMarginLeft.set(convertDpToPixels(UIDataClass.standardDialogListLongMarginLeft))
        standardDialogListTextColor.set(colorMapper.map(currentTheme, UIDataClass.standardDialogListTextColor))
        standardDialogListTextSize.set(textSizeMapper.map(currentTheme, UIDataClass.standardDialogListTextSize))
        standardDialogListTextTypeface.set(typefaceMapper.map(currentTheme, UIDataClass.standardDialogListTextSize))
        standardDialogListSmallTextColor.set(colorMapper.map(currentTheme, UIDataClass.standardDialogListSmallTextColor))
        standardDialogListSmallTextSize.set(textSizeMapper.map(currentTheme, UIDataClass.standardDialogListSmallTextSize))
        standardDialogListSmallTextTypeface.set(typefaceMapper.map(currentTheme, UIDataClass.standardDialogListSmallTextSize))
        standardDialogListTitleMarginTop.set(convertDpToPixels(UIDataClass.standardDialogListTitleMarginTop))
        standardDialogListTitleHeight.set(convertDpToPixels(UIDataClass.standardDialogListTitleHeight))
        standardDialogListLine1MarginTop.set(convertDpToPixels(UIDataClass.standardDialogListLine1MarginTop))
        standardDialogListLine2MarginTop.set(convertDpToPixels(UIDataClass.standardDialogListLine2MarginTop))
        standardDialogListFooterTextColor.set(colorMapper.map(currentTheme, UIDataClass.standardDialogListFooterTextColor))
        standardDialogListFooterTextSize.set(textSizeMapper.map(currentTheme, UIDataClass.standardDialogListFooterTextSize))
        standardDialogListFooterTextTypeface.set(typefaceMapper.map(currentTheme, UIDataClass.standardDialogListFooterTextSize))
    }

}