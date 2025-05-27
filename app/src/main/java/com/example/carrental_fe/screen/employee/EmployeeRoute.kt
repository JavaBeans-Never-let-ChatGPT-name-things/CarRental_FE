package com.example.carrental_fe.screen.employee

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.carrental_fe.nav.EmployeeRoute
import com.example.carrental_fe.nav.EmployeeScreenNavGraph
import com.example.carrental_fe.nav.defaultEmployeeRoute

@Composable
fun EmployeeRoute(
    onNavigateToLogin: () -> Unit,
    onSendEmailSuccessNav: (String) -> Unit,
    onNavigateToEditProfile: () -> Unit,
) {
    var currentRoute by rememberSaveable { mutableStateOf(
        defaultEmployeeRoute)}
    Scaffold(
        containerColor = Color(0xFFFFFFFF),
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFF7F7F9)
            ) {
                for (route in EmployeeRoute.entries) {
                    val isSelected = route == currentRoute
                    val iconRes = if (isSelected) route.selectedIcon
                    else route.unselectedIcon
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (currentRoute != route)
                                currentRoute = route
                        },
                        label = { Text(stringResource(route.labelResId)) },
                        icon = {
                            Icon(iconRes, null)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color(0xFFAED4FD),
                            selectedIconColor = Color(0xFF0D6EFD),
                            unselectedIconColor = Color.Black,
                            selectedTextColor = Color(0xFF0D6EFD),
                        )
                    )
                }
            }
        }
    ) {
            innerPadding->
        EmployeeScreenNavGraph(
            currentRoute = currentRoute,
            onNavigateToUserProfile = onNavigateToEditProfile,
            onNavigateToLogin = onNavigateToLogin,
            onSendEmailSuccessNav = onSendEmailSuccessNav,
            modifier = Modifier.padding(innerPadding)
        )
    }
}