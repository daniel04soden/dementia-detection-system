package com.example.dementiaDetectorApp.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dementiaDetectorApp.R
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
) : ViewModel() {

    private val resultChannel = Channel<TestResult<Unit>>()
    val testResults = resultChannel.receiveAsFlow()

    var isLoading = false
        private set

    private val _id = mutableIntStateOf(-1)
    val id: State<Int> = _id
    fun onIdChange(newID: Int) {
        _id.intValue = newID
    }

    // --------------------------------------------
    // Navigation
    // --------------------------------------------

    val nav = listOf(
        NavBarContent("Home", R.drawable.home, "home"),
        NavBarContent("Test Status", R.drawable.test, "status"),
        NavBarContent("Risk Assessment", R.drawable.ra, "risk"),
        NavBarContent("Contact", R.drawable.contact, "contact")
    )

    private val _navItems = mutableStateOf(nav)
    val navItems: State<List<NavBarContent>> = _navItems

    private val _navIndex = mutableIntStateOf(0)
    val navIndex: State<Int> = _navIndex
    fun onNavIndexChange(newIDX: Int) {
        _navIndex.intValue = newIDX
    }

    // --------------------------------------------
    // Tests & Status
    // --------------------------------------------

    private val _tests = mutableStateOf<List<Test>>(emptyList())
    val tests: State<List<Test>> = _tests

    private val _testsDone = mutableIntStateOf(0)
    val testsDone: State<Int> = _testsDone

    private val questionnaireStatus = mutableIntStateOf(0)
    private val stage1Status = mutableIntStateOf(0)
    private val stage2Status = mutableIntStateOf(0)
    private val speechStatus = mutableIntStateOf(0)

    private val _riskScore = mutableIntStateOf(0)
    val riskScore: State<Int> = _riskScore

    // --------------------------------------------
    // Status fetching
    // --------------------------------------------

    fun getStatus() {
        viewModelScope.launch {
            isLoading = true

            val result = repository.getStatus(StatusRequest(id.value))

            questionnaireStatus.intValue = result.data?.lifestyleStatus ?: 0
            stage1Status.intValue = result.data?.stageOneStatus ?: 0
            stage2Status.intValue = result.data?.stageTwoStatus ?: 0
            speechStatus.intValue = result.data?.speechTestStatus ?: 0

            _tests.value = listOf(
                Test("Lifestyle questionnaire", "questionnaire", questionnaireStatus.intValue),
                Test("GP Cognitive Test part 1", "test1", stage1Status.intValue),
                Test("GP Cognitive Test part 2", "test2", stage2Status.intValue),
                Test("Speech Test", "speech", speechStatus.intValue)
            )

            _riskScore.intValue = _tests.value.sumOf { getTestScore(it.state) }
            _testsDone.intValue = _tests.value.count { it.state > 1 }

            isLoading = false
        }
    }

    fun onTestSubmission(idx: Int) {
        _tests.value = _tests.value.toMutableList().also {
            it[idx] = it[idx].copy(state = 1)
        }

        _riskScore.intValue = _tests.value.sumOf { getTestScore(it.state) }
        _testsDone.intValue = _tests.value.count { it.state > 1 }

        getStatus()
    }

    private fun getTestScore(state: Int): Int {
        return when (state) {
            in 0..2 -> 0
            else -> 1
        }
    }

    // --------------------------------------------
    // Navigation checks
    // --------------------------------------------

    fun CheckCompleted(
        route: String,
        todo: () -> Unit,
        aiTodo: (() -> Unit)? = null
    ) {
        when (route) {
            "questionnaire" -> {
                if ((questionnaireStatus.intValue != 0) && questionnaireStatus.intValue != 5) {
                    ToastManager.showToast("Questionnaire already completed")
                } else {
                    if (questionnaireStatus.intValue == 5) {
                        aiTodo?.invoke()
                    } else {
                        todo()
                    }
                }
            }

            "test1" -> {
                if (stage1Status.intValue != 0)
                    ToastManager.showToast("Test stage 1 already completed")
                else todo()
            }

            "test2" -> {
                if (stage1Status.intValue == 0) {
                    ToastManager.showToast("Complete Stage 1 before Stage 2")
                } else if (stage2Status.intValue != 0) {
                    ToastManager.showToast("Test stage 2 already completed")
                } else {
                    todo()
                }
            }

            "speech" -> {
                if (speechStatus.intValue != 0)
                    ToastManager.showToast("Speech test already completed")
                else todo()
            }
        }
    }

    init {
        getStatus()
    }
}