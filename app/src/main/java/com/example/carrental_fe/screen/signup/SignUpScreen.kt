package com.example.carrental_fe.screen.signup

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.carrental_fe.dialog.ErrorDialog
import com.example.carrental_fe.dialog.LoadingDialog
import com.example.carrental_fe.nav.SignUp
import com.example.carrental_fe.nav.VerifyAccount
import com.example.carrental_fe.screen.component.*


@Composable
fun RegisterScreen(
    onBackNav: () -> Unit = {},
    onLoginNav: () -> Unit = {},
    signUpViewModel: SignUpViewModel = viewModel(factory = SignUpViewModel.Factory),
    onRegisterSuccessNav: (String) -> Unit
){
    val username by signUpViewModel.username.collectAsState()
    val displayName by signUpViewModel.displayName.collectAsState()
    val email by signUpViewModel.email.collectAsState()
    val password by signUpViewModel.password.collectAsState()
    val isPasswordVisible by signUpViewModel.isPasswordVisible.collectAsState()
    val signUpState by signUpViewModel.signUpState.collectAsState()

    var showErrorDialog by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }

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
            Spacer(modifier = Modifier.height(50.dp))

            BackButton(onClick = {onBackNav()}, iconResId = R.drawable.back_icon)

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Register Account",
                fontSize = 28.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(7.dp))



            Spacer(modifier = Modifier.height(40.dp))

            InputLabel(text = "Username", drawable = R.drawable.user)
            Spacer(modifier = Modifier.height(5.dp))
            InputField(placeHolder = "Username", onValueChange = {signUpViewModel.onUsernameChange(it)}, value = username)
            Spacer(modifier = Modifier.height(10.dp))

            InputLabel(text = "Your Name", drawable = R.drawable.displayname)
            Spacer(modifier = Modifier.height(5.dp))
            InputField(placeHolder = "Your Name", onValueChange = { signUpViewModel.onDisplayNameChange(it)}, value = displayName)
            Spacer(modifier = Modifier.height(10.dp))

            InputLabel(text = "Email Address", R.drawable.email)
            Spacer(modifier = Modifier.height(5.dp))
            InputField(placeHolder = "Email", onValueChange = { signUpViewModel.onEmailChange(it) }, value = email)
            Spacer(modifier = Modifier.height(10.dp))

            InputLabel(text = "Password", R.drawable.password)
            Spacer(modifier = Modifier.height(5.dp))
            PasswordField(
                onValueChange = { signUpViewModel.onPasswordChange(it) },
                togglePassWordVisibility = { signUpViewModel.togglePasswordVisibility() },
                value = password,
                isPasswordVisible = isPasswordVisible)

            Spacer(modifier = Modifier.height(60.dp))

            CustomButton(
                onClickChange = { signUpViewModel.register() },
                backgroundColor = Color(0xFF0D6EFD),
                text = "Sign Up",
                textColor = 0xFFFFFFFF
            )

            Spacer(modifier = Modifier.height(80.dp))

            Text(
                text = "Already Have Account? Log In",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLoginNav() }
            )
        }
    }
    LaunchedEffect(signUpState) {
        when (signUpState) {
            is SignUpState.Loading -> showLoadingDialog = true
            is SignUpState.Error -> {
                showLoadingDialog = false
                showErrorDialog = true
            }
            is SignUpState.Success -> {
                showLoadingDialog = false
                onRegisterSuccessNav(email)
                signUpViewModel.resetFields()
                signUpViewModel.resetState()
            }
            else -> showLoadingDialog = false
        }
    }
    if (showLoadingDialog) {
        LoadingDialog(text = "Please wait...")
    }

    if (showErrorDialog) {
        ErrorDialog (text = (signUpState as SignUpState.Error).message) { showErrorDialog = false }
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(
        onBackNav = {},
        onLoginNav = {},
        onRegisterSuccessNav = {}
    )
}