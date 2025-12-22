package com.example.dementiaDetectorApp.ui.composables

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.browser.customtabs.CustomTabsIntent
import com.example.dementiaDetectorApp.models.PaymentState
import com.example.dementiaDetectorApp.ui.theme.buttonColours
import com.example.dementiaDetectorApp.viewModels.PaymentVM
import com.example.dementiaDetectorApp.viewModels.SharedVM

@Composable
fun StripePaymentButton(
    stripeViewModel: PaymentVM,
    sharedVM: SharedVM,
    buttonContent: @Composable () -> Unit
) {
    val context = LocalContext.current
    val checkoutUrl by stripeViewModel.checkoutUrl.collectAsStateWithLifecycle()
    val paymentState by stripeViewModel.paymentState.collectAsStateWithLifecycle()

    Button(
        onClick = {
            if (paymentState == PaymentState.Idle) {
                stripeViewModel.createPaymentIntent(sharedVM.id.value)
            }
        },
        colors = buttonColours(),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .padding(bottom = 25.dp)
    ) {
        buttonContent()
    }

    LaunchedEffect(paymentState) {
        if (paymentState is PaymentState.Ready && checkoutUrl != null) {
            val customTabsIntent = CustomTabsIntent.Builder().build()
            customTabsIntent.launchUrl(context, Uri.parse(checkoutUrl!!))
        }
    }
}