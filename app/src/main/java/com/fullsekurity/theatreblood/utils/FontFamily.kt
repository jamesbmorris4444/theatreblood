package com.fullsekurity.theatreblood.utils

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.fullsekurity.theatreblood.R

class FontFamily {
    fun getFontResId(context: Context, font: String): Typeface? {
        return when (font) {
            "avenir_regular" -> ResourcesCompat.getFont(context, R.font.avenir_regular)
            "avenir_bold" -> ResourcesCompat.getFont(context, R.font.avenir_bold)
            "avenir_book" -> ResourcesCompat.getFont(context, R.font.avenir_book)
            else -> ResourcesCompat.getFont(context, R.font.avenir_regular)
        }
    }

    fun getFontResourceId(font: String): Int? {
        return when (font) {
            "avenir_regular" -> R.font.avenir_regular
            "avenir_bold" -> R.font.avenir_bold
            "avenir_book" -> R.font.avenir_book
            else -> R.font.avenir_regular
        }
    }

    companion object {
        fun newInstance(): FontFamily { return FontFamily() }
    }
}