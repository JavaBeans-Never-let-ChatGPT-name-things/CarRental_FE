package com.example.carrental_fe.screen.admin

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
import com.example.carrental_fe.nav.AdminRoute
import com.example.carrental_fe.nav.AdminScreenNavGraph
import com.example.carrental_fe.nav.defaultAdminRoute

@Composable
fun AdminRoute(
    onNavigateToUserDetail: (String) -> Unit,
    onNavigateToCarDetail: (String) -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onSendEmailSuccessNav: (String) -> Unit,
    onNavigateToLogin: () -> Unit,
){
    var currentRoute by rememberSaveable { mutableStateOf(
        defaultAdminRoute
    )}
    Scaffold(
        containerColor = Color(0xFFFFFFFF),
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFF7F7F9)
            ) {
                for (route in AdminRoute.entries) {
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
        AdminScreenNavGraph(
            currentRoute = currentRoute,
            onNavigateToUserDetail = onNavigateToUserDetail,
            onNavigateToCarDetail = onNavigateToCarDetail,
            onNavigateToEditProfile = onNavigateToEditProfile,
            onNavigateToLogin = onNavigateToLogin,
            onSendEmailSuccessNav = onSendEmailSuccessNav,
            modifier = Modifier.padding(innerPadding)
        )
    }
}