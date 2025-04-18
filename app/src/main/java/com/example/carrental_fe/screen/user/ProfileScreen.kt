package com.example.carrental_fe.screen.user

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carrental_fe.R
import com.example.carrental_fe.screen.component.CustomButton
import com.example.carrental_fe.screen.component.InputField
import com.example.carrental_fe.screen.component.InputLabel

@Composable
fun ProfileScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Tiêu đề
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(), // Thêm padding nếu muốn
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
                        .clickable { }
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Avatar
        Image(
            painter = painterResource(id = R.drawable.male_avatar_svgrepo_com),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .border(1.dp, Color.White, CircleShape)
                .align(Alignment.CenterHorizontally)
        )

        // Tên
        Spacer(modifier = Modifier.height(30.dp))
        InputLabel("Your Name", R.drawable.user)
        InputField("Sherlock Holmes", "hihi", {})


        // Email
        Spacer(modifier = Modifier.height(16.dp))
        InputLabel("Email Address", R.drawable.email)
        InputField("sherlockholmes@gmail.com", "hihi", {})

        // Địa chỉ
        Spacer(modifier = Modifier.height(16.dp))
        InputLabel("Address", R.drawable.house_24px)
        InputField("221B Baker Street", "hihi", {})

        // Số điện thoại
        Spacer(modifier = Modifier.height(16.dp))
        InputLabel("Phone Number", R.drawable.phone_number)
        InputField("0908070605", "hihi", {})


        // Change Password
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Change Password",
            fontSize = 14.sp,
            color = Color.Blue,
            modifier = Modifier
                .align(Alignment.End)
                .clickable {
                    Toast.makeText(context, "Change Password clicked", Toast.LENGTH_SHORT).show()
                }
        )

        // Nút Log Out
        Spacer(modifier = Modifier.height(24.dp))
        CustomButton(
            backgroundColor = Color(0xFF0D6EFD),
            text = "Log out",
            textColor = 0xFFFFFFFF,
            onClickChange = {}
        )
    }
}

@Preview
@Composable
fun verjvethg() {
    ProfileScreen();
}