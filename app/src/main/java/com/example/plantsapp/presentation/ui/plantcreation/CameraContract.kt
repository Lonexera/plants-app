package com.example.plantsapp.presentation.ui.plantcreation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract

class CameraContract : ActivityResultContract<Uri, Uri?>() {

    private var photoUri: Uri? = null

    override fun createIntent(context: Context, input: Uri): Intent {
        photoUri = input

        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(
                MediaStore.EXTRA_OUTPUT,
                input
            )
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (resultCode == Activity.RESULT_OK) {
            photoUri
        } else {
            null
        }
    }
}
