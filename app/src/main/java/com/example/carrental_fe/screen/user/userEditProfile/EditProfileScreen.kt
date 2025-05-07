package com.example.carrental_fe.screen.user.userEditProfile

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.carrental_fe.R
import com.example.carrental_fe.screen.component.BackButton
import com.example.carrental_fe.screen.component.CustomButton
import com.example.carrental_fe.screen.component.InputField
import com.example.carrental_fe.screen.component.InputLabel
import com.example.carrental_fe.screen.user.userHomeScreen.TopTitle
import java.io.File

@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel = viewModel(factory = EditProfileViewModel.Factory),
    onBackNav: () -> Unit) {
    val displayName by viewModel.displayName.collectAsState()
    val email by viewModel.email.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val address by viewModel.address.collectAsState()
    val gender by viewModel.gender.collectAsState()
    val avatarFile by viewModel.avatarFile.collectAsState()
    val avatarUrl by viewModel.avatarUrl.collectAsState()
    val context = LocalContext.current
    // Chọn ảnh
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val file = uriToFile(uri, context)
            viewModel.onAvatarSelected(file)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.setInitialData()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                BackButton(
                    onClick = { onBackNav() },
                    iconResId = R.drawable.back_icon,
                    modifier = Modifier.align(Alignment.CenterStart)
                )

                TopTitle(
                    title = "Profile",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Avatar
        if (avatarFile != null) {
            // Người dùng đã chọn ảnh mới
            Image(
                painter = rememberAsyncImagePainter(avatarFile),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.White, CircleShape)
                    .align(Alignment.CenterHorizontally)
                    .clickable { imageLauncher.launch("image/*") },
                contentScale = ContentScale.Crop
            )
        } else if (!avatarUrl.isNullOrEmpty()) {
            // Dùng ảnh từ URL
            AsyncImage(
                model = avatarUrl,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.White, CircleShape)
                    .align(Alignment.CenterHorizontally)
                    .clickable { imageLauncher.launch("image/*") },
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.male_avatar_svgrepo_com),
                error = painterResource(id = R.drawable.male_avatar_svgrepo_com)
            )
        } else {
            // Ảnh mặc định
            Image(
                painter = painterResource(id = R.drawable.male_avatar_svgrepo_com),
                contentDescription = "Default Profile Image",
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.White, CircleShape)
                    .align(Alignment.CenterHorizontally)
                    .clickable { imageLauncher.launch("image/*") },
                contentScale = ContentScale.Crop
            )
        }

        // Tên
        Spacer(modifier = Modifier.height(30.dp))
        InputLabel("Your Name", R.drawable.user)
        InputField(
            placeHolder = "Complete your name",
            value = displayName,
            onValueChange = viewModel::onDisplayNameChanged,
            editable = true
        )

        // Email
        Spacer(modifier = Modifier.height(16.dp))
        InputLabel("Email Address", R.drawable.email)
        InputField(
            placeHolder = "Complete your email address",
            value = email,
            onValueChange = viewModel::onEmailChanged,
            editable = true
        )

        // Địa chỉ
        Spacer(modifier = Modifier.height(16.dp))
        InputLabel("Address", R.drawable.house_24px)
        InputField(
            placeHolder = "Complete your address",
            value = address,
            onValueChange = viewModel::onAddressChanged,
            editable = true
        )

        // Số điện thoại
        Spacer(modifier = Modifier.height(16.dp))
        InputLabel("Phone Number", R.drawable.phone_number)
        InputField(
            placeHolder = "Complete your phone number",
            value = phoneNumber,
            onValueChange = viewModel::onPhoneNumberChanged,
            editable = true
        )

        Spacer(modifier = Modifier.height(16.dp))
        InputLabel("Gender", drawable = R.drawable.email)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = gender == 1,
                    onClick = { viewModel.onGenderChanged(1) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF0D6EFD),
                        unselectedColor = Color.Black
                    )
                )
                Text("Male")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = gender == 2,
                    onClick = { viewModel.onGenderChanged(2) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF0D6EFD),
                        unselectedColor = Color.Black
                    )
                )
                Text("Female")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CustomButton(
            backgroundColor = Color(0xFF0D6EFD),
            text = "Save",
            textColor = 0xFFFFFFFF,
            onClickChange = {
                viewModel.saveProfile(onBackNav)
            }
        )
    }
}

fun uriToFile(uri: Uri, context: Context): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val tempFile = File.createTempFile("avatar", ".jpg", context.cacheDir)
    tempFile.outputStream().use { outputStream ->
        inputStream?.copyTo(outputStream)
    }
    return tempFile
}