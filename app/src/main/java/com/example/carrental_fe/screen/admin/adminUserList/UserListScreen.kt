package com.example.carrental_fe.screen.admin.adminUserList

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.carrental_fe.R
import com.example.carrental_fe.dialog.ConfirmReportLostDialog
import com.example.carrental_fe.dto.response.UserDTO
import com.example.carrental_fe.model.enums.AccountRole
import com.example.carrental_fe.screen.user.userHomeScreen.TopTitle

@Composable
fun UserListScreen(
    onNavigateToUserDetail: (String) -> Unit,
    viewModel: UserListViewModel = viewModel(factory = UserListViewModel .Factory)) {
    val users by viewModel.users.collectAsState()
    val query by viewModel.query.collectAsState()
    val selectedSort by viewModel.selectedSort.collectAsState()
    val selectedStatus by viewModel.selectedStatus.collectAsState()
    val sortOptions = listOf("Select an option", "Display Name", "Contract Count")
    val statOptions = listOf("Total User", "Active User", "Inactive User")
    val stats by viewModel.initialStats.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val listState = rememberLazyListState()

    var showPromoteDialog by remember { mutableStateOf<Pair<Boolean, String>?>(null) }
    var showDemoteDialog by remember { mutableStateOf<Pair<Boolean, String>?>(null) }

    LaunchedEffect(Unit) {
        listState.scrollToItem(0)
    }
    LaunchedEffect(isLoading) {
        if (isLoading) {
            listState.scrollToItem(0)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        TopTitle("Users")
        Spacer(Modifier.height(24.dp))
        Text("User List", fontWeight = FontWeight.SemiBold)

        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            statOptions.forEach { status ->
                StatCard(
                    title = status,
                    count = when (status) {
                        "Active User" -> stats?.second?: 0
                        "Inactive User" -> stats?.third?: 0
                        else -> stats?.first?: 0
                    },
                    color = when (status) {
                        "Active User" -> Color(0xFF32A834)
                        "Inactive User" -> Color(0xFFF40707)
                        else -> Color(0xFF0D6EFD)
                    },
                    isSelected = selectedStatus == status,
                    onClick = { viewModel.updateStatus(status) }
                )
            }
        }

        Spacer(Modifier.height(24.dp))
        OutlinedTextField(
            value = query,
            onValueChange = {
                viewModel.updateQuery(it)
            },
            placeholder = { Text("Search by Display Name", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF7F7F9),
                focusedContainerColor = Color(0xFFF7F7F9),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Sort Option")
            Spacer(Modifier.width(12.dp))
            var expanded by remember { mutableStateOf(false) }
            Box {
                Text(
                    text = selectedSort,
                    modifier = Modifier
                        .clickable { expanded = true }
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    sortOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                viewModel.updateSort(option)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Box(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading)
            {
                CircularProgressIndicator(color = Color(0xFF0D6EFD))
            }
            else{
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.Top) {
                    items(users) { user ->
                        UserCard(user, onClick = {
                            onNavigateToUserDetail(user.displayName)
                        },
                            onPromoteClick = {
                                showPromoteDialog = Pair(true, user.displayName)
                            },
                            onDemoteClick = {
                                showDemoteDialog = Pair(true, user.displayName)
                            }
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
    }
    showPromoteDialog?.let { (isShowing, userName) ->
        if (isShowing) {
            ConfirmReportLostDialog(
                title = "Confirm Promotion",
                message = "Are you sure you want to promote $userName to EMPLOYEE?",
                onConfirm = {
                    viewModel.promoteUser(userName)
                    showPromoteDialog = null
                },
                onDismiss = { showPromoteDialog = null }
            )
        }
    }

    showDemoteDialog?.let { (isShowing, userName) ->
        if (isShowing) {
            ConfirmReportLostDialog(
                title = "Confirm Demotion",
                message = "Are you sure you want to demote $userName to USER?",
                onConfirm = {
                    viewModel.demoteUser(userName)
                    showDemoteDialog = null
                },
                onDismiss = { showDemoteDialog = null }
            )
        }
    }
}

@Composable
fun UserCard(
    user: UserDTO,
    onClick: () -> Unit,
    onPromoteClick: () -> Unit = {},
    onDemoteClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F9))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    model = user.avatarUrl,
                    contentDescription = "Profile Image",
                    placeholder = painterResource(id = if (user.gender == 1) R.drawable.male else R.drawable.female),
                    error = painterResource(id = if (user.gender == 1) R.drawable.male else R.drawable.female),
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = user.role.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                InfoRow(icon = Icons.Default.Person, label = "Display Name:", value = user.displayName)
                Spacer(Modifier.height(8.dp))
                InfoRow(icon = Icons.Default.Phone, label = "Phone Number:", value = user.phoneNumber ?: "")
                Spacer(Modifier.height(8.dp))
                InfoRow(icon = Icons.Default.Email, label = "Email:", value = user.email)
                Spacer(Modifier.height(8.dp))
                InfoRow(icon = Icons.Default.Receipt, label = if (user.role == AccountRole.USER) "Contracts Count:" else "Contracts Incharge:", value = "${user.countractCount}")
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Status",
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Icon(
                    painter = painterResource(
                        id = if (user.enabled) R.drawable.ic_check_circle else R.drawable.ic_block
                    ),
                    contentDescription = null,
                    tint = if (user.enabled) Color(0xFF32A834) else Color.Red,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(Modifier.height(8.dp))

                if (user.role == AccountRole.USER) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Promote",
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )
                        IconButton(onClick = onPromoteClick) {
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = "Promote",
                                tint = Color(0xFF32A834),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                } else if (user.role == AccountRole.EMPLOYEE) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Demote",
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )
                        IconButton(onClick = onDemoteClick) {
                            Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = "Demote",
                                tint = Color.Red,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(15.dp))
        Spacer(Modifier.width(10.dp))
        Column {
            Text(
                text = label,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = value,
                fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                fontSize = 12.sp
            )
        }
    }
}


@Composable
fun StatCard(title: String, count: Int, color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) color else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F9))
    ) {
        Column(
            Modifier
                .padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(Icons.Default.People, contentDescription = null, tint = color)
            Text(title, fontWeight = FontWeight.Bold)
            Text(count.toString(), fontWeight = FontWeight.ExtraBold)
        }
    }
}