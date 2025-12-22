package com.example.dementiaDetectorApp.viewModels

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.dementiaDetectorApp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class RiskVM @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _riskResult = mutableStateOf("")
    val riskResult: State<String> = _riskResult

    private val _riskMsg = mutableStateOf("")
    val riskMsg: State<String> = _riskMsg

    private val _img = MutableStateFlow(R.drawable.logo)
    val img: StateFlow<Int> = _img

    fun onRiskChange(score: Int) {
        when (score) {
            1 -> {
                _riskResult.value = "Low risk"
                _riskMsg.value = "Your risk of having dementia are quite low, a checkup with a doctor is not needed but wouldn't hurt"
            }
            2 -> {
                _riskResult.value = "Moderate risk"
                _riskMsg.value = "You are at moderate risk of having dementia, a checkup with a medical professional is recommended"
            }
            3 -> {
                _riskResult.value = "High risk"
                _riskMsg.value = "You are at high risk of having dementia, a checkup with a medical professional is required"
            }
            4 -> {
                _riskResult.value = "Extremely high risk"
                _riskMsg.value = "You are at extremely high risk of having dementia, you should have a meeting with the medical professional as soon as possible"
            }
            else -> {
                _riskResult.value = "No detected risk"
                _riskMsg.value = "Our tests have not detected any signs of dementia, a checkup with a medical professional is not required"
            }
        }
    }
}