package com.example.dementiaDetectorApp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dementiaDetectorApp.api.tests.TestRepository
import com.example.dementiaDetectorApp.api.tests.TestResult
import com.zekierciyas.library.model.QuestionType
import com.zekierciyas.library.model.SurveyModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class Stage2VM @Inject constructor(
    private val repository: TestRepository
): ViewModel() {

    private val resultChannel = Channel<TestResult<Unit>>()
    val testResults = resultChannel.receiveAsFlow()

    var isLoading = false
        private set

    private val _prefaceVisi = MutableStateFlow(true)
    val prefaceVisi: StateFlow<Boolean> = _prefaceVisi
    fun onVisiChange(newVisi: Boolean){_prefaceVisi.value = newVisi}

    private val _formVisi = MutableStateFlow(false)
    val formVisi: StateFlow<Boolean> = _formVisi
    fun onFormChange(newVisi: Boolean){_formVisi.value = newVisi}

    private val _memory = MutableStateFlow(-1)
    val memory: StateFlow<Int> = _memory
    fun onMemoryChange(newVal: Int){_memory.value = newVal}

    private val _conversation = MutableStateFlow(-1)
    val conversation: StateFlow<Int> = _conversation
    fun onConvoChange(newVal: Int){_conversation.value = newVal}

    private val _speaking = MutableStateFlow(-1)
    val speaking: StateFlow<Int> = _speaking
    fun onSpeakChange(newVal: Int){ _speaking.value = newVal}

    private val _financial = MutableStateFlow(-1)
    val financial: StateFlow<Int> = _financial
    fun onFinanceChange(newVal: Int){_financial.value = newVal}

    private val _medication = MutableStateFlow(-1)
    val medication: StateFlow<Int> = _medication
    fun onMedicationChange(newVal: Int){_medication.value = newVal}

    private val _transport = MutableStateFlow(-1)
    val transport: StateFlow<Int> = _transport
    fun onTransportChange(newVal: Int){_transport.value = newVal}

    val answerSet1 = listOf("Yes", "No", "Don't know")
    val answerSet2 = listOf("Yes", "No", "Don't know", "N/A")

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
        answers.forEach { (questionId, answerSet) ->
            val answer = answerSet.firstOrNull() ?: return@forEach

            val newValue = when (answer) {
                "Yes" -> 1
                "No" -> 0
                "Don't know" -> 2
                "N/A" -> 3
                else -> -1  // Keep unanswered state
            }

            // Clear error when user answers a question
            clearError()

            when (questionId) {
                "memory" -> onMemoryChange(newValue)
                "convo" -> onConvoChange(newValue)
                "speech" -> onSpeakChange(newValue)
                "finance" -> onFinanceChange(newValue)
                "medication" -> onMedicationChange(newValue)
                "transport" -> onTransportChange(newValue)
                else -> {}
            }
        }
    }


    fun allQuestionsAnswered(): Boolean {
        return listOf(
            _memory.value,
            _conversation.value,
            _speaking.value,
            _financial.value,
            _medication.value,
            _transport.value
        ).none { it == -1 }
    }

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage
    private fun clearError() { _errorMessage.value = null}

    fun submitAnswers(patientID: Int){
        clearError()
        if(!allQuestionsAnswered()){
            _errorMessage.value= "Please answer all questions before submitting"
            return
        }
        else{
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
                    }
                    is TestResult.UnknownError -> {
                        Log.d("Stage2VM", "Submit unknown error: $result")
                    }
                }

                resultChannel.trySend(result)
                isLoading = false
            }
        }
    }

    private val _successVisi = MutableStateFlow(false)
    val successVisi: StateFlow<Boolean> = _successVisi
    private fun onSuccessChange(newVisi: Boolean){_successVisi.value = newVisi}
}