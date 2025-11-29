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
class QViewModel @Inject constructor(
    private val repository: TestRepository
): ViewModel() {

    private val resultChannel = Channel<TestResult<Unit>>()
    val testResults = resultChannel.receiveAsFlow()

    var isLoading = false
        private set

    //Preface visibility
    private val _prefaceVisi = MutableStateFlow(true)
    val prefaceVisi: StateFlow<Boolean> = _prefaceVisi
    fun onVisiChange(newVisi: Boolean){_prefaceVisi.value = newVisi}

    //Section 1
    private val _s1visi = MutableStateFlow(false)
    val s1visi: StateFlow<Boolean> = _s1visi
    fun onS1Change(newVisi: Boolean){_s1visi.value = newVisi}

    private val _gender = MutableStateFlow(-1) //0 = Female 1 = Male
    val gender: StateFlow<Int> = _gender
    val genderOptions = listOf("Male", "Female")
    private fun onGenderChange(newGender: Int){_gender.value=newGender}

    private val _age = MutableStateFlow(0)
    val age: StateFlow<Int> = _age
    private fun onAgeChange(newAge: Int) { _age.value = newAge }

    private val _dHand = MutableStateFlow(-1) // Left = 0 Right = 1
    val dHand: StateFlow<Int> = _dHand
    val dHandOptions= listOf("Left Handed", "Right Handed")
    private fun onDHandChange(newDHand: Int) { _dHand.value = newDHand }

    private val _edu = MutableStateFlow("")
    val edu: StateFlow<String> = _edu
    private fun onEduChange(newEdu: String) { _edu.value = newEdu }
    val eduOptions = listOf(
        "No formal education", "Some primary education", "Completed primary education",
        "Some secondary education", "Completed secondary education", "Some college/university",
        "Associate degree", "Bachelor's degree", "Some postgraduate education",
        "Master's degree", "Professional degree", "Doctorate"
    )

    val s1Survey = listOf(
        //Gender
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "gender",
            questionTitle = "1) Gender",
            answers = genderOptions,
            questionDescription = "Select your gender"
        ),

        //Age
        SurveyModel(
            questionType = QuestionType.TEXT,
            questionId = "age",
            questionTitle = "2) Age",
            questionDescription = "Enter your age"
        ),

        //Dominant Hand
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "dHand",
            questionTitle = "3) Dominant Hand",
            questionDescription = "Select whether you are left or right handed",
            answers = dHandOptions
        ),

        //Education Level
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "edu",
            questionTitle = "4) Education Level",
            questionDescription = "Classify your level of education",
            answers = eduOptions
        ),
    )

    fun isS1Complete(): Boolean {
        return gender.value != -1 &&
                age.value > 0 &&
                dHand.value != -1 &&
                edu.value.isNotEmpty()
    }

    //Section 2
    private val _s2visi = MutableStateFlow(false)
    val s2visi: StateFlow<Boolean> = _s2visi
    fun onS2Change(newVisi: Boolean){_s2visi.value = newVisi}

    private val _weight = MutableStateFlow(-1.0F)
    val weight: StateFlow<Float> = _weight
    fun onWeightChange(newWeight: Float){_weight.value = newWeight}

    private val _avgTemp = MutableStateFlow(-1.0F)
    val avgTemp: StateFlow<Float> = _avgTemp
    fun onAvgTempChange(newAvgTemp: Float) { _avgTemp.value = newAvgTemp }

    private val _restingHR = MutableStateFlow(-1)
    val restingHR: MutableStateFlow<Int> = _restingHR
    fun onRestingHRChange(newHR: Int) { _restingHR.value = newHR }

    private val _oxLv = MutableStateFlow(-1)
    val oxLv: StateFlow<Int> = _oxLv
    fun onOxLvChange(newOxLv: Int) { _oxLv.value = newOxLv }

    private val _apoe = MutableStateFlow(false)
    val apoe: StateFlow<Boolean> = _apoe
    fun onApoeChange(hasApoe: Boolean) { _apoe.value = hasApoe }

    private val _history = MutableStateFlow(false)
    val history: StateFlow<Boolean> = _history
    fun onHistoryChange(hasHistory: Boolean) { _history.value = hasHistory }

    val s2Survey = listOf(
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "history",
            questionDescription = "Does your family have a history of getting dementia",
            questionTitle = "1) History",
            answers = listOf("Yes", "No")
        ),
        SurveyModel(
            questionType = QuestionType.TEXT,
            questionId = "weight",
            questionDescription = "Enter you current body weight in Kg",
            questionTitle = "2) Weight",
        ),
        SurveyModel(
            questionType = QuestionType.TEXT,
            questionId = "avgTemp",
            questionDescription = "What is your usual average temperature",
            questionTitle = "3) Average Temperature",
        ),
        SurveyModel(
            questionType = QuestionType.TEXT,
            questionId = "hr",
            questionDescription = "What is your average resting heart-rate",
            questionTitle = "4) Average Resting Heart-Rate",
        ),
        SurveyModel(
            questionType = QuestionType.TEXT,
            questionId = "oxLv",
            questionDescription = "What is your average Blood Oxygen Level",
            questionTitle = "5) Blood Oxygen Level",
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "apoe",
            questionDescription = "Do you possess the APOE gene",
            questionTitle = "6) APOE",
            answers = listOf("Yes", "No")
        )
    )

    fun isS2Complete(): Boolean {
        return history.value != null &&
                weight.value > -1f &&
                avgTemp.value > -1f &&
                restingHR.value > -1 &&
                oxLv.value > -1 &&
                apoe.value != null
    }

    //Section 3
    private val _s3visi = MutableStateFlow(false)
    val s3visi: StateFlow<Boolean> = _s3visi
    fun onS3Change(newVisi: Boolean){_s3visi.value = newVisi}

    private val _smoke = MutableStateFlow(false)
    val smoke: MutableStateFlow<Boolean> = _smoke
    fun onSmokeChange(isSmoker: Boolean) { _smoke.value = isSmoker }

    private val _activityLv = MutableStateFlow("")
    val activityLv: StateFlow<String> = _activityLv
    fun onActivityLvChange(newActivityLv: String) { _activityLv.value = newActivityLv }

    private val _depressed = MutableStateFlow(false)
    val depressed: StateFlow<Boolean> = _depressed
    fun onDepressedChange(isDepressed: Boolean) { _depressed.value = isDepressed }

    private val _diet = MutableStateFlow("")
    val diet: StateFlow<String> = _diet
    fun onDietChange(newDiet: String) { _diet.value = newDiet }

    private val _goodSleep = MutableStateFlow(false)
    val goodSleep: StateFlow<Boolean> =_goodSleep
    fun onGoodSleepChange(hasGoodSleep: Boolean) { _goodSleep.value = hasGoodSleep }


    val activityOptions = listOf("Sedentary", "Lightly Active", "Moderately Active", "Very Active", "Extra Active")
    val dietOptions = listOf("Standard", "Vegetarian", "Vegan", "Low Carb", "Mediterranean", "Other")

    val s3Survey = listOf(
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "smoker",
            questionTitle = "1) Do you smoke",
            questionDescription = "Are you, or have you previously been a regular smoker",
            answers = listOf("Yes", "No")
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "sleep",
            questionTitle = "2) Sleep Quality",
            questionDescription = "Do you usually get good (8+ hours) of sleep",
            answers = listOf("Yes", "No")
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "depressed",
            questionTitle = "3) Depression",
            questionDescription = "Are you, or have you previously been diagnosed with depression",
            answers = listOf("Yes", "No")
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "actLv",
            questionTitle = "4) Activity Level",
            questionDescription = "Choose what best describes your usual level of physical activity",
            answers = activityOptions
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "diet",
            questionTitle = "5) Diet",
            questionDescription = "Choose what best describes your usual diet",
            answers = dietOptions
        ),
    )

    fun isS3Complete(): Boolean {
        return smoke.value != null &&
                goodSleep.value != null &&
                depressed.value != null &&
                activityLv.value.isNotEmpty() &&
                diet.value.isNotEmpty()
    }

    fun onSurveyAnswerChange(answers: Map<String, Set<String>>) {
        answers.forEach { (questionId, answerSet) ->
            val answer = answerSet.firstOrNull() ?: return@forEach
            val yesNoToBoolean = answer.equals("Yes", ignoreCase = true)

            when (questionId) {
                // Section 1
                "gender" -> {
                    val newValue = if (answer == "Male") 1 else 0
                    onGenderChange(newValue)
                }
                "dHand" -> {
                    val newValue = if (answer == "Right Handed") 1 else 0
                    onDHandChange(newValue)
                }
                "edu" -> onEduChange(answer)
                "age" -> onAgeChange(answer.toIntOrNull() ?: 0)

                // Section 2
                "history" -> onHistoryChange(yesNoToBoolean)
                "apoe" -> onApoeChange(yesNoToBoolean)
                "weight" -> onWeightChange(answer.toFloatOrNull() ?: 0f)
                "avgTemp" -> onAvgTempChange(answer.toFloatOrNull() ?: 0f)
                "hr" -> onRestingHRChange(answer.toIntOrNull() ?: 0)
                "oxLv" -> onOxLvChange(answer.toIntOrNull() ?: 0)

                // Section 3
                "smoker" -> onSmokeChange(yesNoToBoolean)
                "sleep" -> onGoodSleepChange(yesNoToBoolean)
                "depressed" -> onDepressedChange(yesNoToBoolean)
                "actLv" -> onActivityLvChange(answer)
                "diet" -> onDietChange(answer)
            }
        }
    }


    fun submitAnswers(){
        viewModelScope.launch {
            isLoading = true

            val result = repository.reportQuestionnaire(
                patientID = 1,
                gender = _gender.value,
                age = _age.value,
                dHand = _dHand.value,
                weight = _weight.value,
                avgTemp = _avgTemp.value,
                restingHR = _restingHR.value,
                oxLv = _oxLv.value,
                history = _history.value,
                smoke = _smoke.value,
                apoe = _apoe.value,
                activityLv = _activityLv.value,
                depressed = _depressed.value,
                diet = _diet.value,
                goodSleep = _goodSleep.value,
                edu = _edu.value,
            )
            when (result) {
                is TestResult.Success -> {
                    Log.d("Stage1VM", "Submit success: $result")
                    onS3Change(false)
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