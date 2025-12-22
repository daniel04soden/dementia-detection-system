package com.example.dementiaDetectorApp.viewModels

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.dementiaDetectorApp.R
import com.example.dementiaDetectorApp.ui.theme.Yellow
import com.example.dementiaDetectorApp.ui.theme.rGreen
import com.example.dementiaDetectorApp.ui.theme.rGreenL
import com.example.dementiaDetectorApp.ui.theme.rOrange
import com.example.dementiaDetectorApp.ui.theme.rRed
import com.example.dementiaDetectorApp.ui.theme.rYellow
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

    private val _tint = MutableStateFlow(Color.Green)
    val tint:StateFlow<Color> = _tint

    fun onRiskChange(score: Int) {
        when (score) {
            1 -> {
                _riskResult.value = "Low risk"
                _riskMsg.value = "Your risk of having dementia are quite low, a checkup with a doctor is not needed but wouldn't hurt"
                _img.value = R.drawable.r2
                _tint.value = rGreenL
            }
            2 -> {
                _riskResult.value = "Moderate risk"
                _riskMsg.value = "You are at moderate risk of having dementia, a checkup with a medical professional is recommended"
                _img.value = R.drawable.r3
                _tint.value = rYellow
            }
            3 -> {
                _riskResult.value = "High risk"
                _riskMsg.value = "You are at high risk of having dementia, a checkup with a medical professional is required"
                _img.value = R.drawable.r4
                _tint.value = rOrange
            }
            4 -> {
                _riskResult.value = "Extremely high risk"
                _riskMsg.value = "You are at extremely high risk of having dementia, you should have a meeting with the medical professional as soon as possible"
                _img.value = R.drawable.r5
                _tint.value = rRed
            }
            else -> {
                _riskResult.value = "No detected risk"
                _riskMsg.value = "Our tests have not detected any signs of dementia, a checkup with a medical professional is not required"
                _img.value = R.drawable.r1
                _tint.value = rGreen
            }
        }
    }
}