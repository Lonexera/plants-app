package com.example.plantsapp.presentation.ui.utils

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.plantsapp.R
import com.example.plantsapp.domain.model.Task

@StringRes fun Task.getTitleRes(): Int {
    return when (this) {
        is Task.WateringTask -> R.string.title_watering_task
        is Task.SprayingTask -> R.string.title_spraying_task
        is Task.LooseningTask -> R.string.title_loosening_task
        is Task.TakingPhotoTask -> R.string.title_taking_photo_task
    }
}

@DrawableRes fun Task.getIconRes(): Int {
    return when (this) {
        is Task.WateringTask -> R.drawable.ic_watering
        is Task.SprayingTask -> R.drawable.ic_spraying
        is Task.LooseningTask -> R.drawable.ic_loosening
        is Task.TakingPhotoTask -> R.drawable.ic_taking_photo
    }
}

@ColorRes fun Task.getColorRes(): Int {
    return when (this) {
        is Task.WateringTask -> R.color.blue
        is Task.SprayingTask -> R.color.purple
        is Task.LooseningTask -> R.color.brown
        is Task.TakingPhotoTask -> R.color.green_blue
    }
}
