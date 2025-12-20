package com.example.dementiaDetectorApp.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dementiaDetectorApp.api.tests.TestRepository
import com.example.dementiaDetectorApp.api.tests.TestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class Stage1VM @Inject constructor(
    private val repository: TestRepository
) : ViewModel() {

    private val resultChannel = Channel<TestResult<Unit>>()

    var isLoading = false
        private set

    private val _prefaceVisi = MutableStateFlow(true)
    val prefaceVisi: StateFlow<Boolean> = _prefaceVisi
    fun onVisiChange(newVisi: Boolean){_prefaceVisi.value = newVisi}

    private val _timedVisi = MutableStateFlow(false)
    val timedVisi: StateFlow<Boolean> = _timedVisi
    fun onTimedVisiChange(newVisi: Boolean){_timedVisi.value=newVisi}

    fun visiTimer(){
        viewModelScope.launch {
            delay(1_000L)
            onTimedVisiChange(false)
            onQ1VisiChange(true)
        }
    }

    private val _q1Visi = MutableStateFlow(false)
    val q1Visi: StateFlow<Boolean> = _q1Visi
    fun onQ1VisiChange(newVisi: Boolean){_q1Visi.value = newVisi}

    private val _date = MutableStateFlow("")
    val date: StateFlow<String> = _date
    fun onDateChange(newDate: String){
        _date.value = newDate
        if(_date.value.length==8){
            _date.value = validateAndFormatDate()
        }
    }

    fun validateAndFormatDate(): String {
        val cleanInput = _date.value.replace("/", "").take(8)
        if (cleanInput.length >= 6 && cleanInput.matches(Regex("\\d{6,}"))) {
            return "${cleanInput.take(2)}/${cleanInput.drop(2).take(2)}/${cleanInput.drop(4)}"
        }
        return _date.value
    }


    private val _q2Visi = MutableStateFlow(false)
    val q2Visi: StateFlow<Boolean> = _q2Visi
    fun onQ2VisiChange(newVisi: Boolean){_q2Visi.value = newVisi}

    private val _clock = MutableStateFlow(-1)
    val clock: StateFlow<Int> = _clock
    fun onClockChange(newClock: Int){_clock.value = newClock}

    val clocks = listOf(
        "clock1",
        "clock2",
        "clock3",
        "clock4",
        "clock5",
        "clock6",
    )

    val _confirmVisi = MutableStateFlow(false)
    val confirmVisi: MutableStateFlow<Boolean> = _confirmVisi
    fun onConfChange(newVisi: Boolean){_confirmVisi.value=newVisi}

    private val _q3Visi = MutableStateFlow(false)
    val q3Visi: StateFlow<Boolean> = _q3Visi
    fun onQ3VisiChange(newVisi: Boolean){_q3Visi.value = newVisi}

    private val _newsEntry = MutableStateFlow("")
    val newsEntry: StateFlow<String> = _newsEntry
    fun onNewsEntryChange(newEntry: String){_newsEntry.value = newEntry}

    private val _q4Visi = MutableStateFlow(false)
    val q4Visi: StateFlow<Boolean> = _q4Visi
    fun onQ4VisiChange(newVisi: Boolean){_q4Visi.value=newVisi}

    private val _fName = MutableStateFlow("")
    val fName: StateFlow<String> = _fName
    fun onFNChange(newFN: String){
        _fName.value = newFN
        isQ4FullyAnswered()
    }

    private val _lName = MutableStateFlow("")
    val lName: StateFlow<String> = _lName
    fun onLNChange(newLN: String){
        _lName.value = newLN
        isQ4FullyAnswered()
    }

    private val _number = MutableStateFlow("")
    val number: StateFlow<String> = _number
    fun onNumberChange(newNum: String){
        _number.value = newNum
        isQ4FullyAnswered()
    }

    private val _street = MutableStateFlow("")
    val street: StateFlow<String> = _street
    fun onStreetChange(newStreet: String){
        _street.value = newStreet
        isQ4FullyAnswered()
    }

    private val _city = MutableStateFlow("")
    val city: StateFlow<String> = _city
    fun onAreaChange(newCity: String){
        _city.value = newCity
        isQ4FullyAnswered()
    }

    private val _q4FullyAnswered = mutableStateOf(false)
    val q4FullyAnswered: State<Boolean> = _q4FullyAnswered

    private fun  isQ4FullyAnswered(){
        _q4FullyAnswered.value = (
                _fName.value.isNotEmpty() &&
                        _lName.value.isNotEmpty() &&
                        _number.value.isNotEmpty() &&
                        _street.value.isNotEmpty() &&
                        _city.value.isNotEmpty()
                )
    }

    // Submit function - matches AuthViewModel pattern with Channel results and loading state
    fun submitAnswers(patientID: Int) {
        viewModelScope.launch {
            isLoading = true

            val result = repository.reportStage1(
                patientID = patientID,
                dateQuestion = _date.value,
                clockID = _clock.value,
                news = _newsEntry.value,
                recallName = _fName.value,
                recallSurname = _lName.value,
                recallNumber = _number.value,
                recallStreet = _street.value,
                recallCity = _city.value
            )

            when (result) {
                is TestResult.Success -> {
                    Log.d("Stage1VM", "Submit success: $result")
                    onQ4VisiChange(false)
                    onSuccessChange(true)
                }
                is TestResult.Unauthorized -> {
                    Log.d("Stage1VM", "Submit unauthorized: $result")
                }
                is TestResult.UnknownError -> {
                    Log.d("Stage1VM", "Submit unknown error: $result")
                }
            }

            resultChannel.trySend(result)
            isLoading = false
        }
    }

    private val _successVisi = MutableStateFlow(false)
    val successVisi: StateFlow<Boolean> = _successVisi
    private fun onSuccessChange(newVisi: Boolean){_successVisi.value = newVisi}
}
