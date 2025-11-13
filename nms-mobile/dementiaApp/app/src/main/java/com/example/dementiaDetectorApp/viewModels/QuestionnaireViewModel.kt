package com.example.dementiaDetectorApp.viewModels

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class QViewModel: ViewModel(){
    //Answer Vals
    private val _gender = MutableStateFlow(0) //0 = Female 1 = Male
    val gender: StateFlow<Int> = _gender

    private val _age = MutableStateFlow(0)
    val age: StateFlow<Int> = _age

    private val _dHand = MutableStateFlow(0) // Left = 0 Right = 1
    val dHand: StateFlow<Int> = _dHand

    private val _weight = MutableStateFlow(0.0F)
    val weight: StateFlow<Float> = _weight

    private val _avgTemp = MutableStateFlow(0.0F)
    val avgTemp: StateFlow<Float> = _avgTemp

    private val _restingHR = MutableStateFlow(0)
    val restingHR: MutableStateFlow<Int> = _restingHR

    private val _oxLv = MutableStateFlow(0)
    val oxLv: StateFlow<Int> = _oxLv

    private val _history = MutableStateFlow(false)
    val history: StateFlow<Boolean> = _history

    private val _smoke = MutableStateFlow(false)
    val smoke: MutableStateFlow<Boolean> = _smoke

    private val _apoe = MutableStateFlow(false)
    val apoe: StateFlow<Boolean> = _apoe

    private val _activityLv = MutableStateFlow("")
    val activityLv: StateFlow<String> = _activityLv

    private val _depressed = MutableStateFlow(false)
    val depressed: StateFlow<Boolean> = _depressed

    private val _diet = MutableStateFlow("")
    val diet: StateFlow<String> = _diet

    private val _goodSleep = MutableStateFlow(false)
    val goodSleep: StateFlow<Boolean> =_goodSleep

    private val _edu = MutableStateFlow("")
    val edu: StateFlow<String> = _edu

    //Var update functions
    fun onGenderChange(newGender: Int){_gender.value=newGender}
    fun onAgeChange(newAge: Int) { _age.value = newAge }
    fun onDHandChange(newDHand: Int) { _dHand.value = newDHand }
    fun onWeightChange(newWeight: Float) { _weight.value = newWeight }
    fun onAvgTempChange(newAvgTemp: Float) { _avgTemp.value = newAvgTemp }
    fun onRestingHRChange(newHR: Int) { _restingHR.value = newHR }
    fun onOxLvChange(newOxLv: Int) { _oxLv.value = newOxLv }
    fun onHistoryChange(hasHistory: Boolean) { _history.value = hasHistory }
    fun onSmokeChange(isSmoker: Boolean) { _smoke.value = isSmoker }
    fun onApoeChange(hasApoe: Boolean) { _apoe.value = hasApoe }
    fun onActivityLvChange(newActivityLv: String) { _activityLv.value = newActivityLv }
    fun onDepressedChange(isDepressed: Boolean) { _depressed.value = isDepressed }
    fun onDietChange(newDiet: String) { _diet.value = newDiet }
    fun onGoodSleepChange(hasGoodSleep: Boolean) { _goodSleep.value = hasGoodSleep }
    fun onEduChange(newEdu: String) { _edu.value = newEdu }
}