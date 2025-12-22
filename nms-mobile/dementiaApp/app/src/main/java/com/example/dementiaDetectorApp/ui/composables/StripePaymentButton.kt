package com.example.dementiaDetectorApp.ui.composables

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dementiaDetectorApp.models.PaymentState
import com.example.dementiaDetectorApp.ui.theme.buttonColours
import com.example.dementiaDetectorApp.viewModels.SharedVM
import kotlinx.coroutines.delay
import com.example.dementiaDetectorApp.viewModels.PaymentVM

@Composable
fun StripePaymentButton(
    stripeViewModel: PaymentVM,
    sharedVM: SharedVM,
    buttonContent: @Composable () -> Unit
) {
    val context = LocalContext.current
    val checkoutUrl by stripeViewModel.checkoutUrl.collectAsStateWithLifecycle()
    val paymentState by stripeViewModel.paymentState.collectAsStateWithLifecycle()

    // Payment button
    Button(
        onClick = {
            if (paymentState == PaymentState.Idle) {
                stripeViewModel.createPaymentIntent(sharedVM.id.value)
            }
        },
        colors = buttonColours(),
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .padding(bottom = 25.dp)
    ) {
        buttonContent()
    }

    // Open Stripe Checkout when ready
    LaunchedEffect(paymentState) {
        if (paymentState is PaymentState.Ready && checkoutUrl != null) {
            val customTabsIntent = CustomTabsIntent.Builder().build()
            customTabsIntent.launchUrl(context, Uri.parse(checkoutUrl!!))
        }
    }

    // Poll for payment completion
    LaunchedEffect(Unit) {
        while (true) {
            delay(2000L) // Poll every 2 seconds
            try {
                stripeViewModel.checkPaymentStatus()
            } catch (_: Exception) {}
        }
    }
}