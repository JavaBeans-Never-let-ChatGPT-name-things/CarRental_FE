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
import com.example.carrental_fe.screen.user.UserHomeScreenViewModel

@Composable
fun UserRoute(
    onNavigateToSearchScreen: () -> Unit,
    viewModel: UserHomeScreenViewModel
)
{
    var currentRoute by rememberSaveable { mutableStateOf(defaultRoute) }
    Scaffold (
        bottomBar = { NavigationBar {
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
            viewModel = viewModel,
            currentRoute = currentRoute,
            modifier = Modifier.padding(innerPadding)
        )
    }
}