package com.example.dementiaDetectorApp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dementiaDetectorApp.api.tests.TestRepository
import com.example.dementiaDetectorApp.api.tests.TestResult
import com.example.dementiaDetectorApp.models.results.RecallRes
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@HiltViewModel
class Stage1VM @Inject constructor(
    private val repository: TestRepository
) : ViewModel() {

    private val resultChannel = Channel<TestResult<Unit>>()
    val testResults = resultChannel.receiveAsFlow()

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
    fun onDateChange(newDate: String){_date.value = newDate}

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

    val clockHands = listOf(
        "12:10",
        "11:21",
        "11:10",
        "11:10",
        "7:58",
        "10:50"
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
    fun onFNChange(newFN: String){_fName.value = newFN}

    private val _lName = MutableStateFlow("")
    val lName: StateFlow<String> = _lName
    fun onLNChange(newLN: String){_lName.value = newLN}

    private val _number = MutableStateFlow("")
    val number: StateFlow<String> = _number
    fun onNumberChange(newNum: String){_number.value = newNum}

    private val _street = MutableStateFlow("")
    val street: StateFlow<String> = _street
    fun onStreetChange(newStreet: String){_street.value = newStreet}

    private val _area = MutableStateFlow("")
    val area: StateFlow<String> = _area
    fun onAreaChange(newArea: String){_area.value = newArea}

    // Submit function - matches AuthViewModel pattern with Channel results and loading state
    fun submitAnswers() {
        viewModelScope.launch {
            isLoading = true

            val recall = RecallRes(
                name = fName.value,
                surname = lName.value,
                number = number.value,
                street = street.value,
                city = area.value
            )

            val result = repository.reportStage1(
                testDate = date.value,
                dateQuestion = date.value,
                clockNumber = clocks[clock.value],
                clockHands = "10 past 11",
                news = newsEntry.value,
                recall = recall
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
