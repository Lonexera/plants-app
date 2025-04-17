package com.example.plantsapp.presentation.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.plantsapp.R

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateToAuth: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigateToAuth.collect {
            onNavigateToAuth()
        }
    }

    ProfileScreen(
        uiState = uiState,
        onSignOut = viewModel::onSignOut
    )
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreen(
    uiState: ProfileViewModel.UiState,
    onSignOut: () -> Unit,
) {
    val user = uiState.user
    var showSignOutWarning by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterVertically,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (user != null) {
                GlideImage(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(112.dp),
                    model = user.profilePicture,
                    contentDescription = "User avatar",
                    contentScale = ContentScale.Crop,
                    failure = placeholder(R.drawable.placeholder_person),
                )

                Text(
                    text = user.name,
                    fontSize = 22.sp
                )
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
                .align(Alignment.BottomCenter),
            onClick = { showSignOutWarning = true },
        ) {
            Text(text = stringResource(R.string.title_btn_sign_out))
        }
    }

    if (showSignOutWarning) {
        BasicAlertDialog(
            onDismissRequest = { showSignOutWarning = false }
        ) {
            Surface(
                modifier = Modifier.clip(RoundedCornerShape(10.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.title_sign_out_dialog_confirmation),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Text(
                        text = stringResource(R.string.msg_sign_out_dialog_confirmation),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = onSignOut,
                        ) {
                            Text(text = stringResource(R.string.title_dialog_confirmation_btn_positive))
                        }

                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = { showSignOutWarning = false },
                        ) {
                            Text(text = stringResource(R.string.title_dialog_confirmation_btn_negative))
                        }
                    }
                }
            }
        }
    }
}
