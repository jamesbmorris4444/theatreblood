

package com.fullsekurity.theatreblood.ui

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.logger.LogUtils.TagFilter.THM
import com.fullsekurity.theatreblood.utils.*
import com.fullsekurity.theatreblood.utils.Constants.STANDARD_BUTTON_HEIGHT
import com.fullsekurity.theatreblood.utils.Constants.STANDARD_EDIT_TEXT_HEIGHT
import com.fullsekurity.theatreblood.utils.Constants.STANDARD_EDIT_TEXT_SMALL_MARGIN
import com.fullsekurity.theatreblood.utils.Constants.STANDARD_GRID_EDIT_TEXT_HEIGHT
import com.fullsekurity.theatreblood.utils.Constants.STANDARD_GRID_HEIGHT
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

    val standardBackground: ObservableField<Drawable> = ObservableField()
    var backgroundLottieJsonFileName = ""
    val backgroundVisibleForLottie: ObservableField<Boolean> = ObservableField(false)
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

    val editTextCursor: ObservableField<Int> = ObservableField(0)

    val editTextLabelColor: ObservableField<String> = ObservableField("#ffffff")
    val editTextLabelSize: ObservableField<Float> = ObservableField(0f)
    val editTextLabelTypeface: ObservableField<String> = ObservableField("")
    val editTextColor: ObservableField<String> = ObservableField("#ffffff")
    val editTextSize: ObservableField<Float> = ObservableField(0f)
    val editTextTypeface: ObservableField<String> = ObservableField("")
    val editTextUpperHintColor: ObservableField<String> = ObservableField("#ffffff")
    val editTextLowerHintColor: ObservableField<String> = ObservableField("#ffffff")
    val editTextBackground: ObservableField<Drawable> = ObservableField()
    val editTextBackgroundResInt: ObservableField<Int> = ObservableField(0)

    val buttonDrawable: ObservableField<Drawable> = ObservableField()
    val largeButtonDrawable: ObservableField<Drawable> = ObservableField()
    val buttonTextColor: ObservableField<String> = ObservableField("#ffffff")
    val buttonTextSize: ObservableField<Float> = ObservableField(0f)
    val buttonTextTypeface: ObservableField<String> = ObservableField("")

    val donorItemTextColor: ObservableField<String> = ObservableField("#ffffff")
    val donorItemTextSize: ObservableField<Float> = ObservableField(0f)
    val donorItemTextTypeface: ObservableField<String> = ObservableField("")

    val largeErrorTextColor: ObservableField<String> = ObservableField("#ffffff")
    val largeErrorTextSize: ObservableField<Float> = ObservableField(0f)
    val largeErrorTextTypeface: ObservableField<String> = ObservableField("")

    val radioButtonColor: ObservableField<String> = ObservableField("#ffffff")
    val dropdownBackground: ObservableField<Drawable> = ObservableField()

    val productItemTextColor: ObservableField<String> = ObservableField("#ffffff")
    val productItemTextSize: ObservableField<Float> = ObservableField(0f)
    val productItemTextTypeface: ObservableField<String> = ObservableField("")

    val productGridBackgroundDrawable11: ObservableField<Drawable> = ObservableField()
    val productGridBackgroundDrawable12: ObservableField<Drawable> = ObservableField()
    val productGridBackgroundDrawable21: ObservableField<Drawable> = ObservableField()
    val productGridBackgroundDrawable22: ObservableField<Drawable> = ObservableField()

    val editTextProductHintColor: ObservableField<String> = ObservableField("#ffffff")
    val editTextProductColor: ObservableField<String> = ObservableField("#ffffff")
    val editTextAboRhProductColor: ObservableField<String> = ObservableField("#ffffff")
    val editTextProductSize: ObservableField<Float> = ObservableField(0f)
    val editTextProductTypeface: ObservableField<String> = ObservableField("")
    val editTextProductBackground: ObservableField<Drawable> = ObservableField()

    val productIdTextColor: ObservableField<String> = ObservableField("#ffffff")
    val productIdTextSize: ObservableField<Float> = ObservableField(0f)
    val productIdTextTypeface: ObservableField<String> = ObservableField("")

    val incorrectDonorTextColor: ObservableField<String> = ObservableField("#ffffff")
    val incorrectDonorTextSize: ObservableField<Float> = ObservableField(0f)
    val incorrectDonorTextTypeface: ObservableField<String> = ObservableField("")

    val dropdownTextColor: ObservableField<String> = ObservableField("#ffffff")
    val dropdownTextSize: ObservableField<Float> = ObservableField(0f)
    val dropdownTextTypeface: ObservableField<String> = ObservableField("")

    // computed values

    var standardLeftAndRightMargin: ObservableField<Int> = ObservableField(0)
    var standardEditTextProductSmallMargin: ObservableField<Int> = ObservableField(0)
    val standardEditTextProductInnerWidth: ObservableField<Int> = ObservableField(0)
    var standardDialogInternalWidth: ObservableField<Int> = ObservableField(0)
    var standardWidth: ObservableField<Int> = ObservableField(0)
    var standardHalfWidth: ObservableField<Int> = ObservableField(0)
    var standardGridHeight: ObservableField<Int> = ObservableField(0)
    var standardEditTextHeight: ObservableField<Int> = ObservableField(0)
    var standardGridEditTextHeight: ObservableField<Int> = ObservableField(0)
    var standardWidthWithButton: ObservableField<Int> = ObservableField(0)
    var standardButtonWidth: ObservableField<Int> = ObservableField(0)
    var standardButtonHeight: ObservableField<Int> = ObservableField(0)
    var standardLargeButtonWidth: ObservableField<Int> = ObservableField(0)
    var editTextDisplayModifyHintStyle = 0
    var datePickerColorStyle = 0

    private val context: Context = getApplication<Application>().applicationContext
    val modalErrorIcon: Drawable? = ContextCompat.getDrawable(context, R.drawable.mo_close_error)

    @Inject
    lateinit var colorMapper: ColorMapper
    @Inject
    lateinit var textSizeMapper: TextSizeMapper
    @Inject
    lateinit var typefaceMapper: TypefaceMapper

    var currentTheme: MainActivity.UITheme = MainActivity.UITheme.NOT_ASSIGNED
        set(value) {
            if (value != currentTheme) {
                LogUtils.D(TAG, LogUtils.FilterTags.withTags(THM), String.format("LOAD THEME for %s  ->  %s", currentTheme.name, value.name))
                val settings = context.getSharedPreferences("THEME", Context.MODE_PRIVATE)
                val editor = settings.edit()
                editor.putString("THEME", value.name)
                editor.apply()
                uiDataClass = uiDataModel.loadData(value)
                themeSwitcher(value)
            }
            field = value
        }
    private val uiDataModel = UIDataModel()
    private var uiDataClass: UIDataClass? = null
    lateinit var recyclerViewAlternatingColor1: String
    lateinit var recyclerViewAlternatingColor2: String
    lateinit var primaryColor: String
    lateinit var secondaryColor: String
    lateinit var toolbarTextColor: String

    init {
        DaggerMapperDependencyInjector.builder()
                .mapperInjectorModule(MapperInjectorModule())
                .build()
                .inject(this)
        standardLeftAndRightMargin.set(convertDpToPixels(STANDARD_LEFT_AND_RIGHT_MARGIN))
        standardEditTextProductSmallMargin.set(convertDpToPixels(STANDARD_EDIT_TEXT_SMALL_MARGIN))
        standardEditTextProductInnerWidth.set(computeStandardEditTextInnerWidth())
        standardDialogInternalWidth.set(computeStandardInternalWidth())
        standardWidth.set(computeStandardWidth())
        standardHalfWidth.set(computeStandardWidth() / 2)
        standardGridHeight.set(convertDpToPixels(STANDARD_GRID_HEIGHT))
        standardEditTextHeight.set(convertDpToPixels(STANDARD_EDIT_TEXT_HEIGHT))
        standardGridEditTextHeight.set(convertDpToPixels(STANDARD_GRID_EDIT_TEXT_HEIGHT))
        standardWidthWithButton.set(computeStandardWidthWithButton())
        standardButtonWidth.set(computeStandarButtonWidth())
        standardLargeButtonWidth.set(computeStandarButtonWidth() * 2)
        standardButtonHeight.set(convertDpToPixels(STANDARD_BUTTON_HEIGHT))
        standardDialogWidth.set(computeStandardWidth())
    }

    fun lottieAnimation(view: LottieAnimationView, jsonFile: String, repeatCount: Int) {
        val animationView: LottieAnimationView = view
        if (jsonFile.isEmpty()) {
            backgroundVisibleForLottie.set(false)
        } else {
            backgroundVisibleForLottie.set(true)
            animationView.visibility = View.VISIBLE
            animationView.setAnimation(jsonFile)
            animationView.playAnimation()
            animationView.repeatCount = repeatCount
        }
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

    private fun computeStandardEditTextInnerWidth(): Int {
        // |<--edit text small margin-->|<--edit text small width-->|<--edit text small margin-->|
        // |<--------------------------standard width / 2--------------------------------------->|

        val screenWidth = context.resources.displayMetrics.widthPixels
        val standardWidthDivideBy2 = (screenWidth - 2 * convertDpToPixels(STANDARD_LEFT_AND_RIGHT_MARGIN)) / 2
        return standardWidthDivideBy2 - 2 * convertDpToPixels(STANDARD_EDIT_TEXT_SMALL_MARGIN)
    }

    private fun computeStandardWidth(): Int {
        // |<--standard margin-->|<--standard width-->|<--standard margin-->|
        // |<-----------------------total width---------------------------->|

        val screenWidth = context.resources.displayMetrics.widthPixels
        return screenWidth - 2 * convertDpToPixels(STANDARD_LEFT_AND_RIGHT_MARGIN)
    }

    private fun computeStandardWidthWithButton(): Int {
        // |<--standard margin-->|<--standard width with button_light-->|<--standard margin-->|<--standard button_light width-->|<--standard margin-->|
        // |<----------------------------------------------------total width------------------------------------------------------------->|

        val screenWidth = context.resources.displayMetrics.widthPixels
        val standardMargin = convertDpToPixels(STANDARD_LEFT_AND_RIGHT_MARGIN)
        val totalButtonWidth = screenWidth - 3 * standardMargin
        return totalButtonWidth * Constants.EDIT_TEXT_TO_BUTTON_RATIO / (Constants.EDIT_TEXT_TO_BUTTON_RATIO + 1)
    }

    private fun computeStandarButtonWidth(): Int {
        // |<--standard margin-->|<--standard width with button_light-->|<--standard margin-->|<--standard button_light width-->|<--standard margin-->|
        // |<----------------------------------------------------total width------------------------------------------------------------->|

        val screenWidth = context.resources.displayMetrics.widthPixels
        val standardMargin = convertDpToPixels(STANDARD_LEFT_AND_RIGHT_MARGIN)
        val totalButtonWidth = screenWidth - 3 * standardMargin
        return totalButtonWidth / (Constants.EDIT_TEXT_TO_BUTTON_RATIO + 1)
    }

    private fun themeSwitcher(theme: MainActivity.UITheme) {
        uiDataClass?.let { uiDataClass ->

            // change colors in these styles to match any changes to colors in UIDataModel
            if (theme == MainActivity.UITheme.LIGHT) {
                editTextDisplayModifyHintStyle = R.style.TextInputLayoutForLight
                datePickerColorStyle = R.style.DatePickerDialogThemeForLight
            } else {
                editTextDisplayModifyHintStyle = R.style.TextInputLayoutForDark
                datePickerColorStyle = R.style.DatePickerDialogThemeForDark
            }

            recyclerViewAlternatingColor1 = colorMapper.map(theme, uiDataClass.recyclerViewAlternatingColor1)
            recyclerViewAlternatingColor2 = colorMapper.map(theme, uiDataClass.recyclerViewAlternatingColor2)
            primaryColor = colorMapper.map(theme, uiDataClass.primaryColor)
            secondaryColor = colorMapper.map(theme, uiDataClass.secondaryColor)
            toolbarTextColor = colorMapper.map(theme, uiDataClass.toolbarTextColor)
            standardBackground.set(ContextCompat.getDrawable(context, uiDataClass.standardBackground))
            backgroundLottieJsonFileName = uiDataClass.backgroundLottieJsonFileName
            backgroundVisibleForLottie.set(backgroundLottieJsonFileName.isNotEmpty())
            standardDialogBackground.set(ContextCompat.getDrawable(context, uiDataClass.standardDialogBackground))
            standardDialogDashedLine.set(ContextCompat.getDrawable(context, uiDataClass.standardDialogDashedLine))
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

            editTextCursor.set(uiDataClass.editTextCursor)

            editTextLabelColor.set(colorMapper.map(theme, uiDataClass.editTextLabelColor))
            editTextLabelSize.set(textSizeMapper.map(theme, uiDataClass.editTextLabelSize))
            editTextLabelTypeface.set(typefaceMapper.map(theme, uiDataClass.editTextLabelSize))
            editTextColor.set(colorMapper.map(theme, uiDataClass.editTextColor))
            editTextSize.set(textSizeMapper.map(theme, uiDataClass.editTextSize))
            editTextTypeface.set(typefaceMapper.map(theme, uiDataClass.editTextSize))
            editTextUpperHintColor.set(colorMapper.map(theme, uiDataClass.editTextUpperHintColor))
            editTextLowerHintColor.set(colorMapper.map(theme, uiDataClass.editTextLowerHintColor))
            editTextBackground.set(ContextCompat.getDrawable(context, uiDataClass.editTextBackground))
            editTextBackgroundResInt.set(uiDataClass.editTextBackground)

            buttonDrawable.set(ContextCompat.getDrawable(context, uiDataClass.buttonDrawable)?.mutate())
            largeButtonDrawable.set(ContextCompat.getDrawable(context, uiDataClass.buttonDrawable)?.mutate())
            buttonTextColor.set(colorMapper.map(theme, uiDataClass.buttonTextColor))
            buttonTextSize.set(textSizeMapper.map(theme, uiDataClass.buttonTextSize))
            buttonTextTypeface.set(typefaceMapper.map(theme, uiDataClass.buttonTextSize))

            donorItemTextColor.set(colorMapper.map(theme, uiDataClass.donorItemTextColor))
            donorItemTextSize.set(textSizeMapper.map(theme, uiDataClass.donorItemTextSize))
            donorItemTextTypeface.set(typefaceMapper.map(theme, uiDataClass.donorItemTextSize))

            largeErrorTextColor.set(colorMapper.map(theme, uiDataClass.largeErrorTextColor))
            largeErrorTextSize.set(textSizeMapper.map(theme, uiDataClass.largeErrorTextSize))
            largeErrorTextTypeface.set(typefaceMapper.map(theme, uiDataClass.largeErrorTextSize))

            radioButtonColor.set(colorMapper.map(theme, uiDataClass.radioButtonColor))
            dropdownBackground.set(ContextCompat.getDrawable(context, uiDataClass.dropdownBackground))

            productItemTextColor.set(colorMapper.map(theme, uiDataClass.productItemTextColor))
            productItemTextSize.set(textSizeMapper.map(theme, uiDataClass.productItemTextSize))
            productItemTextTypeface.set(typefaceMapper.map(theme, uiDataClass.productItemTextSize))

            productGridBackgroundDrawable11.set(ContextCompat.getDrawable(context, uiDataClass.productGridBackgroundDrawable11))
            productGridBackgroundDrawable12.set(ContextCompat.getDrawable(context, uiDataClass.productGridBackgroundDrawable12))
            productGridBackgroundDrawable21.set(ContextCompat.getDrawable(context, uiDataClass.productGridBackgroundDrawable21))
            productGridBackgroundDrawable22.set(ContextCompat.getDrawable(context, uiDataClass.productGridBackgroundDrawable22))

            editTextProductHintColor.set(colorMapper.map(theme, uiDataClass.editTextProductHintColor))
            editTextProductColor.set(colorMapper.map(theme, uiDataClass.editTextProductColor))
            editTextAboRhProductColor.set(colorMapper.map(theme, uiDataClass.editTextAboRhProductColor))
            editTextProductSize.set(textSizeMapper.map(theme, uiDataClass.editTextProductSize))
            editTextProductTypeface.set(typefaceMapper.map(theme, uiDataClass.editTextProductSize))
            editTextProductBackground.set(ContextCompat.getDrawable(context, uiDataClass.editTextProductBackground))

            productIdTextColor.set(colorMapper.map(theme, uiDataClass.productIdTextColor))
            productIdTextSize.set(textSizeMapper.map(theme, uiDataClass.productIdTextSize))
            productIdTextTypeface.set(typefaceMapper.map(theme, uiDataClass.productIdTextSize))

            incorrectDonorTextColor.set(colorMapper.map(theme, uiDataClass.incorrectDonorTextColor))
            incorrectDonorTextSize.set(textSizeMapper.map(theme, uiDataClass.incorrectDonorTextSize))
            incorrectDonorTextTypeface.set(typefaceMapper.map(theme, uiDataClass.incorrectDonorTextSize))

            dropdownTextColor.set(colorMapper.map(theme, uiDataClass.dropdownTextColor))
            dropdownTextSize.set(textSizeMapper.map(theme, uiDataClass.dropdownTextSize))
            dropdownTextTypeface.set(typefaceMapper.map(theme, uiDataClass.dropdownTextSize))
        }
    }

}