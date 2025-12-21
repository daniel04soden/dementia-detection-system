package com.example.dementiaDetectorApp.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dementiaDetectorApp.api.tests.StatusRequest
import com.example.dementiaDetectorApp.api.tests.TestRepository
import com.example.dementiaDetectorApp.api.tests.TestResult
import com.example.dementiaDetectorApp.models.NavBarContent
import com.example.dementiaDetectorApp.models.Test
import com.example.dementiaDetectorApp.ui.util.ToastManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SharedVM @Inject constructor(
    private val repository: TestRepository
): ViewModel(){
    private val resultChannel = Channel<TestResult<Unit>>()
    val testResults = resultChannel.receiveAsFlow()

    var isLoading = false
        private set

    //User
    private val _id = mutableIntStateOf(-1)
    val id: State<Int> = _id
    fun onIdChange(newID: Int) {
        _id.intValue = newID
        Log.d("Id change", "${_id.intValue}")
    }

    private val _hasPaid = mutableStateOf(false)
    val hasPaid:State<Boolean> = _hasPaid
    fun PaidChange(){_hasPaid.value = !_hasPaid.value}

    //NavBar
    val nav = listOf(
        NavBarContent(
            title = "Home",
            iconId = 0,
            "home"
        ),

        NavBarContent(
            title = "Test Status",
            iconId = 0,
            "status"
        ),

        NavBarContent(
            title = "Risk Assessment",
            iconId = 0,
            "risk"
        ),

        NavBarContent(
            title = "Contact",
            iconId = 0,
            "contact"
        )
    )

    private val _navItems = mutableStateOf<List<NavBarContent>>(nav)
    val navItems: State<List<NavBarContent>> = _navItems

    private val _navIndex = mutableIntStateOf(0)
    val navIndex: State<Int> = _navIndex
    fun onNavIndexChange(newIDX: Int){_navIndex.intValue=newIDX}

    //Tests
    private val _tests = mutableStateOf<List<Test>>(emptyList())
    val tests: State<List<Test>> = _tests

    private val questionnaireStatus = mutableIntStateOf(0)

    fun CheckCompleted(route:String, todo: () -> Unit) {
        when (route){
            "questionnaire" -> {
                if (questionnaireStatus.intValue != 0) {
                    ToastManager.showToast("Questionnaire already completed")
                } else {
                    todo()
                }
            }

            "test1" -> {
                if (stage1Status.intValue != 0) {
                    ToastManager.showToast("Test stage 1 already completed")
                } else {
                    todo()
                }
            }

            "test2" -> {
                if (stage2Status.intValue != 0) {
                    ToastManager.showToast("Test stage 2 already completed")
                } else {
                    todo()
                }
            }

            "speech" -> {
                if (speechStatus.intValue != 0) {
                    ToastManager.showToast("Speech test already completed")
                } else {
                    todo()
                }
            }
        }
    }

    private val stage1Status = mutableIntStateOf(0)
    private val stage2Status = mutableIntStateOf(0)
    private val speechStatus = mutableIntStateOf(0)

    fun getStatus(){
        viewModelScope.launch{
            isLoading = true
            val result = repository.getStatus(StatusRequest(id.value))
            questionnaireStatus.intValue = result.data?.LifestyleStatus?:0
            stage1Status.intValue = result.data?.StageOneStatus?:0
            stage2Status.intValue = result.data?.StageTwoStatus?:0
            val speechVal = result.data?.SpeechStatus?:false
            if (speechVal){speechStatus.intValue=2}else speechStatus.intValue=0
        }
    }

    fun updateTestList(){
        getStatus()
        val testList = listOf(
            Test(
                name = "Lifestyle questionnaire",
                route = "questionnaire",
                state = questionnaireStatus.intValue,
            ),

            Test(
                name = "GP Cognitive Test part 1",
                route = "test1",
                state = stage1Status.intValue,
            ),

            Test(
                name = "GP Cognitive Test part 2",
                route = "test2",
                state = stage2Status.intValue,
            ),

            Test(
                name = "Speech Test",
                route = "speech",
                state = speechStatus.intValue,
            )
        )
        _tests.value = testList
    }
}