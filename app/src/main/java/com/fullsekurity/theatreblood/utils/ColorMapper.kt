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
        PRIMARY("primary", "#5B0691"),
        SECONDARY("secondary", "#e50239"),
        SUCCESS("success", "#00ff00"),
        WARNING("warning", "#ff9900"),
        ERROR("error", "#E40449"),
        WHITE("white", "#ffffff"),
        BLACK("black", "#000000"),
        VERY_LIGHT_GRAY("veryLightGray", "#08000000"),
        LIGHTER_GRAY("lighterGray", "#38000000"),
        LIGHT_GRAY("lightGray", "#68000000"),
        MEDIUM_GRAY("mediumGray", "#98000000"),
        DARK_GRAY("darkGray", "#c8000000"),
        DARKEST_GRAY("darkestGray", "#e8000000")
    }

    enum class DarkColors(var colorName: String, val colorValue: String) {
        PRIMARY("primary", "#191970"),
        SECONDARY("secondary", "#e50239"),
        SUCCESS("success", "00ff00"),
        WARNING("warning", "#ff9900"),
        ERROR("error", "#E40449"),
        WHITE("white", "#ffffff"),
        BLACK("black", "#000000"),
        CYAN("cyan", "#00ffff"),
        DARKEST_GRAY("darkestGray", "#08ffffff"),
        VERY_LIGHT_GRAY("darkGray", "#38ffffff"),
        LIGHTER_GRAY("mediumGray", "#68ffffff"),
        LIGHT_GRAY("lightGray", "#98ffffff"),
        MEDIUM_GRAY("lighterGray", "#c8ffffff"),
        DARK_GRAY("veryLightGray", "#e8ffffff")
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