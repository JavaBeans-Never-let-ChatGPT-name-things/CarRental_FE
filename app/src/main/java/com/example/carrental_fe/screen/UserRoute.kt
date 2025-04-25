package com.example.carrental_fe.screen

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
import com.example.carrental_fe.nav.MainRoutes
import com.example.carrental_fe.nav.UserScreenNavGraph
import com.example.carrental_fe.nav.defaultRoute

@Composable
fun UserRoute(
    onNavigateToSearchScreen: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToCarDetail: (String) -> Unit,
)
{
    var currentRoute by rememberSaveable { mutableStateOf(defaultRoute) }
    Scaffold (
        containerColor = Color(0xFFFFFFFF),
        bottomBar = { NavigationBar(
            containerColor = Color(0xFFF7F7F9)
        ){
            for (route in MainRoutes.entries) {
                val isSelected = route == currentRoute
                val iconRes = if (isSelected) route.selectedIcon
                else route.unselectedIcon
                val icon = @Composable {
                    Icon(iconRes, null)
                }
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (currentRoute != route)
                            currentRoute = route
                    },
                    label = { Text(stringResource(route.labelResId)) },
                    icon = {
                        icon()
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color(0xFFAED4FD),
                        selectedIconColor = Color(0xFF0D6EFD),
                        unselectedIconColor = Color.Black,
                        selectedTextColor = Color(0xFF0D6EFD),
                    )
                )
            }
        } }
    ) {
            innerPadding ->
        UserScreenNavGraph(
            onNavigateToSearchScreen = onNavigateToSearchScreen,
            onNavigateToEditProfile = onNavigateToEditProfile,
            onNavigateToCarDetail = onNavigateToCarDetail,
            currentRoute = currentRoute,
            modifier = Modifier.padding(innerPadding)
        )
    }
}