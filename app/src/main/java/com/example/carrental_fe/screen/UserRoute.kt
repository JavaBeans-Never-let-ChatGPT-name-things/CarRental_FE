package com.example.carrental_fe.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carrental_fe.model.Account
import com.example.carrental_fe.nav.MainRoutes
import com.example.carrental_fe.nav.UserScreenNavGraph
import com.example.carrental_fe.nav.defaultRoute
import com.example.carrental_fe.screen.userHomeScreen.UserHomeScreenViewModel
import com.example.carrental_fe.screen.userNotificationScreen.NotificationViewModel

@Composable
fun UserRoute(
    onNavigateToSearchScreen: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToCarDetail: (String) -> Unit,
    onCheckoutNav: (String, String, Long?) -> Unit,
    )
{
    val notification: NotificationViewModel = viewModel(factory = NotificationViewModel.Factory)
    var currentRoute by rememberSaveable { mutableStateOf(defaultRoute) }
    val notificationBadgeCount by notification.unreadNotificationCount.collectAsState()
    var lastRoute by remember { mutableStateOf(currentRoute) }

    LaunchedEffect(currentRoute) {
        if (lastRoute == MainRoutes.NOTIFICATIONS && currentRoute != MainRoutes.NOTIFICATIONS) {
            notification.markAllAsRead()
        }
        lastRoute = currentRoute
    }
    Scaffold (
        containerColor = Color(0xFFFFFFFF),
        bottomBar = { NavigationBar(
            containerColor = Color(0xFFF7F7F9)
        ){
            for (route in MainRoutes.entries) {
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
                       BadgedBox(
                           badge = {
                               if (route == MainRoutes.NOTIFICATIONS && notificationBadgeCount > 0){
                                   Badge { Text(notificationBadgeCount.toString())}
                               }
                           }
                       ) {
                           Icon(iconRes,null)
                       }
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
            vm = notification,
            onNavigateToSearchScreen = onNavigateToSearchScreen,
            onNavigateToEditProfile = onNavigateToEditProfile,
            onNavigateToCarDetail = onNavigateToCarDetail,
            onCheckoutNav = onCheckoutNav,
            currentRoute = currentRoute,
            modifier = Modifier.padding(innerPadding)
        )
    }
}