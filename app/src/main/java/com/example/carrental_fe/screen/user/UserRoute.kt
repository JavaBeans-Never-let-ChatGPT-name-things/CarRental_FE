package com.example.carrental_fe.screen.user

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.navigation.NavHostController
import com.example.carrental_fe.dialog.ErrorDialog
import com.example.carrental_fe.dialog.SuccessDialog
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carrental_fe.nav.MainRoutes
import com.example.carrental_fe.nav.User
import com.example.carrental_fe.nav.UserScreenNavGraph
import com.example.carrental_fe.nav.defaultRoute
import com.example.carrental_fe.screen.user.userNotificationScreen.NotificationViewModel


@SuppressLint("UnrememberedGetBackStackEntry")

@Composable
fun UserRoute(
    navController: NavHostController,
    onNavigateToLogin: () -> Unit,
    onNavigateToSearchScreen: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToCarDetail: (String) -> Unit,
    onSendEmailSuccessNav: (String) -> Unit,
    onCheckoutNav: (String, String, Long?) -> Unit,
    )
{
    val notification: NotificationViewModel = viewModel(factory = NotificationViewModel.Factory)
    var currentRoute by rememberSaveable { mutableStateOf(defaultRoute) }
    val notificationBadgeCount by notification.unreadNotificationCount.collectAsState()
    var lastRoute by remember { mutableStateOf(currentRoute) }

    val navEntry = navController.getBackStackEntry(User)
    var isShowSuccessDialog by remember { mutableStateOf(false) }
    var isShowErrorDialog by remember { mutableStateOf(false) }
    val retryStatus: String? by navEntry
        .savedStateHandle
        .getLiveData<String>("retryPaymentStatus")
        .observeAsState()
    LaunchedEffect(retryStatus) {
        if (retryStatus != null) {
            if (retryStatus == "success"){
                isShowSuccessDialog = true
            }
            else
            {
                isShowErrorDialog = true
            }
        }
    }
    if (isShowSuccessDialog)
    {
        SuccessDialog("Successfully Retry Contract", onDismiss = {
            isShowSuccessDialog = false
            navEntry.savedStateHandle["retryPaymentStatus"] = null
        })
    }
    if (isShowErrorDialog)
    {
        ErrorDialog("Failed to Retry Contract", onDismiss = {
            isShowErrorDialog = false
            navEntry.savedStateHandle["retryPaymentStatus"] = null
        })
    }

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
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToEditProfile = onNavigateToEditProfile,
            onNavigateToCarDetail = onNavigateToCarDetail,
            onSendEmailSuccessNav = onSendEmailSuccessNav,
            onCheckoutNav = onCheckoutNav,
            currentRoute = currentRoute,
            modifier = Modifier.padding(innerPadding)
        )
    }
}