package com.fullsekurity.theatreblood.ui

import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.MainActivity

class UIDataModel {
    private var lightViewUIDataClass: UIDataClass? = null
    private var darkViewUIDataClass: UIDataClass? = null
    private fun createViewData(theme: MainActivity.UITheme): UIDataClass? {
        return when (theme) {
            MainActivity.UITheme.LIGHT, MainActivity.UITheme.NOT_ASSIGNED -> {
                if (lightViewUIDataClass == null) {
                    lightViewUIDataClass = UIDataClass(
                        R.drawable.lt_background,
                        "background_full_screen_train.json", // background_full_screen_train.json
                        R.drawable.lt_standard_modal_background,
                        R.drawable.lt_dashed_line,
                        "lightGray",
                        "lightGray",
                        48f,
                        24f,
                        "darkGray",
                        "mediumTextBold",
                        50f,
                        50f,
                        14f,
                        14f,
                        "darkGray",
                        "mediumText",
                        48f,
                        "secondary",
                        "mediumText",
                        36f,
                        "darkGray",
                        "mediumText",
                        "secondary",
                        "darkGray",
                        "mediumText",
                        12f,
                        "success",
                        "mediumText",
                        76f,
                        "darkGray",
                        "smallText",
                        "lightGray",
                        "smallText",
                        8f,
                        20f,
                        12f,
                        3f,
                        "darkGray",
                        "mediumText",

                        "veryLightGray",
                        "lighterGray",
                        "primary",
                        "secondary",
                        "white",

                        "mediumGray",
                        "black",
                        "subTitle",
                        "veryLightGray",
                        "primary",
                        R.drawable.edit_text_background,

                        "mediumGray",
                        "largeText",
                        "black",
                        "subTitle",
                        "veryLightGray",
                        "primary",
                        R.drawable.edit_text_background,

                        R.drawable.button_light,
                        "white",
                        "subTitle",

                        "black",
                        "largeText",

                        "error",
                        "mainTitle",

                        "primary",

                        "white",
                        "mainTitle",

                        "white",
                        "mainTitle",
                        R.drawable.lt_blood_grid_11,
                        R.drawable.lt_blood_grid_12,
                        R.drawable.lt_blood_grid_21,
                        R.drawable.lt_blood_grid_22
                    )
                }
                lightViewUIDataClass
            }
            MainActivity.UITheme.DARK -> {
                if (darkViewUIDataClass == null) {
                    darkViewUIDataClass = UIDataClass(
                        R.drawable.dk_background,
                        "background_full_screen_night.json", // background_full_screen_night.json
                        R.drawable.dk_standard_modal_background,
                        R.drawable.lt_dashed_line,
                        "white",
                        "lightGray",
                        48f,
                        24f,
                        "darkGray",
                        "mediumText",
                        50f,
                        50f,
                        14f,
                        14f,
                        "darkGray",
                        "mediumText",
                        48f,
                        "secondary",
                        "mediumText",
                        36f,
                        "darkGray",
                        "mediumText",
                        "secondary",
                        "darkGray",
                        "mediumText",
                        12f,
                        "success",
                        "mediumText",
                        76f,
                        "darkGray",
                        "smallText",
                        "lightGray",
                        "smallText",
                        8f,
                        20f,
                        12f,
                        3f,
                        "darkGray",
                        "mediumText",

                        "darkGray",
                        "mediumGray",
                        "primary",
                        "secondary",
                        "white",

                        "darkGray",
                        "black",
                        "subTitle",
                        "veryLightGray",
                        "primary",
                        R.drawable.edit_text_background,

                        "mediumGray",
                        "largeText",
                        "black",
                        "subTitle",
                        "veryLightGray",
                        "primary",
                        R.drawable.edit_text_background,

                        R.drawable.button_dark,
                        "white",
                        "subTitle",

                        "white",
                        "largeText",

                        "error",
                        "mainTitle",

                        "primary",

                        "white",
                        "mainTitle",

                        "white",
                        "mainTitle",
                        R.drawable.lt_blood_grid_11,
                        R.drawable.lt_blood_grid_12,
                        R.drawable.lt_blood_grid_21,
                        R.drawable.lt_blood_grid_22


                    )
                }
                darkViewUIDataClass
            }
        }
    }

    fun loadData(theme: MainActivity.UITheme) : UIDataClass? {
        return createViewData(theme)
    }

}
