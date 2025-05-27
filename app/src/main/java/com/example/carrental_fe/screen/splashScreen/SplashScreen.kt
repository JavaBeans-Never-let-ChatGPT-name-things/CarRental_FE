package com.example.carrental_fe.screen.splashScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carrental_fe.R
import com.example.carrental_fe.dto.response.TokenResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    viewModel: SplashScreenViewModel = viewModel(factory = SplashScreenViewModel.Factory),
    onNavigateToLoginScreen: () -> Unit,
    onNavigateToHomeScreen: (TokenResponse) -> Unit
) {
    val loggedInState by viewModel.loggedInState.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize()
    )
    {
        Image(painter = painterResource(R.drawable.load_screen),
            contentDescription = null,
            modifier = Modifier.fillMaxSize())
    }
    LaunchedEffect(loggedInState) {
        when (loggedInState) {
            is LoggedInState.Success -> {
                onNavigateToHomeScreen((loggedInState as LoggedInState.Success).tokenResponse)
            }
            is LoggedInState.Error -> {
                launch {
                    delay(1500)
                    onNavigateToLoginScreen()
                }
            }
            else -> {}
        }
    }
}