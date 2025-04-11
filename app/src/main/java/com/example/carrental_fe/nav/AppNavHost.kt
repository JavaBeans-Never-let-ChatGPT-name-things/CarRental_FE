package com.example.carrental_fe.nav
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carrental_fe.dto.response.TokenResponse
import com.example.carrental_fe.screen.UserRoute
import com.example.carrental_fe.screen.forgot.ForgotPasswordScreen
import com.example.carrental_fe.screen.login.LoginScreen
import com.example.carrental_fe.screen.resetPassword.ResetPasswordScreen
import com.example.carrental_fe.screen.signup.RegisterScreen
import com.example.carrental_fe.screen.user.SearchScreen
import com.example.carrental_fe.screen.user.UserHomeScreenViewModel
import com.example.carrental_fe.screen.verify.VerifyAccountScreen
import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object SignUp

@Serializable
object ForgotPassword

@Serializable
data class VerifyAccount(val email: String? = null)

@Serializable
data class ResetPassword(val email: String? = null)

@Serializable
data object User

@Serializable
data object Admin

@Serializable
object Search

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    val userHomeScreenViewModel: UserHomeScreenViewModel
    = viewModel(factory = UserHomeScreenViewModel.Factory)
    NavHost(
        navController = navController,
        enterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }) + fadeOut() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut() },
        startDestination = Login
    )
    {
        composable<Login> {
            LoginScreen(
                onSignUpNav = { navController.navigate(route = SignUp) },
                onRecoveryNav = { navController.navigate(route = ForgotPassword) },
                onLoginSuccessNav = { token ->
                    navController.navigate(route = if (token.role == "ADMIN") Admin else User) {
                        popUpTo(route = Login)
                    }
                }
            )
        }
        composable<SignUp> {
            RegisterScreen(
                onBackNav = { navController.popBackStack() },
                onLoginNav = { navController.popBackStack() },
                onRegisterSuccessNav = { email ->
                    navController.navigate(route = VerifyAccount(email)) {
                        popUpTo(SignUp) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<ForgotPassword> {
            ForgotPasswordScreen(
                onBackNav = { navController.popBackStack() },
                onSendEmailSuccessNav = { emailForgot ->
                    navController.navigate(route = ResetPassword(emailForgot)) {
                        popUpTo(route = ForgotPassword) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<VerifyAccount> {
            VerifyAccountScreen(
                onBackNav = { navController.popBackStack() },
                onVerifySuccessNav = { token: TokenResponse ->
                    val role = token.role
                    navController.navigate(route = if (role == "ADMIN") Admin else User) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                    }
                }
            )
        }
        composable<ResetPassword> {
            ResetPasswordScreen(
                onResetSuccess = {
                    navController.navigate(Login) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                    }
                }
            )
        }
        composable<User> { UserRoute(
            onNavigateToSearchScreen = {navController.navigate(route = Search) {
                popUpTo(route = User) { inclusive = false }
            }},
            viewModel = userHomeScreenViewModel
        ) }
        composable<Admin> { }
        composable<Search> {
            SearchScreen(viewModel = userHomeScreenViewModel)
        }
    }
}