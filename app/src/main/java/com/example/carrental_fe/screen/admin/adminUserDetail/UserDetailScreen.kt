package com.example.carrental_fe.screen.admin.adminUserDetail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.carrental_fe.R
import com.example.carrental_fe.screen.component.BackButton
import com.example.carrental_fe.screen.component.InputField
import com.example.carrental_fe.screen.component.InputLabel
import com.example.carrental_fe.screen.user.userContractScreen.ContractCard
import com.example.carrental_fe.screen.user.userHomeScreen.TopTitle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserDetailScreen(
    onBackNav: () -> Unit,
    viewModel: UserDetailViewModel = viewModel(factory = UserDetailViewModel.Factory)
) {
    val scrollState = rememberScrollState()
    val user by viewModel.user.collectAsState()
    user?.let { userDetail ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(Color.White)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(
                    onClick = { onBackNav() },
                    iconResId = R.drawable.back_icon,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                TopTitle("User Details", modifier = Modifier.align(Alignment.CenterVertically))
            }

            Spacer(modifier = Modifier.height(16.dp))

            AsyncImage(
                model = userDetail.avatarUrl,
                contentDescription = "Profile Image",
                placeholder = painterResource(id = if (userDetail.gender == 1) R.drawable.male else R.drawable.female),
                error = painterResource(id = if (userDetail.gender == 1) R.drawable.male else R.drawable.female),
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(30.dp))

            InputLabel("Username", R.drawable.user)
            InputField(userDetail.username, "", {}, editable = false)

            Spacer(modifier = Modifier.height(16.dp))
            InputLabel("Display Name", R.drawable.user)
            InputField(userDetail.displayName, "", {}, editable = false)

            Spacer(modifier = Modifier.height(16.dp))
            InputLabel("Email Address", R.drawable.email)
            InputField(userDetail.email, "", {}, editable = false)

            Spacer(modifier = Modifier.height(16.dp))
            InputLabel("Address", R.drawable.house_24px)
            InputField(userDetail.address?:"", "", editable = false)

            Spacer(modifier = Modifier.height(16.dp))
            InputLabel("Phone Number", R.drawable.phone_number)
            InputField(userDetail.phoneNumber ?: "", "", editable = false)

            Spacer(modifier = Modifier.height(16.dp))
            InputLabel("Total Penalty Fee", R.drawable.fee_icon, color = Color.Red)
            InputField(
                "${userDetail.totalPenalty}",
                "",
                editable = false,
                inputType = "number"
            )

            Spacer(modifier = Modifier.height(16.dp))
            InputLabel("Gender", R.drawable.gender)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                RadioButton(
                    selected = userDetail.gender == 0,
                    onClick = {},
                    enabled = false,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF0D6EFD),
                        unselectedColor = Color.Gray
                    )
                )
                Text("Male")

                RadioButton(
                    selected = userDetail.gender == 1,
                    onClick = {},
                    enabled = false,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF0D6EFD),
                        unselectedColor = Color.Gray
                    )
                )
                Text("Female")
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Rental Contracts", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalPager(
                state = rememberPagerState(pageCount = { userDetail.rentalContracts.size }),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            ) { index ->
                val contract = userDetail.rentalContracts[index]
                ContractCard(
                    contract = contract,
                    role = ""
                )
            }
        }
    }
}