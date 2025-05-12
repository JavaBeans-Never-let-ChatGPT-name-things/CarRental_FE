package com.example.carrental_fe.screen.user.userProfile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.carrental_fe.R
import com.example.carrental_fe.dialog.LoadingDialog
import com.example.carrental_fe.screen.component.CustomButton
import com.example.carrental_fe.screen.component.InputField
import com.example.carrental_fe.screen.component.InputLabel
import com.example.carrental_fe.screen.user.userHomeScreen.TopTitle

@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel = viewModel(factory = ProfileScreenViewModel.Factory),
    onNavigateToLogin:() -> Unit,
    onNavigateToEditProfile: () -> Unit) {
    val context = LocalContext.current
    val accountInfo = viewModel.accountInfo.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    if (isLoading) {
        LoadingDialog(text = "Logging out ....")
    }
    LaunchedEffect(Unit) {
        viewModel.getAccountInfo()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {
                    TopTitle(title = "Profile")
                }

                Image(
                    painter = painterResource(R.drawable.edit),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .offset(x = (-13).dp)
                        .clickable { onNavigateToEditProfile() }
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Avatar
        if (accountInfo.value?.avatarUrl != null) {
            AsyncImage(
                model = accountInfo.value?.avatarUrl,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.White, CircleShape)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.male_avatar_svgrepo_com),
                error = painterResource(id = R.drawable.male_avatar_svgrepo_com)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.male_avatar_svgrepo_com),
                contentDescription = "Default Profile Image",
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.White, CircleShape)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop
            )
        }
        // Tên
        Spacer(modifier = Modifier.height(30.dp))
        InputLabel("Your Name", R.drawable.user)
        InputField(
            placeHolder = "Complete your name",
            value = accountInfo.value?.displayName ?: "",
            onValueChange = {},
            editable = false
        )


        // Email
        Spacer(modifier = Modifier.height(16.dp))
        InputLabel("Email Address", R.drawable.email)
        InputField(
            placeHolder = "Complete your email address",
            value = accountInfo.value?.email ?: "",
            onValueChange = {},
            editable = false)

        // Địa chỉ
        Spacer(modifier = Modifier.height(16.dp))
        InputLabel("Address", R.drawable.house_24px)
        InputField(
            placeHolder = "Complete your address",
            value = accountInfo.value?.address ?: "",
            onValueChange = {},
            editable = false)

        // Số điện thoại
        Spacer(modifier = Modifier.height(16.dp))
        InputLabel("Phone Number", R.drawable.phone_number)
        InputField(
            placeHolder = "Complete your phone number",
            value = accountInfo.value?.phoneNumber ?: "",
            onValueChange = {},
            editable = false)

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Change Password",
            fontSize = 14.sp,
            color = Color(0xFF0D6EFD),
            modifier = Modifier
                .align(Alignment.End)
                .clickable {
                    Toast.makeText(context, "Change Password clicked", Toast.LENGTH_SHORT)
                        .show()
                }
        )

        Spacer(modifier = Modifier.height(24.dp))
        CustomButton(
            backgroundColor = Color(0xFF0D6EFD),
            text = "Log out",
            textColor = 0xFFFFFFFF,
            onClickChange = {
                isLoading = true
                viewModel.logout {
                    isLoading = false
                    onNavigateToLogin()
                }
            }
        )
    }
}