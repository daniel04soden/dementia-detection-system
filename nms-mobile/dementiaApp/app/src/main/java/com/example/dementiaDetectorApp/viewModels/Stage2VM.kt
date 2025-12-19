package com.example.dementiaDetectorApp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dementiaDetectorApp.api.tests.TestRepository
import com.example.dementiaDetectorApp.api.tests.TestResult
import com.example.dementiaDetectorApp.ui.util.ToastManager
import com.zekierciyas.library.model.QuestionType
import com.zekierciyas.library.model.SurveyModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class Stage2VM @Inject constructor(
    private val repository: TestRepository
): ViewModel() {

    private val resultChannel = Channel<TestResult<Unit>>()
    val testResults = resultChannel.receiveAsFlow()

    private var isLoading = false

    private val _prefaceVisi = MutableStateFlow(true)
    val prefaceVisi: StateFlow<Boolean> = _prefaceVisi
    fun onVisiChange(newVisi: Boolean){_prefaceVisi.value = newVisi}

    private val _formVisi = MutableStateFlow(false)
    val formVisi: StateFlow<Boolean> = _formVisi
    fun onFormChange(newVisi: Boolean){_formVisi.value = newVisi}

    private val _memory = MutableStateFlow(-1)
    private val memory: StateFlow<Int> = _memory
    private fun onMemoryChange(newVal: Int){_memory.value = newVal}

    private val _conversation = MutableStateFlow(-1)
    private val conversation: StateFlow<Int> = _conversation
    private fun onConvoChange(newVal: Int){_conversation.value = newVal}

    private val _speaking = MutableStateFlow(-1)
    private val speaking: StateFlow<Int> = _speaking
    private fun onSpeakChange(newVal: Int){ _speaking.value = newVal}

    private val _financial = MutableStateFlow(-1)
    private val financial: StateFlow<Int> = _financial
    private fun onFinanceChange(newVal: Int){_financial.value = newVal}

    private val _medication = MutableStateFlow(-1)
    private val medication: StateFlow<Int> = _medication
    private fun onMedicationChange(newVal: Int){_medication.value = newVal}

    private val _transport = MutableStateFlow(-1)
    private val transport: StateFlow<Int> = _transport
    private fun onTransportChange(newVal: Int){_transport.value = newVal}

    val allQuestionsAnswered: StateFlow<Boolean> = combine(
        memory, conversation, speaking, financial, medication, transport
    ) { values ->
        values.all { it != -1 }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    private val answerSet1 = listOf("Yes", "No", "Don't know")
    private val answerSet2 = listOf("Yes", "No", "Don't know", "N/A")

    val survey = listOf(
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "memory",
            questionTitle = "1) Memory",
            questionDescription = "Does the patient have more trouble remembering things that have happened recently than before?",
            answers = answerSet1
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "convo",
            questionTitle = "2) Conversation",
            questionDescription = "Does s/he have more trouble recalling conversations a few days later?",
            answers = answerSet1
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "speech",
            questionTitle = "3) Speech",
            questionDescription = "When speaking, does s/he have more difficulty finding the right word or tend to use the wrong words more often?",
            answers = answerSet1
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "finance",
            questionTitle = "4) Financial Matters",
            questionDescription = "Is s/he less able to manage money and financial affairs (e.g. paying bills and budgeting)?",
            answers = answerSet2
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "medication",
            questionTitle = "5) Medication",
            questionDescription = "Is s/he less able to manage his or her medication independently?",
            answers = answerSet2
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "transport",
            questionTitle = "6) Transport",
            questionDescription = "Does s/he need more assistance with transport (either private or public)? If due to physical problems only, tick 'No'.",
            answers = answerSet2
        ),
    )

    fun onSurveyAnswerChange(answers: Map<String, Set<String>>) {
        Log.e("S2-CALLBACK", "Answers map: $answers")
        if (answers.isEmpty()) {
            Log.e("S2-CALLBACK", "Empty answers - skipping")
            return
        }
        answers.forEach { (questionId, answerSet) ->
            val answer = answerSet.firstOrNull()
            Log.e("S2-CALLBACK", "Processing: $questionId = $answer")
            if (answer != null) {
                val newValue = when (answer) {
                    "Yes" -> 1
                    "No" -> 0
                    "Don't know" -> 2
                    "N/A" -> 3
                    else -> -1
                }
                Log.e("S2-CALLBACK", "$questionId set to $newValue")
                when (questionId) {
                    "memory" -> onMemoryChange(newValue)
                    "convo" -> onConvoChange(newValue)
                    "speech" -> onSpeakChange(newValue)
                    "finance" -> onFinanceChange(newValue)
                    "medication" -> onMedicationChange(newValue)
                    "transport" -> onTransportChange(newValue)
                    else -> Log.e("S2-CALLBACK", "Unknown ID: $questionId")
                }
            }
        }
    }

    fun submitAnswers(patientID: Int){
        viewModelScope.launch{
            isLoading = true

            val result = repository.reportStage2(
                patientID = patientID,
                memoryScore = _memory.value,
                recallRes = _conversation.value,
                speakingScore = _speaking.value,
                financialScore = _financial.value,
                medicineScore = _medication.value,
                transportScore = _transport.value
            )

            when (result) {
                is TestResult.Success -> {
                    Log.d("Stage2VM", "Submit success: $result")
                    onFormChange(false)
                    onSuccessChange(true)
                }
                is TestResult.Unauthorized -> {
                    Log.d("Stage2VM", "Submit unauthorized: $result")
                    ToastManager.showToast("There was an authorization error, please logout and log back in again")
                }
                is TestResult.UnknownError -> {
                    Log.d("Stage2VM", "Submit unknown error: $result")
                    ToastManager.showToast("There was an error submitting the results, please try again later.")
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