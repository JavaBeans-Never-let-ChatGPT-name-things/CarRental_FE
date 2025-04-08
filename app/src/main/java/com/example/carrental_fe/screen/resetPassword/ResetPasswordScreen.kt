package com.example.carrental_fe.screen.resetPassword

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carrental_fe.*
import com.example.carrental_fe.nav.Login
import com.example.carrental_fe.screen.component.*
@Composable
fun ResetPasswordScreen(
    resetPasswordViewModel: ResetPasswordViewModel = viewModel(factory = ResetPasswordViewModel.Factory),
    onResetSuccess: () -> Unit
)
{
    var showErrorDialog by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }

    val verificationCode by resetPasswordViewModel.verificationCode.collectAsState()
    val password by resetPasswordViewModel.password.collectAsState()
    val isPasswordVisible by resetPasswordViewModel.isPasswordVisible.collectAsState()
    val resetPasswordState by resetPasswordViewModel.resetPasswordState.collectAsState()

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

            BackButton(onClick = {}, iconResId = R.drawable.back_icon)

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Reset Password",
                fontSize = 28.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Please enter your Verification Code \n and New Password ",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                color = Color(0xFF707B81),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Verification Code",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(10.dp))
            InputField(placeHolder = "xxxxxx",
                onValueChange = { resetPasswordViewModel.onVerificationCodeChange(it) },
                value = verificationCode)
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Password",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(10.dp))
            PasswordField(togglePassWordVisibility = { resetPasswordViewModel.togglePasswordVisibility()},
                onValueChange = { resetPasswordViewModel.onPasswordChange(it) },
                value = password,
                isPasswordVisible = isPasswordVisible)
            Spacer(modifier = Modifier.height(50.dp))
            CustomButton(
                onClickChange = { resetPasswordViewModel.resetPassword() }, text = "Confirm", textColor = 0xFFFFFFFF,
                backgroundColor = Color(0xFF0D6EFD),
                imageResId = null
            )
        }
    }
    LaunchedEffect(resetPasswordState) {
        when (resetPasswordState) {
            is ResetPasswordState.Loading -> showLoadingDialog = true
            is ResetPasswordState.Error -> {
                showLoadingDialog = false
                showErrorDialog = true
            }
            is ResetPasswordState.Success -> {
                showLoadingDialog = false
                onResetSuccess()
            }
            else -> showLoadingDialog = false
        }
    }
}

@Preview
@Composable
fun PreviewResetPasswordScreen()
{
    ResetPasswordScreen(
        onResetSuccess = {}
    )
}