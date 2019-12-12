package com.fullsekurity.theatreblood.utils

import com.fullsekurity.theatreblood.activity.MainActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ColorMapper @Inject constructor() {

    // Usage:
    // private val colorMapper = ColorMapper()
    // colorMapper.initialize()
    // colorMapper.map(theme, "primaryDark")

    enum class LightColors(var colorName: String, val colorValue: String) {
        PINK("pink", "#E81388"),
        SUCCESS("success", "#4FC143"),
        WARNING("warning", "#FF9900"),
        ERROR("error", "#E40449"),
        WHITE("white", "#FFFFFF"),
        BLACK("black", "#000000"),
        VERY_LIGHT_GRAY("veryLightGray", "#13000000"),
        LIGHT_GRAY("lightGray", "#33000000"),
        MEDIUM_GRAY("mediumGray", "#86000000"),
        DARK_GRAY("darkGray", "#E6000000")
    }

    enum class DarkColors(var colorName: String, val colorValue: String) {

        PINK("pink", "#E81388"),
        SUCCESS("success", "#4FC143"),
        WARNING("warning", "#FF9900"),
        ERROR("error", "#E40449"),
        WHITE("white", "#FFFFFF"),
        BLACK("black", "#000000"),
        VERY_LIGHT_GRAY("veryLightGray", "#13FFFFFF"),
        LIGHT_GRAY("lightGray", "#33FFFFFF"),
        MEDIUM_GRAY("mediumGray", "#86FFFFFF"),
        DARK_GRAY("darkGray", "#E6FFFFFF")
    }

    private val colorMap: MutableList<MutableMap<String,String>> = mutableListOf()

    fun initialize() {
        val themes = enumValues<MainActivity.UITheme>()
        themes.forEach { theme ->
            if (theme == MainActivity.UITheme.LIGHT) {
                colorMap.add(mutableMapOf())
                val colors = enumValues<LightColors>()
                colors.forEach { color ->
                    colorMap[theme.ordinal][color.colorName] = color.colorValue
                }
            } else if (theme == MainActivity.UITheme.DARK) {
                colorMap.add(mutableMapOf())
                val colors = enumValues<DarkColors>()
                colors.forEach { color ->
                    colorMap[theme.ordinal][color.colorName] = color.colorValue
                }
            }
        }
    }

    fun map(theme: MainActivity.UITheme, colorName: String): String {
        return colorMap[theme.ordinal][colorName] ?: "#ffffff"
    }

}