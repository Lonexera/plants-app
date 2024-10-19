package com.example.plantsapp.presentation.ui.authentication

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import timber.log.Timber

class GoogleSignInContract(
    val getGoogleSignInClient: () -> GoogleSignInClient
) : ActivityResultContract<Unit?, String?>() {

    override fun createIntent(context: Context, input: Unit?): Intent {
        return getGoogleSignInClient().signInIntent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        return intent?.let {
             try {
                val account = GoogleSignIn
                    .getSignedInAccountFromIntent(it)
                    .getResult(ApiException::class.java)
                account.idToken
            } catch (e: ApiException) {
                Timber.e(e)
                null
            }
        }
    }
}
