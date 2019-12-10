package com.fullsekurity.theatreblood.ui

import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.MainActivity
import io.reactivex.subjects.ReplaySubject

class UIDataModel {
    val uiDataClassObservable: ReplaySubject<UIDataClass> = ReplaySubject.create()
    private var uiDataClass: UIDataClass? = null
    private var lightViewUIDataClass: UIDataClass? = null
    private fun createViewData(theme: MainActivity.UITheme): UIDataClass? {
        return when (theme) {
            MainActivity.UITheme.LIGHT, MainActivity.UITheme.NOT_ASSIGNED -> {
                if (uiDataClass == null) {
                    uiDataClass = UIDataClass(
                        R.drawable.lt_standard_modal_background,
                        R.drawable.ic_dashed_line,
                            320f,
                            "lightGray",
                            "lightGray",
                            48f,
                            24f,
                            "darkestGray",
                            "mediumText",
                            50f,
                            50f,
                            14f,
                            14f,
                            "darkestGray",
                            "mediumText",
                            48f,
                            "tertiary",
                            "mediumText",
                            36f,
                            "darkestGray",
                            "regularText",
                            "tertiary",
                            "darkestGray",
                            "regularText",
                            12f,
                            "success",
                            "regularText",
                            76f,
                            "darkestGray",
                            "smallText",
                            "lightGray",
                            "textField",
                            8f,
                            20f,
                            12f,
                            3f,
                            "darkestGray",
                            "mediumText"

                    )
                }
                uiDataClass
            }
            MainActivity.UITheme.DARK -> {
                if (lightViewUIDataClass == null) {
                    lightViewUIDataClass = UIDataClass(
                        R.drawable.dk_standard_modal_background,
                        R.drawable.ic_dashed_line,
                            320f,
                            "lightGray",
                            "lightGray",
                            48f,
                            24f,
                            "darkestGray",
                            "mediumText",
                            50f,
                            50f,
                            14f,
                            14f,
                            "darkestGray",
                            "mediumText",
                            48f,
                            "tertiary",
                            "mediumText",
                            36f,
                            "darkestGray",
                            "regularText",
                            "tertiary",
                            "darkestGray",
                            "regularText",
                            12f,
                            "success",
                            "regularText",
                            76f,
                            "darkestGray",
                            "smallText",
                            "lightGray",
                            "textField",
                            8f,
                            20f,
                            12f,
                            3f,
                            "darkestGray",
                            "mediumText"

                    )
                }
                lightViewUIDataClass
            }
        }
    }

    fun loadData(theme: MainActivity.UITheme) {
        dataReceived(theme)
    }

    private fun dataReceived(theme: MainActivity.UITheme) {
        createViewData(theme)?.let {
            uiDataClassObservable.onNext(it)
        }
    }

}
