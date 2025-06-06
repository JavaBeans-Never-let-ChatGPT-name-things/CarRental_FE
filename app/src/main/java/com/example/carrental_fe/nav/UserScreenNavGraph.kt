package com.example.carrental_fe.nav

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.carrental_fe.R
import com.example.carrental_fe.screen.user.userContractScreen.ContractScreen
import com.example.carrental_fe.screen.user.userFavScreen.FavouriteScreen
import com.example.carrental_fe.screen.user.userHomeScreen.HomeScreen
import com.example.carrental_fe.screen.user.userProfile.ProfileScreen
import com.example.carrental_fe.screen.user.userNotificationScreen.NotificationScreen
import com.example.carrental_fe.screen.user.userNotificationScreen.NotificationViewModel

internal val defaultRoute = MainRoutes.HOME

internal enum class MainRoutes(
    @StringRes val labelResId: Int,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
) {
    HOME(
        R.string.home_screen_label,
        Icons.Outlined.Home,
        Icons.Filled.Home
    ),
    FAV_CAR(
        R.string.favcar_screen_label,
        Icons.Outlined.FavoriteBorder,
        Icons.Filled.Favorite
    ),
    CONTRACTS(
        R.string.contract_screen_label,
        Icons.AutoMirrored.Outlined.List,
        Icons.AutoMirrored.Filled.List
    ),
    NOTIFICATIONS(
        R.string.notification_screen_label,
        Icons.Outlined.Notifications,
        Icons.Filled.Notifications
    ),
    PROFILE(
        R.string.profile_screen_label,
        Icons.Outlined.Person,
        Icons.Filled.Person
    )
}
@Composable
internal fun UserScreenNavGraph (
    vm: NotificationViewModel,
    onNavigateToSearchScreen: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToCarDetail: (carId: String) -> Unit,
    onCheckoutNav: (String, String, Long?) -> Unit,
    onSendEmailSuccessNav: (String) -> Unit,
    currentRoute: MainRoutes,
    modifier: Modifier = Modifier
){
    BackHandler {
        onNavigateToLogin()
    }
    val inTransition = fadeIn(tween(durationMillis = 250)) + slideInVertically { it / 50 }
    val outTransition = fadeOut(tween(durationMillis = 250))
    AnimatedContent(
        targetState = currentRoute,
        modifier = modifier,
        transitionSpec = {
            inTransition togetherWith outTransition using SizeTransform()
        },
        label = "Main screen swap page"
    ){
        when (it){
            MainRoutes.HOME -> {
                HomeScreen(
                    onNavigateToSearchScreen = onNavigateToSearchScreen,
                    onNavigateToCarDetail = onNavigateToCarDetail
                )
            }
            MainRoutes.FAV_CAR -> {
                FavouriteScreen(
                    onNavigateToCarDetail = onNavigateToCarDetail
                )
            }

            MainRoutes.CONTRACTS -> {
                ContractScreen(
                    onCheckoutNav = onCheckoutNav,
                )
            }
            MainRoutes.NOTIFICATIONS -> {
                NotificationScreen(
                    vm = vm
                )
            }
            MainRoutes.PROFILE -> {
                ProfileScreen(onNavigateToEditProfile = onNavigateToEditProfile,
                    onNavigateToLogin = onNavigateToLogin,
                    onSendEmailSuccessNav = onSendEmailSuccessNav)
            }
        }
    }
}