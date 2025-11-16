package com.example.dementiaDetectorApp.viewModels

import android.view.View
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TestViewModel: ViewModel() {
    // Additional vals for new fields

    private val _dateA = MutableStateFlow("")
    val dateA: StateFlow<String> = _dateA

    private val _clockA = MutableStateFlow(0)
    val clockA: StateFlow<Int> = _clockA

    private val _newsA = MutableStateFlow("")
    val newsA: StateFlow<String> = _newsA

    private val _recallFName = MutableStateFlow("")
    val recallFName: StateFlow<String> = _recallFName

    private val _recallLName = MutableStateFlow("")
    val recallLName: StateFlow<String> = _recallLName

    private val _recallNum = MutableStateFlow("")
    val recallNum: StateFlow<String> = _recallNum

    private val _recallStreet = MutableStateFlow("")
    val recallStreet: StateFlow<String> = _recallStreet

    private val _recallArea = MutableStateFlow("")
    val recallArea: StateFlow<String> = _recallArea

    private val _rememberA = MutableStateFlow(0)
    val rememberA: StateFlow<Int> = _rememberA

    private val _conversationA = MutableStateFlow(0)
    val conversationA: StateFlow<Int> = _conversationA

    private val _speakingA = MutableStateFlow(0)
    val speakingA: StateFlow<Int> = _speakingA

    private val _financialA = MutableStateFlow(0)
    val financialA: StateFlow<Int> = _financialA

    private val _medicationA = MutableStateFlow(0)
    val medicationA: StateFlow<Int> = _medicationA

    private val _transportA = MutableStateFlow(0)
    val transportA: StateFlow<Int> = _transportA

// Corresponding update functions

    fun onDateAChange(newDateA: String) { _dateA.value = newDateA }
    fun onClockAChange(newClockA: Int) { _clockA.value = newClockA }
    fun onNewsAChange(newNewsA: String) { _newsA.value = newNewsA }
    fun onRecallFNameChange(newFName: String) { _recallFName.value = newFName }
    fun onRecallLNameChange(newLName: String) { _recallLName.value = newLName }
    fun onRecallNumChange(newNum: String) { _recallNum.value = newNum }
    fun onRecallStreetChange(newStreet: String) { _recallStreet.value = newStreet }
    fun onRecallAreaChange(newArea: String) { _recallArea.value = newArea }
    fun onRememberAChange(newRememberA: Int) { _rememberA.value = newRememberA }
    fun onConversationAChange(newConversationA: Int) { _conversationA.value = newConversationA }
    fun onSpeakingAChange(newSpeakingA: Int) { _speakingA.value = newSpeakingA }
    fun onFinancialAChange(newFinancialA: Int) { _financialA.value = newFinancialA }
    fun onMedicationAChange(newMedicationA: Int) { _medicationA.value = newMedicationA }
    fun onTransportAChange(newTransportA: Int) { _transportA.value = newTransportA }

}