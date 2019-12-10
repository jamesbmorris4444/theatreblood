package com.fullsekurity.theatreblood.utils

import com.fullsekurity.theatreblood.activity.MainActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class
TextSizeMapper @Inject constructor() {

    // Usage:
    // private val textSizeMapper = TextSizeMapper()
    // textSizeMapper.initialize()
    // textSizeMapper.map(theme, "primaryDark")

    enum class LightTextSizes(var textSizeName: String, val textSizeValue: Float) {
        MAIN_TITLE("mainTitle", 22f),
        SUB_TITLE("subTitle", 20f),
        LARGE_TEXT("largeText", 16f),
        MEDIUM_TEXT("mediumText", 14f),
        SMALL_TEXT("smallText", 12f),
        MAIN_TITLE_BOLD("mainTitleBold", 22f),
        SUB_TITLE_BOLD("subTitleBold", 20f),
        LARGE_TEXT_BOLD("largeTextBold", 16f),
        MEDIUM_TEXT_BOLD("mediumTextBold", 14f),
        SMALL_TEXT_BOLD("smallTextBold", 12f)
    }

    enum class DarkTextSizes(var textSizeName: String, val textSizeValue: Float) {
        MAIN_TITLE("mainTitle", 22f),
        SUB_TITLE("subTitle", 20f),
        LARGE_TEXT("largeText", 16f),
        MEDIUM_TEXT("mediumText", 14f),
        SMALL_TEXT("smallText", 12f),
        MAIN_TITLE_BOLD("mainTitleBold", 22f),
        SUB_TITLE_BOLD("subTitleBold", 20f),
        LARGE_TEXT_BOLD("largeTextBold", 16f),
        MEDIUM_TEXT_BOLD("mediumTextBold", 14f),
        SMALL_TEXT_BOLD("smallTextBold", 12f)
    }

    private val textSizeMap: MutableList<MutableMap<String,Float>> = mutableListOf()

    fun initialize() {
        val themes = enumValues<MainActivity.UITheme>()
        themes.forEach { theme ->
            if (theme == MainActivity.UITheme.LIGHT) {
                textSizeMap.add(mutableMapOf())
                val textSizes = enumValues<LightTextSizes>()
                textSizes.forEach { textSize ->
                    textSizeMap[theme.ordinal][textSize.textSizeName] = textSize.textSizeValue
                }
            } else if (theme == MainActivity.UITheme.DARK) {
                textSizeMap.add(mutableMapOf())
                val textSizes = enumValues<DarkTextSizes>()
                textSizes.forEach { textSize ->
                    textSizeMap[theme.ordinal][textSize.textSizeName] = textSize.textSizeValue
                }
            }
        }
    }

    fun map(theme: MainActivity.UITheme, textSizeName: String): Float {
        return textSizeMap[theme.ordinal][textSizeName] ?: 12f
    }

}