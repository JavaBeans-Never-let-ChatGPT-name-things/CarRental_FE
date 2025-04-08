package com.example.carrental_fe.screen.forgot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carrental_fe.*
import com.example.carrental_fe.dialog.ErrorDialog
import com.example.carrental_fe.dialog.LoadingDialog
import com.example.carrental_fe.screen.component.*

@Composable
fun ForgotPasswordScreen(
    forgotPasswordViewModel: ForgotPasswordViewModel = viewModel(factory = ForgotPasswordViewModel.Factory),
    onBackNav: () -> Unit,
    onSendEmailSuccessNav: (String) -> Unit)
{
    val value by forgotPasswordViewModel.email.collectAsState()
    val forgotPasswordState by forgotPasswordViewModel.forgotPasswordState.collectAsState()

    var showErrorDialog by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ){
            Spacer(modifier = Modifier.height(50.dp))

            BackButton(onClick = onBackNav, iconResId = R.drawable.back_icon)

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Forgot Password",
                fontSize = 28.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Enter your Email account to reset\n" +
                        "your password",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                color = Color(0xFF707B81),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(50.dp))
            InputField(value = value, placeHolder = "Email", onValueChange = { forgotPasswordViewModel.onEmailChange(it) })
            Spacer(modifier = Modifier.height(50.dp))
            CustomButton(
                onClickChange = { forgotPasswordViewModel.sendEmail() }, text = "Send Email", textColor = 0xFFFFFFFF,
                backgroundColor = Color(0xFF0D6EFD),
                imageResId = null
            )
        }
    }
    LaunchedEffect(forgotPasswordState) {
        when (forgotPasswordState) {
            is ForgotPasswordState.Loading -> showLoadingDialog = true
            is ForgotPasswordState.Error -> {
                showLoadingDialog = false
                showErrorDialog = true
            }
            is ForgotPasswordState.Success -> {
                showLoadingDialog = false
                onSendEmailSuccessNav(value)
                forgotPasswordViewModel.resetEmail()
                forgotPasswordViewModel.resetState()
            }
            else -> showLoadingDialog = false
        }
    }
    if (showLoadingDialog) {
        LoadingDialog(text = "Please wait...")
    }

    if (showErrorDialog) {
        ErrorDialog { showErrorDialog = false }
    }
}
@Preview
@Composable
fun ForgotPasswordAScreenPreview()
{
    ForgotPasswordScreen (
        onBackNav = {},
        onSendEmailSuccessNav = {}
    )
}