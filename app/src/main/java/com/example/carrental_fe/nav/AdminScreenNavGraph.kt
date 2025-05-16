package com.example.carrental_fe.nav

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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.carrental_fe.R
import com.example.carrental_fe.screen.admin.adminContractList.PendingContractScreen
import com.example.carrental_fe.screen.admin.adminUserList.UserListScreen

internal val defaultAdminRoute = AdminRoute.CONTRACTS

internal enum class AdminRoute(
    @StringRes val labelResId: Int,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
) {
    CONTRACTS(
        R.string.contract_screen_label,
        Icons.AutoMirrored.Outlined.List,
        Icons.AutoMirrored.Filled.List
    ),
    HOME(
        R.string.userManagement_screen_label,
        Icons.Outlined.People,
        Icons.Filled.People
    ),
    ANALYTICS(
        R.string.analytics,
        Icons.Outlined.Assessment,
        Icons.Filled.Assessment
    ),
    PROFILE(
        R.string.profile_screen_label,
        Icons.Outlined.Person,
        Icons.Filled.Person
    )
}
@Composable
internal fun AdminScreenNavGraph (
    currentRoute: AdminRoute,
    onNavigateToUserDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
){
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
            AdminRoute.CONTRACTS -> {
                PendingContractScreen()
            }
            AdminRoute.HOME -> {
                UserListScreen(
                    onNavigateToUserDetail = onNavigateToUserDetail
                )
            }

            AdminRoute.ANALYTICS -> {

            }
            AdminRoute.PROFILE -> {

            }
        }
    }
}