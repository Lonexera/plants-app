package com.example.plantsapp.presentation.ui.authentication

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.example.plantsapp.R

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onNavigateToTasks: () -> Unit,
) {
    val uiState by viewModel.authState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.error.collect { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT)
                .show()
        }
    }

    when (val state = uiState) {
        is AuthViewModel.AuthState.Auth -> AuthScreen(
            uiState = state,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onSignInClick = viewModel::signIn
        )

        AuthViewModel.AuthState.Loading -> {
            Loading()
        }

        AuthViewModel.AuthState.NavigateToTasks -> LaunchedEffect(uiState) { onNavigateToTasks() }
    }
}

@Composable
private fun AuthScreen(
    uiState: AuthViewModel.AuthState.Auth,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            painter = painterResource(R.drawable.auth_image),
            contentDescription = "plants",
        )

        Text(
            text = stringResource(R.string.app_name),
            style = TextStyle.Default.copy(
                // TODO: create a text style
                fontFamily = FontFamily(Font(R.font.roboto_bold)),
                fontSize = 32.sp,
            ),
            color = colorResource(R.color.green_primary),
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            modifier = Modifier.fillMaxWidth(0.8f),
            text = stringResource(R.string.title_auth_quote),
            style = TextStyle.Default.copy(
                // TODO: create a text style
                fontFamily = FontFamily(Font(R.font.roboto)),
                fontSize = 24.sp,
            ),
            textAlign = TextAlign.Center,
            color = colorResource(R.color.dark_green),
        )

        Text(
            modifier = Modifier.fillMaxWidth(0.8f),
            text = stringResource(R.string.title_auth_quote_author),
            style = TextStyle.Default.copy(
                // TODO: create a text style
                fontFamily = FontFamily(Font(R.font.roboto)),
                fontSize = 18.sp,
            ),
            textAlign = TextAlign.End,
            color = colorResource(R.color.green_300),
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 16.dp
                ),
            value = uiState.email,
            onValueChange = onEmailChange,
            label = {
                Text(text = "Email")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
            )
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 16.dp
                ),
            value = uiState.password,
            onValueChange = onPasswordChange,
            label = {
                Text(text = "Password")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
            )
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.green_primary)
            ),
            onClick = dropUnlessResumed { onSignInClick() },
        ) {
            Text(text = stringResource(R.string.title_sign_in_btn))
        }
    }
}

@Composable
private fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
