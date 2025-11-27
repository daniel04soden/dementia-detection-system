package com.example.dementiaDetectorApp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class Stage1VM: ViewModel(){
    //Preface
    private val _timedVisi = MutableStateFlow(true)
    val timedVisi: StateFlow<Boolean> = _timedVisi
    fun onTimedVisiChange(newVisi: Boolean){_timedVisi.value=newVisi}

    fun visiTimer(){
        viewModelScope.launch {
            delay(1_000L)
            onTimedVisiChange(false)
            onQ1VisiChange(true)
        }
    }

    //Question 1
    private val _q1Visi = MutableStateFlow(false)
    val q1Visi: StateFlow<Boolean> = _q1Visi
    fun onQ1VisiChange(newVisi: Boolean){_q1Visi.value = newVisi}

    private val _date = MutableStateFlow("")
    val date: StateFlow<String> = _date
    fun onDateChange(newDate: String){_date.value = newDate}

    //Question 2
    private val _q2Visi = MutableStateFlow(false)
    val q2Visi: StateFlow<Boolean> = _q2Visi
    fun onQ2VisiChange(newVisi: Boolean){_q2Visi.value = newVisi}

    private val _clock = MutableStateFlow(0)
    val clock: StateFlow<Int> = _clock
    fun onClockChange(newClock: Int){_clock.value = newClock}

    val clocks = listOf(
        "clock1",
        "clock2",
        "clock3",
        "clock1",
        "clock2",
        "clock3",
        "clock1",
        "clock2"
    )

    //Question 3
    private val _q3Visi = MutableStateFlow(false)
    val q3Visi: StateFlow<Boolean> = _q3Visi
    fun onQ3VisiChange(newVisi: Boolean){_q3Visi.value = newVisi}

    private val _newsEntry = MutableStateFlow("")
    val newsEntry: StateFlow<String> = _newsEntry
    fun onNewsEntryChange(newEntry: String){_newsEntry.value =  newEntry}

    //Question 4
    private val _q4Visi = MutableStateFlow(false)
    val q4Visi: StateFlow<Boolean> = _q4Visi
    fun onQ4VisiChange(newVisi: Boolean){_q4Visi.value=newVisi}

    private val _fName = MutableStateFlow("")
    val fName: StateFlow<String> = _fName
    fun onFNChange(newFN: String){_fName.value = newFN}

    private val _lName = MutableStateFlow("")
    val lName: StateFlow<String> = _fName
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
}