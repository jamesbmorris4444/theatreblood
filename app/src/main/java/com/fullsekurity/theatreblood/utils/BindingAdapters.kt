package com.fullsekurity.theatreblood.utils

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso

@BindingAdapter("android:src")
fun setImageUrl(view: ImageView, url: String?) {
    if (url != null) {
        Picasso
            .get()
            .load(Constants.SMALL_IMAGE_URL_PREFIX + url)
            .into(view)
    }
}

@BindingAdapter("android:src2")
fun setImageViewResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}

@BindingAdapter("tabBackground")
fun setIndicator(view: TabLayout, drawable: Drawable?) {
    view.background = drawable
}

@BindingAdapter("layout_marginTop")
fun setMarginTop(v: View, topMargin: Int) {
    v.layoutParams ?: return
    val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.topMargin = topMargin
    v.layoutParams = layoutParams
    v.requestLayout()
}

@BindingAdapter("layout_marginBottom")
fun setMarginBottom(v: View, bottomMargin: Int) {
    val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.bottomMargin = bottomMargin
    v.layoutParams = layoutParams
    v.requestLayout()
}

@BindingAdapter("layout_marginLeft")
fun setMarginLeft(v: View, leftMargin: Int) {
    val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.leftMargin = leftMargin
    v.layoutParams = layoutParams
    v.requestLayout()
}

@BindingAdapter("layout_marginRight")
fun setMarginRight(v: View, rightMargin: Int) {
    val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.rightMargin = rightMargin
    v.layoutParams = layoutParams
    v.requestLayout()
}

@BindingAdapter("layout_center_relativelayout")
fun setRelativeLayoutCenterHorizontal(v: View, gravity: Int) {
    // TRUE == anything but 0, we use 1
    // FALSE == 0
    val layoutParams: RelativeLayout.LayoutParams = v.layoutParams as RelativeLayout.LayoutParams
    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, gravity)
    v.layoutParams = layoutParams
    v.requestLayout()
}

@BindingAdapter("textview_gravity")
fun setTextViewGravity(v: TextView, gravity: Int) {
    v.gravity = gravity
    v.requestLayout()
}

@BindingAdapter("layout_marginBoth_computed", "view_fullscreen")
fun setMarginBothComputedFullScreen(v: View, leftandRightMargin: Int, viewFullscreen: Int) {
    val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
    when {
        viewFullscreen == 0 -> {
            // compute left and right margins as equal values to center the image
            layoutParams.leftMargin = leftandRightMargin
            layoutParams.rightMargin = leftandRightMargin
            layoutParams.width = v.context.resources.displayMetrics.widthPixels - 2 * leftandRightMargin
        }
        viewFullscreen == 1 -> {
            // expand image to full screen
            layoutParams.leftMargin = 0
            layoutParams.rightMargin = 0
            layoutParams.width = v.context.resources.displayMetrics.widthPixels
        }
        viewFullscreen == 2 -> // left justify image
            layoutParams.leftMargin = 0
        viewFullscreen < 0 -> {
            // left justify image with right margin = -viewFullscreen
            layoutParams.leftMargin = 0
            layoutParams.width = v.context.resources.displayMetrics.widthPixels + viewFullscreen
        }
        else -> {
            // use viewFullScreen for equal left and right margins, and center the image
            layoutParams.leftMargin = viewFullscreen
            layoutParams.rightMargin = viewFullscreen
            layoutParams.width = v.context.resources.displayMetrics.widthPixels - 2 * viewFullscreen
        }
    }
    v.layoutParams = layoutParams
    v.requestLayout()
}

@BindingAdapter("layout_marginLeft_computed")
fun setMarginLeftComputed(v: View, leftandRightMargin: Int) {
    val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.leftMargin = leftandRightMargin
    v.layoutParams = layoutParams
    v.requestLayout()
}

@BindingAdapter("layout_marginRight_computed")
fun setMarginRightComputed(v: View, leftandRightMargin: Int) {
    val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.rightMargin = leftandRightMargin
    v.layoutParams = layoutParams
    v.requestLayout()
}

@BindingAdapter("text_font")
fun setFont(view: TextView, font: String) {
    val typeface = FontFamily.newInstance().getFontResId(view.context, font)
    view.typeface = typeface
}

@BindingAdapter("include_font_padding")
fun setIncludeFontPadding(view: TextView, apply: Boolean) {
    if (apply) {
        view.includeFontPadding = true
    }
}

@BindingAdapter("text_color")
fun setTextColor(view: TextView, color: String) {
    view.setTextColor(Color.parseColor(color))
    // If color is ever needed as a drawable: Converters.convertColorToDrawable(0x00ff00)
}

@BindingAdapter("edit_text_underline_color")
fun setEditTextUnderlineColor(view: TextInputEditText, color: String) {
    val colorStateList = ColorStateList.valueOf(Color.parseColor(color))
    view.supportBackgroundTintList = colorStateList
}

@BindingAdapter("password_toggle_tint")
fun setPasswordToggleTint(view: TextInputLayout, color: String) {
    val colorStateList = ColorStateList.valueOf(Color.parseColor(color))
    view.setPasswordVisibilityToggleTintList(colorStateList)
}


@BindingAdapter("include_font_padding")
fun includeFontPadding(view: TextView, include: Int) {
    view.includeFontPadding = include == 1
}

@BindingAdapter("background_color")
fun setBackgroundColor(view: LinearLayout, color: String) {
    view.setBackgroundColor(Color.parseColor(color))
}


@BindingAdapter("text_size")
fun setTextSize(view: TextView, size: Float) {
    view.textSize = size
}

@BindingAdapter("card_view_background_color")
fun setCardColor(view: View, color: String) {
    (view as CardView).setCardBackgroundColor(Color.parseColor(color))
    // If color is ever needed as a drawable: Converters.convertColorToDrawable(0x00ff00)
}

@BindingAdapter("background_color")
fun setViewBackgroundColor(view: View, color: String) {
    if (view is ConstraintLayout) {
        view.setBackgroundColor(Color.parseColor(color))
    } else {
        view.setBackgroundColor(Color.parseColor(color))
    }
    // If color is ever needed as a drawable: Converters.convertColorToDrawable(0x00ff00)
}

@BindingAdapter("background_color")
fun setViewBackgroundColor(view: View, color: Int) {
    view.setBackgroundColor(color)
    // If color is ever needed as a drawable: Converters.convertColorToDrawable(0x00ff00)
}

@BindingAdapter("layout_width_dynamic")
fun setLayoutWidth(v: View, width: Int) {
    v.layoutParams.width = width
    v.requestLayout()
}

@BindingAdapter("layout_height_dynamic")
fun setLayoutHeight(v: View, height: Int) {
    v.layoutParams.height = height
    v.requestLayout()
}

@BindingAdapter("requestFocus")
fun requestFocus(view: TextView, requestFocus: Boolean) {
    if (requestFocus) {
        view.isFocusableInTouchMode = true
        view.requestFocus()
    }
}

@BindingAdapter("rightIcon")
fun setImageResource(editText: EditText, resource: Int) {
    editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, resource, 0);
}

@BindingAdapter("text_hint_color")
fun setTextHintColor(view: EditText, color: String) {
    view.setHintTextColor(Color.parseColor(color))
}

@BindingAdapter("set_hint")
fun setHint(view: TextView, hint: String) {
    view.hint = hint
}

@BindingAdapter("image_resource")
fun setImageResource(imageView: ImageView, resId: Int) {
    imageView.setImageResource(resId)
}

@BindingAdapter("background")
fun setRecyclerviewBackground(recyclerView: RecyclerView, color: String) {
    recyclerView.setBackgroundColor(Color.parseColor(color))
}

@BindingAdapter("recyclerViewViewModel")
fun setRecyclerViewViewModel(recyclerView: RecyclerView, viewModel: RecyclerViewViewModel?) {
    if (viewModel != null) {
        viewModel.setupRecyclerView(recyclerView)
    }
}