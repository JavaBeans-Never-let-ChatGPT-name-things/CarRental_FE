package com.example.carrental_fe.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.carrental_fe.R
import com.example.carrental_fe.screen.component.CustomButton
import com.example.carrental_fe.screen.component.InputField
import com.example.carrental_fe.screen.component.InputLabel
import com.example.carrental_fe.screen.component.PasswordField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.compose

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onSignUpClick: () -> Unit,
    onRecoveryClick: () -> Unit)
{
    LaunchedEffect(Unit) {
        loginViewModel.resetFields()
    }
    val username by loginViewModel.username.collectAsState()
    val password by loginViewModel.password.collectAsState()
    val isPasswordVisible by loginViewModel.isPasswordVisible.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(106.dp))

            // Tiêu đề
            Text(
                text = "Hello!",
                fontSize = 32.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = "Fill Your Details Or Continue With\nSocial Media",
                fontFamily = FontFamily(Font(R.font.montserrat_regular )),
                fontSize = 16.sp,
                color = Color(0xFF707B81),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(70.dp))

            InputLabel(text = "Username", drawable = R.drawable.user)
            Spacer(modifier = Modifier.height(12.dp))
            InputField(value = username, placeHolder = "Username", onValueChange = {loginViewModel.onUsernameChange(it)})

            Spacer(modifier = Modifier.height(30.dp))

            InputLabel(text = "Password", R.drawable.password)
            Spacer(modifier = Modifier.height(12.dp))
            PasswordField(value = password,
                onValueChange = { loginViewModel.onPasswordChange(it)},
                togglePassWordVisibility = {loginViewModel.togglePasswordVisibility()},
                isPasswordVisible = isPasswordVisible)

            Text(
                text = "Recovery Password",
                fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 12.dp, bottom = 30.dp)
                    .clickable{ onRecoveryClick()}
            )

            CustomButton(
                backgroundColor = Color(0xFF0D6EFD),
                text = "Sign In",
                textColor = 0xFFFFFFFF,
                onClickChange = {loginViewModel.login()}
            )

            Spacer(modifier = Modifier.height(24.dp))

            CustomButton(
                backgroundColor = Color(0xFFF5F5F5),
                text = "Sign In With Google",
                imageResId = R.drawable.google,
                textColor = 0xFF2B2B2B
            )

            Spacer(modifier = Modifier.height(125.dp))

            Text(
                text = "New User? Create Account",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSignUpClick() }
            )
        }
    }
}

@Composable
@Preview
fun LoginScreenPreview(){
    LoginScreen(
        loginViewModel = viewModel(factory = LoginViewModel.Factory),
        onSignUpClick = {},
        onRecoveryClick = {}
    )
}