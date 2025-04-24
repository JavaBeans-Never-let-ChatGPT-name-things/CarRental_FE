package com.example.carrental_fe.screen.userCheckout

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PaymentWebViewScreen(vm: CheckoutViewModel = viewModel(factory = CheckoutViewModel.Factory),onBackStab: () -> Unit) {
    val context = LocalContext.current
    val url = vm.checkoutUrl.collectAsState()
    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        factory = {
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        when {
                            url?.contains("success") == true -> {
                                onBackStab()
                                vm.paymentSuccess()
                                Toast.makeText(context, "Payment Success!", Toast.LENGTH_SHORT).show()
                            }
                            url?.contains("cancel") == true -> {
                                onBackStab()
                                vm.paymentFailed()
                                Toast.makeText(context, "Payment Canceled", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                loadUrl(url.value)
            }
        }
    )
}