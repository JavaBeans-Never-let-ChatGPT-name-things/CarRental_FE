package com.example.carrental_fe.screen.verify

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.carrental_fe.screen.component.BackButton
import com.example.carrental_fe.screen.component.CustomButton
import com.example.carrental_fe.screen.component.InputField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun VerifyAccountScreen(
    onBackClick:() -> Unit,
    verifyAccountViewModel : VerifyAccountViewModel,
    email : String
)
{
    LaunchedEffect(Unit) {
        verifyAccountViewModel.email = email
    }
    val verificationCode by verifyAccountViewModel.verificationCode.collectAsState()
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

            // Nút Back
            BackButton(onClick = onBackClick, iconResId = R.drawable.back_icon)

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Verify Your Account",
                fontSize = 28.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Enter the code you receive in email to \nverify your account",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                color = Color(0xFF707B81),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(50.dp))
            InputField(placeHolder = "Verification Code",
                onValueChange = {verifyAccountViewModel.onVerificationCodeChange(it)},
                value = verificationCode)
            Text(
                text = "Resend Email",
                fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                fontSize = 14.sp,
                color = Color(0xFF707B81),
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 12.dp, bottom = 30.dp)
                    .clickable {verifyAccountViewModel.resendVerificationCode()}
            )
            CustomButton(
                onClickChange = {verifyAccountViewModel.verifyAccount()}, text = "Verify", textColor = 0xFFFFFFFF,
                backgroundColor = Color(0xFF0D6EFD),
                imageResId = null
            )
        }
    }
}

@Composable
@Preview
fun VerifyAccountScreenPreview()
{
    VerifyAccountScreen(
        onBackClick = {},
        verifyAccountViewModel = viewModel(factory = VerifyAccountViewModel.Factory),
        email = ""
    )
}