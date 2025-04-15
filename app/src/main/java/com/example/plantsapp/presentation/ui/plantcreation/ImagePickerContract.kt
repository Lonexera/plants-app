package com.example.plantsapp.presentation.ui.plantcreation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class ImagePickerContract : ActivityResultContract<Unit?, Uri?>() {

    override fun createIntent(context: Context, input: Unit?): Intent {
        return Intent(Intent.ACTION_GET_CONTENT).apply {
            type = MIMETYPE_IMAGE
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (resultCode == Activity.RESULT_OK) {
            intent?.data
        } else {
            null
        }
    }

    companion object {
        private const val MIMETYPE_IMAGE = "image/*"
    }
}
