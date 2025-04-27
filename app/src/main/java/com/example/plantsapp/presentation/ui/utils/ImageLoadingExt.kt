package com.example.plantsapp.presentation.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.example.plantsapp.R

fun Uri?.getBitmapWithPlaceholder(
    context: Context,
    @DrawableRes placeholder: Int = DEFAULT_PLACEHOLDER
): Bitmap {
    return Glide.with(context)
        .asBitmap()
        .load(this ?: placeholder)
        .submit()
        .get()
}

@DrawableRes
private val DEFAULT_PLACEHOLDER = R.drawable.ic_baseline_image_24
