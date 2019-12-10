package com.fullsekurity.theatreblood.utils

import com.fullsekurity.theatreblood.activity.MainActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class
TypefaceMapper @Inject constructor() {

    // Usage:
    // private val typefaceMapper = TypefaceMapper()
    // typefaceMapper.initialize()
    // typefaceMapper.map(theme, "primaryDark")

    enum class LightTypefaces(var textSizeName: String, val typefaceValue: String) {
        MAIN_TITLE("mainTitle", "avenir_regular"),
        SUB_TITLE("subTitle", "avenir_regular"),
        LARGE_TEXT("largeText", "avenir_book"),
        MEDIUM_TEXT("mediumText", "avenir_book"),
        SMALL_TEXT("smallText", "avenir_book"),
        MAIN_TITLE_BOLD("mainTitleBold", "avenir_bold"),
        SUB_TITLE_BOLD("subTitleBold", "avenir_bold"),
        LARGE_TEXT_BOLD("largeTextBold", "avenir_bold"),
        MEDIUM_TEXT_BOLD("mediumTextBold", "avenir_bold"),
        SMALL_TEXT_BOLD("smallTextBold", "avenir_bold")
    }

    enum class DarkTypefaces(var textSizeName: String, val typefaceValue: String) {
        MAIN_TITLE("mainTitle", "avenir_regular"),
        SUB_TITLE("subTitle", "avenir_regular"),
        LARGE_TEXT("largeText", "avenir_book"),
        MEDIUM_TEXT("mediumText", "avenir_book"),
        SMALL_TEXT("smallText", "avenir_book"),
        MAIN_TITLE_BOLD("mainTitleBold", "avenir_bold"),
        SUB_TITLE_BOLD("subTitleBold", "avenir_bold"),
        LARGE_TEXT_BOLD("largeTextBold", "avenir_bold"),
        MEDIUM_TEXT_BOLD("mediumTextBold", "avenir_bold"),
        SMALL_TEXT_BOLD("smallTextBold", "avenir_bold")
    }

    private val typefaceMap: MutableList<MutableMap<String,String>> = mutableListOf()

    fun initialize() {
        val themes = enumValues<MainActivity.UITheme>()
        themes.forEach { theme ->
            if (theme == MainActivity.UITheme.LIGHT) {
                typefaceMap.add(mutableMapOf())
                val typefaces = enumValues<LightTypefaces>()
                typefaces.forEach { typeface ->
                    typefaceMap[theme.ordinal][typeface.textSizeName] = typeface.typefaceValue
                }
            } else if (theme == MainActivity.UITheme.DARK) {
                typefaceMap.add(mutableMapOf())
                val typefaces = enumValues<DarkTypefaces>()
                typefaces.forEach { typeface ->
                    typefaceMap[theme.ordinal][typeface.textSizeName] = typeface.typefaceValue
                }
            }
        }
    }

    fun map(theme: MainActivity.UITheme, textSizeName: String): String {
        return typefaceMap[theme.ordinal][textSizeName] ?: "avenir_regular"
    }

}