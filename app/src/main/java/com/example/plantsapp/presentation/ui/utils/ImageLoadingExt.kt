package com.example.plantsapp.presentation.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.plantsapp.R

fun <T> ImageView.loadPicture(
    picture: T,
    @DrawableRes placeholder: Int = DEFAULT_PLACEHOLDER
) {
    Glide.with(context)
        .load(picture)
        .centerCrop()
        .placeholder(placeholder)
        .into(this)
}

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
