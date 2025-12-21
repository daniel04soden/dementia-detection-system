package com.example.dementiaDetectorApp.viewModels

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dementiaDetectorApp.api.stripe.StripeRepo
import com.example.dementiaDetectorApp.api.stripe.StripeRequest
import com.example.dementiaDetectorApp.models.PaymentState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PaymentVM @Inject constructor(
    private val stripeRepo: StripeRepo
) : ViewModel() {

    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState: StateFlow<PaymentState> = _paymentState.asStateFlow()

    private val _clientSecret = MutableStateFlow<String?>(null)
    val clientSecret: StateFlow<String?> = _clientSecret.asStateFlow()

    fun createPaymentIntent(patientId: Int) {
        viewModelScope.launch {
            _paymentState.value = PaymentState.Loading
            try {
                val request = StripeRequest(patientID = patientId)
                val response = stripeRepo.paymentIntent(request)
                _clientSecret.value = response.clientSecret
                _paymentState.value = PaymentState.Ready
            } catch (e: Exception) {
                _paymentState.value =
                    PaymentState.Error(e.message ?: "Payment intent failed")
            }
        }
    }

    fun onPaymentConfirmed() {
        _paymentState.value = PaymentState.Success
    }

    fun resetPayment() {
        _paymentState.value = PaymentState.Idle
        _clientSecret.value = null
    }

    fun createStripeIntent(clientSecret: String): Intent {
        throw NotImplementedError()
    }
}