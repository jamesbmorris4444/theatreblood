

package com.fullsekurity.theatreblood.ui

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.utils.*
import com.fullsekurity.theatreblood.utils.Constants.STANDARD_BUTTON_HEIGHT
import com.fullsekurity.theatreblood.utils.Constants.STANDARD_EDIT_TEXT_HEIGHT
import com.fullsekurity.theatreblood.utils.Constants.STANDARD_LEFT_AND_RIGHT_MARGIN
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class UIViewModelFactory(private val activity: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UIViewModel(activity) as T
    }
}

class UIViewModel(val activity: Application) : AndroidViewModel(activity) {

    private val TAG = UIViewModel::class.java.simpleName

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

    val editTextNameHintColor: ObservableField<String> = ObservableField("#ffffff")
    val editTextNameColor: ObservableField<String> = ObservableField("#ffffff")
    val editTextNameSize: ObservableField<Float> = ObservableField(0f)
    val editTextNameBackgroundColor: ObservableField<String> = ObservableField("#ffffff")
    val editTextNameTypeface: ObservableField<String> = ObservableField("")
    val editTextNameUpperHintColor: ObservableField<String> = ObservableField("#ffffff")
    val editTextNameBackground: ObservableField<Drawable> = ObservableField()

    val editTextDisplayModifyHintColor: ObservableField<String> = ObservableField("#ffffff")
    val editTextDisplayModifyColor: ObservableField<String> = ObservableField("#ffffff")
    val editTextDisplayModifySize: ObservableField<Float> = ObservableField(0f)
    val editTextDisplayModifyBackgroundColor: ObservableField<String> = ObservableField("#ffffff")
    val editTextDisplayModifyTypeface: ObservableField<String> = ObservableField("")
    val editTextDisplayModifyUpperHintColor: ObservableField<String> = ObservableField("#ffffff")
    val editTextDisplayModifyBackground: ObservableField<Drawable> = ObservableField()

    val buttonTextColor: ObservableField<String> = ObservableField("#ffffff")
    val buttonTextSize: ObservableField<Float> = ObservableField(0f)
    val buttonTextTypeface: ObservableField<String> = ObservableField("")

    val donorItemTextColor: ObservableField<String> = ObservableField("#ffffff")
    val donorItemTextSize: ObservableField<Float> = ObservableField(0f)
    val donorItemTextTypeface: ObservableField<String> = ObservableField("")

    var standardLeftAndRightMargin: ObservableField<Int> = ObservableField(0)
    var standardDialogInternalWidth: ObservableField<Int> = ObservableField(0)
    var standardWidth: ObservableField<Int> = ObservableField(0)
    var standardEditTextHeight: ObservableField<Int> = ObservableField(0)
    var standardWidthWithButton: ObservableField<Int> = ObservableField(0)
    var standardButtonWidth: ObservableField<Int> = ObservableField(0)
    var standardButtonHeight: ObservableField<Int> = ObservableField(0)

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
                LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("LOAD THEME for %s  ->  %s", currentTheme.name, value.name))
                val settings = context.getSharedPreferences("THEME", Context.MODE_PRIVATE)
                val editor = settings.edit()
                editor.putString("THEME", value.name)
                editor.apply()
                uiDataClass = uiDataModel.loadData(value)
                liveDataUpdate(value)
            }
            field = value
        }
    private val uiDataModel = UIDataModel()
    private var uiDataClass: UIDataClass? = null

    init {
        DaggerMapperDependencyInjector.builder()
                .mapperInjectorModule(MapperInjectorModule())
                .build()
                .inject(this)
        standardLeftAndRightMargin.set(convertDpToPixels(STANDARD_LEFT_AND_RIGHT_MARGIN))
        standardDialogInternalWidth.set(computeStandardInternalWidth())
        standardWidth.set(computeStandardWidth())
        standardEditTextHeight.set(convertDpToPixels(STANDARD_EDIT_TEXT_HEIGHT))
        standardWidthWithButton.set(computeStandardWidthWithButton())
        standardButtonWidth.set(computeStandarButtonWidth())
        standardButtonHeight.set(convertDpToPixels(STANDARD_BUTTON_HEIGHT))
    }

    private fun convertDpToPixels(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
    }

    private fun computeStandardInternalWidth(): Int {
        // |<--standard margin-->|<--standard margin-->|<--standard internal width-->|<--standard margin-->|<--standard margin-->|
        // |<---------------------------------------------------total width----------------------------------------------------->|

        val screenWidth = context.resources.displayMetrics.widthPixels
        return screenWidth - 4 * convertDpToPixels(STANDARD_LEFT_AND_RIGHT_MARGIN)
    }

    private fun computeStandardWidth(): Int {
        // |<--standard margin-->|<--standard width-->|<--standard margin-->|
        // |<-----------------------total width---------------------------->|

        val screenWidth = context.resources.displayMetrics.widthPixels
        return screenWidth - 2 * convertDpToPixels(STANDARD_LEFT_AND_RIGHT_MARGIN)
    }

    private fun computeStandardWidthWithButton(): Int {
        // |<--standard margin-->|<--standard width with button-->|<--standard margin-->|<--standard button width-->|<--standard margin-->|
        // |<----------------------------------------------------total width------------------------------------------------------------->|

        val screenWidth = context.resources.displayMetrics.widthPixels
        val standardMargin = convertDpToPixels(STANDARD_LEFT_AND_RIGHT_MARGIN)
        val totalButtonWidth = screenWidth - 3 * standardMargin
        return totalButtonWidth * Constants.EDIT_TEXT_TO_BUTTON_RATIO / (Constants.EDIT_TEXT_TO_BUTTON_RATIO + 1)
    }

    private fun computeStandarButtonWidth(): Int {
        // |<--standard margin-->|<--standard width with button-->|<--standard margin-->|<--standard button width-->|<--standard margin-->|
        // |<----------------------------------------------------total width------------------------------------------------------------->|

        val screenWidth = context.resources.displayMetrics.widthPixels
        val standardMargin = convertDpToPixels(STANDARD_LEFT_AND_RIGHT_MARGIN)
        val totalButtonWidth = screenWidth - 3 * standardMargin
        return totalButtonWidth / (Constants.EDIT_TEXT_TO_BUTTON_RATIO + 1)
    }

    private fun liveDataUpdate(theme: MainActivity.UITheme) {

        uiDataClass?.let { uiDataClass ->
            standardDialogBackground.set(ContextCompat.getDrawable(context, uiDataClass.standardDialogBackground))
            standardDialogDashedLine.set(ContextCompat.getDrawable(context, uiDataClass.standardDialogDashedLine))
            standardDialogWidth.set(convertDpToPixels(uiDataClass.standardDialogWidth))
            standardDialogDividerColor.set(colorMapper.map(theme, uiDataClass.standardDialogDividerColor))
            standardDialogPasswordHintColor.set(colorMapper.map(theme, uiDataClass.standardDialogPasswordHintColor))
            standardDialogTopSpacerHeight.set(convertDpToPixels(uiDataClass.standardDialogTopSpacerHeight))
            standardDialogSubTitleHeight.set(convertDpToPixels(uiDataClass.standardDialogSubTitleHeight))
            standardDialogTitleTextColor.set(colorMapper.map(theme, uiDataClass.standardDialogTitleTextColor))
            standardDialogTitleTextSize.set(textSizeMapper.map(theme, uiDataClass.standardDialogTitleTextSize))
            standardDialogTitleTextTypeface.set(typefaceMapper.map(theme, uiDataClass.standardDialogTitleTextSize))
            standardDialogIconHeight.set(convertDpToPixels(uiDataClass.standardDialogIconHeight))
            standardDialogIconWidth.set(convertDpToPixels(uiDataClass.standardDialogIconWidth))
            standardDialogSubIconHeight.set(convertDpToPixels(uiDataClass.standardDialogSubIconHeight))
            standardDialogSubBodyHeight.set(convertDpToPixels(uiDataClass.standardDialogSubBodyHeight))
            standardDialogBodyTextColor.set(colorMapper.map(theme, uiDataClass.standardDialogBodyTextColor))
            standardDialogBodyTextSize.set(textSizeMapper.map(theme, uiDataClass.standardDialogBodyTextSize))
            standardDialogBodyTextTypeface.set(typefaceMapper.map(theme, uiDataClass.standardDialogBodyTextSize))
            standardDialogButtonHeight.set(convertDpToPixels(uiDataClass.standardDialogButtonHeight))
            standardDialogButtonTextColor.set(colorMapper.map(theme, uiDataClass.standardDialogButtonTextColor))
            standardDialogButtonTextSize.set(textSizeMapper.map(theme, uiDataClass.standardDialogButtonTextSize))
            standardDialogButtonTextTypeface.set(typefaceMapper.map(theme, uiDataClass.standardDialogButtonTextSize))
            standardDialogPasswordHeight.set(convertDpToPixels(uiDataClass.standardDialogPasswordHeight))
            standardDialogPasswordTextColor.set(colorMapper.map(theme, uiDataClass.standardDialogPasswordTextColor))
            standardDialogPasswordTextSize.set(textSizeMapper.map(theme, uiDataClass.standardDialogPasswordTextSize))
            standardDialogPasswordTextTypeface.set(typefaceMapper.map(theme, uiDataClass.standardDialogPasswordTextSize))
            standardDialogPasswordToggleColor.set(colorMapper.map(theme, uiDataClass.standardDialogPasswordToggleColor))
            standardDialogListCenterTextColor.set(colorMapper.map(theme, uiDataClass.standardDialogListCenterTextColor))
            standardDialogListCenterTextSize.set(textSizeMapper.map(theme, uiDataClass.standardDialogListCenterTextSize))
            standardDialogListCenterTextTypeface.set(typefaceMapper.map(theme, uiDataClass.standardDialogListCenterTextSize))
            standardDialogListCenterTextMarginLeft.set(convertDpToPixels(uiDataClass.standardDialogListCenterTextMarginLeft))
            standardDialogListAmountTextColor.set(colorMapper.map(theme, uiDataClass.standardDialogListAmountTextColor))
            standardDialogListAmountTextSize.set(textSizeMapper.map(theme, uiDataClass.standardDialogListAmountTextSize))
            standardDialogListAmountTextTypeface.set(typefaceMapper.map(theme, uiDataClass.standardDialogListAmountTextSize))
            standardDialogListLongMarginLeft.set(convertDpToPixels(uiDataClass.standardDialogListLongMarginLeft))
            standardDialogListTextColor.set(colorMapper.map(theme, uiDataClass.standardDialogListTextColor))
            standardDialogListTextSize.set(textSizeMapper.map(theme, uiDataClass.standardDialogListTextSize))
            standardDialogListTextTypeface.set(typefaceMapper.map(theme, uiDataClass.standardDialogListTextSize))
            standardDialogListSmallTextColor.set(colorMapper.map(theme, uiDataClass.standardDialogListSmallTextColor))
            standardDialogListSmallTextSize.set(textSizeMapper.map(theme, uiDataClass.standardDialogListSmallTextSize))
            standardDialogListSmallTextTypeface.set(typefaceMapper.map(theme, uiDataClass.standardDialogListSmallTextSize))
            standardDialogListTitleMarginTop.set(convertDpToPixels(uiDataClass.standardDialogListTitleMarginTop))
            standardDialogListTitleHeight.set(convertDpToPixels(uiDataClass.standardDialogListTitleHeight))
            standardDialogListLine1MarginTop.set(convertDpToPixels(uiDataClass.standardDialogListLine1MarginTop))
            standardDialogListLine2MarginTop.set(convertDpToPixels(uiDataClass.standardDialogListLine2MarginTop))
            standardDialogListFooterTextColor.set(colorMapper.map(theme, uiDataClass.standardDialogListFooterTextColor))
            standardDialogListFooterTextSize.set(textSizeMapper.map(theme, uiDataClass.standardDialogListFooterTextSize))
            standardDialogListFooterTextTypeface.set(typefaceMapper.map(theme, uiDataClass.standardDialogListFooterTextSize))

            editTextNameHintColor.set(colorMapper.map(theme, uiDataClass.editTextNameHintColor))
            editTextNameColor.set(colorMapper.map(theme, uiDataClass.editTextNameColor))
            editTextNameSize.set(textSizeMapper.map(theme, uiDataClass.editTextNameSize))
            editTextNameBackgroundColor.set(colorMapper.map(theme, uiDataClass.editTextNameBackgroundColor))
            editTextNameTypeface.set(typefaceMapper.map(theme, uiDataClass.editTextNameSize))
            editTextNameUpperHintColor.set(colorMapper.map(theme, uiDataClass.editTextNameUpperHintColor))
            editTextNameBackground.set(ContextCompat.getDrawable(context, uiDataClass.editTextNameBackground))

            editTextDisplayModifyHintColor.set(colorMapper.map(theme, uiDataClass.editTextDisplayModifyHintColor))
            editTextDisplayModifyColor.set(colorMapper.map(theme, uiDataClass.editTextDisplayModifyColor))
            editTextDisplayModifySize.set(textSizeMapper.map(theme, uiDataClass.editTextDisplayModifySize))
            editTextDisplayModifyBackgroundColor.set(colorMapper.map(theme, uiDataClass.editTextDisplayModifyBackgroundColor))
            editTextDisplayModifyTypeface.set(typefaceMapper.map(theme, uiDataClass.editTextDisplayModifySize))
            editTextDisplayModifyUpperHintColor.set(colorMapper.map(theme, uiDataClass.editTextDisplayModifyUpperHintColor))
            editTextDisplayModifyBackground.set(ContextCompat.getDrawable(context, uiDataClass.editTextDisplayModifyBackground))

            buttonTextColor.set(colorMapper.map(theme, uiDataClass.buttonTextColor))
            buttonTextSize.set(textSizeMapper.map(theme, uiDataClass.buttonTextSize))
            buttonTextTypeface.set(typefaceMapper.map(theme, uiDataClass.buttonTextSize))

            donorItemTextColor.set(colorMapper.map(theme, uiDataClass.donorItemTextColor))
            donorItemTextSize.set(textSizeMapper.map(theme, uiDataClass.donorItemTextSize))
            donorItemTextTypeface.set(typefaceMapper.map(theme, uiDataClass.donorItemTextSize))

        }
    }

}