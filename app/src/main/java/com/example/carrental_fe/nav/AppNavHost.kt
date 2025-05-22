package com.example.carrental_fe.nav

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carrental_fe.dto.response.TokenResponse
import com.example.carrental_fe.screen.admin.AdminRoute
import com.example.carrental_fe.screen.admin.adminUserDetail.UserDetailScreen
import com.example.carrental_fe.screen.employee.EmployeeRoute
import com.example.carrental_fe.screen.forgot.ForgotPasswordScreen
import com.example.carrental_fe.screen.login.LoginScreen
import com.example.carrental_fe.screen.resetPassword.ResetPasswordScreen
import com.example.carrental_fe.screen.signup.RegisterScreen
import com.example.carrental_fe.screen.user.UserRoute
import com.example.carrental_fe.screen.user.userCarDetail.CarDetailScreen
import com.example.carrental_fe.screen.user.userCheckout.PaymentWebViewScreen
import com.example.carrental_fe.screen.user.userContractDetail.ContractDetailsScreen
import com.example.carrental_fe.screen.user.userEditProfile.EditProfileScreen
import com.example.carrental_fe.screen.user.userSearchScreen.SearchScreen
import com.example.carrental_fe.screen.verify.VerifyAccountScreen
import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object SignUp

@Serializable
object ForgotPassword

@Serializable
data class VerifyAccount (val email: String? = null)

@Serializable
data class ResetPassword (val email: String? = null)

@Serializable
data object User

@Serializable
data object Admin

@Serializable
data object Employee

@Serializable
object Search

@Serializable
object EditProfile

@Serializable
data class CarDetail(val carId: String? = null)

@Serializable
data class ContractDetail(val carPrice: Float? = null, val carId: String? = null)

@Serializable
data class UserDetail(val displayName: String? = null)

@Serializable
data class PaymentWebView(val url: String? = null, val carId: String? = null, val contractId: Long? = null, val isRetry: Boolean?)
@Composable
fun AppNavHost (navController: NavHostController = rememberNavController())
{
    NavHost(navController = navController,
        enterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }) + fadeOut() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut() },
        startDestination = Login)
    {
        composable<Login> {
            LoginScreen(
                onSignUpNav = { navController.navigate(route = SignUp) },
                onRecoveryNav =  { navController.navigate(route = ForgotPassword) },
                onLoginSuccessNav = {
                        token -> navController.navigate(route = if (token.role == "ADMIN") Admin else if (token.role =="EMPLOYEE") Employee else User){
                    popUpTo(route = Login)
                }
                }
            )
        }
        composable<SignUp> {
            RegisterScreen(
                onBackNav = { navController.popBackStack() },
                onLoginNav = { navController.popBackStack() },
                onRegisterSuccessNav = {
                        email -> navController.navigate(route = VerifyAccount(email)){
                    popUpTo(SignUp) { inclusive = true }
                    launchSingleTop = true
                }
                }
            )
        }
        composable<ForgotPassword> {
            ForgotPasswordScreen(
                onBackNav = { navController.popBackStack()},
                onSendEmailSuccessNav = {
                        emailForgot -> navController.navigate(route = ResetPassword(emailForgot)){
                    popUpTo(route = ForgotPassword) { inclusive = true }
                    launchSingleTop = true
                }
                }
            )
        }
        composable<VerifyAccount> {
            VerifyAccountScreen(
                onBackNav = { navController.popBackStack() },
                onVerifySuccessNav = {
                        token: TokenResponse ->
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
        composable<User> {
            UserRoute(
                onNavigateToSearchScreen = {
                    navController.navigate(route = Search) {
                        popUpTo(route = User) { inclusive = false }
                    }
                },
                onNavigateToEditProfile = {
                    navController.navigate(route = EditProfile) {
                        popUpTo(route = User) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNavigateToCarDetail = { carId ->
                    navController.navigate(route = CarDetail(carId)) {
                        popUpTo(route = User) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onCheckoutNav = { response, carId, contractId ->
                    navController.navigate(
                        route = PaymentWebView(
                            response,
                            carId,
                            contractId,
                            true
                        )
                    ) {
                        popUpTo(route = ContractDetail(carId = carId)) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(route = Login) {
                        popUpTo(route = User) { inclusive = true }
                    }
                },
            )
        }
        composable<Admin> {
            AdminRoute(
                onNavigateToUserDetail = {
                        displayName -> navController.navigate(route = UserDetail(displayName)){
                    popUpTo(route = Admin) { inclusive = false }
                    launchSingleTop = true
                }
                }
            )
        }
        composable<Employee> {
            EmployeeRoute(
                onNavigateToLogin = {
                    navController.navigate(route = Login) {
                        popUpTo(route = User) { inclusive = true }
                    }
                },
                onNavigateToEditProfile = {
                    navController.navigate(route = EditProfile) {
                        popUpTo(route = User) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable <Search> {
            SearchScreen(
                onNavigateToCarDetail = { carId ->
                    navController.navigate(route = CarDetail(carId)){
                        popUpTo(route = Search) { inclusive = false }
                        launchSingleTop = true
                    }
                },
            )
        }
        composable<EditProfile>{
            EditProfileScreen(
                onBackNav = { navController.popBackStack() },
            )
        }
        composable<CarDetail> {
            CarDetailScreen(
                onContractNav = {
                        carPrice, carId -> navController.navigate(route = ContractDetail(carPrice, carId)){}
                }
            )
        }

        composable<ContractDetail> {
            ContractDetailsScreen(
                onCheckoutNav = {
                        response, carId, contractId -> navController.navigate(route = PaymentWebView(response, carId, contractId, false)){
                    popUpTo(route = ContractDetail(carId = carId)) { inclusive = false }
                    launchSingleTop = true
                }
                }
            )
        }
        composable<PaymentWebView>{
            PaymentWebViewScreen(onBackStab = { navController.navigate(route = User)})
        }
        composable<UserDetail>{
            UserDetailScreen(
                onBackNav = {
                    navController.popBackStack()
                }
            )
        }
    }
}