package com.example.dementiaDetectorApp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dementiaDetectorApp.api.stripe.StripeRepo
import com.example.dementiaDetectorApp.api.stripe.StripeRequest
import com.example.dementiaDetectorApp.models.PaymentState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PaymentVM @Inject constructor(
    private val stripeRepo: StripeRepo
) : ViewModel() {

    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState = _paymentState.asStateFlow()

    private val _checkoutUrl = MutableStateFlow<String?>(null)
    val checkoutUrl = _checkoutUrl.asStateFlow()

    fun createPaymentIntent(patientId: Int) {
        viewModelScope.launch {
            _paymentState.value = PaymentState.Loading
            try {
                val response = stripeRepo.paymentIntent(StripeRequest(patientID = patientId))
                _checkoutUrl.value = response.paymentURL
                _paymentState.value = PaymentState.Ready
            } catch (e: Exception) {
                _paymentState.value = PaymentState.Error("Payment failed")
            }
        }
    }

    fun onPaymentConfirmed() {
        _paymentState.value = PaymentState.Success
    }

    fun resetPayment() {
        _paymentState.value = PaymentState.Idle
        _checkoutUrl.value = null
    }
}