package com.example.dementiaDetectorApp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dementiaDetectorApp.api.tests.LifestyleRequest
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

    private val _prefaceVisi = MutableStateFlow(true)
    val prefaceVisi: StateFlow<Boolean> = _prefaceVisi
    fun onVisiChange(newVisi: Boolean){_prefaceVisi.value = newVisi}

    private val _s1visi = MutableStateFlow(false)
    val s1visi: StateFlow<Boolean> = _s1visi
    fun onS1Change(newVisi: Boolean){_s1visi.value = newVisi}

    private val _gender = MutableStateFlow(-1)
    val gender: StateFlow<Int> = _gender
    private fun onGenderChange(newGender: Int){_gender.value=newGender}

    private val _age = MutableStateFlow(0)
    val age: StateFlow<Int> = _age
    private fun onAgeChange(newAge: Int) { _age.value = newAge }

    private val _dominantHand = MutableStateFlow(-1)
    val dominantHand: StateFlow<Int> = _dominantHand
    private fun onDominantHandChange(newValue: Int) { _dominantHand.value = newValue }

    private val _education = MutableStateFlow("")
    val education: StateFlow<String> = _education
    private fun onEducationChange(newEdu: String)
    {
        _education.value = newEdu
        if (newEdu == "Primary Level"){
            onPrimaryChange(true)
            onSecondaryChange(false)
            onDegreeChange(false)
        }
        else if (newEdu == "Secondary Level"){
            onPrimaryChange(true)
            onSecondaryChange(false)
            onDegreeChange(false)
        }
        else if (newEdu == "Tertiary Level"){
            onPrimaryChange(true)
            onSecondaryChange(true)
            onDegreeChange(true)
        }
        else{
            onPrimaryChange(false)
            onSecondaryChange(false)
            onDegreeChange(false)
        }
    }

    private val _cumulativePrimary = MutableStateFlow("")
    private fun onPrimaryChange(change:Boolean){_cumulativePrimary.value = change.toString().uppercase()}

    private val _cumulativeSecondary = MutableStateFlow("")
    private fun onSecondaryChange(change:Boolean){_cumulativeSecondary.value = change.toString().uppercase()}

    private val _cumulativeDegree = MutableStateFlow("")
    private fun onDegreeChange(change:Boolean){_cumulativeDegree.value = change.toString().uppercase()}

    val eduOptions = listOf("Primary Level", "Secondary Level", "Tertiary Level")

    val s1Survey = listOf(
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "gender",
            questionTitle = "1) Gender",
            answers = listOf("Female", "Male"),
            questionDescription = "Select your gender"
        ),
        SurveyModel(
            questionType = QuestionType.TEXT,
            questionId = "age",
            questionTitle = "2) Age",
            questionDescription = "Enter your age"
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "dominantHand",
            questionTitle = "3) Dominant Hand",
            questionDescription = "Select whether you are left or right handed",
            answers = listOf("Left Handed", "Right Handed")
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "education",
            questionTitle = "4) Education Level",
            questionDescription = "What is your highest level of education",
            answers = eduOptions
        ),
    )

    fun isS1Complete(): Boolean {
        return gender.value != -1 &&
                age.value > 0 &&
                dominantHand.value != -1 &&
                education.value.isNotEmpty()
    }

    private val _s2visi = MutableStateFlow(false)
    val s2visi: StateFlow<Boolean> = _s2visi
    fun onS2Change(newVisi: Boolean){_s2visi.value = newVisi}

    private val _weight = MutableStateFlow(-1.0F)
    val weight: StateFlow<Float> = _weight
    fun onWeightChange(newWeight: Float){_weight.value = newWeight}

    private val _bodyTemperature = MutableStateFlow(-1.0F)
    val bodyTemperature: StateFlow<Float> = _bodyTemperature
    fun onBodyTemperatureChange(v: Float) { _bodyTemperature.value = v }

    private val _heartRate = MutableStateFlow(-1)
    val heartRate: MutableStateFlow<Int> = _heartRate
    fun onHeartRateChange(v: Int) { _heartRate.value = v }

    private val _bloodOxygen = MutableStateFlow(-1.0F)
    val bloodOxygen: StateFlow<Float> = _bloodOxygen
    fun onBloodOxygenChange(v: Float) { _bloodOxygen.value = v }

    private val _apoeE4 = MutableStateFlow(-1)
    val apoeE4: StateFlow<Int> = _apoeE4
    fun onApoeE4Change(v: Int) { _apoeE4.value = v }

    private val _familyHistory = MutableStateFlow(-1)
    val familyHistory: StateFlow<Int> = _familyHistory
    fun onFamilyHistoryChange(v: Int) { _familyHistory.value = v }

    private val _diabetic = MutableStateFlow(-1)
    val diabetic: StateFlow<Int> = _diabetic
    fun onDiabeticChange(value: Int) { _diabetic.value = value }

    private val _alcoholLevel = MutableStateFlow(-1.0f)
    val alcoholLevel: StateFlow<Float> = _alcoholLevel
    fun onAlcoholLevelChange(value: Float) { _alcoholLevel.value = value }

    private val _bloodPressure = MutableStateFlow(-1)
    val bloodPressure: StateFlow<Int> = _bloodPressure
    fun onBloodPressureChange(value: Int) { _bloodPressure.value = value }

    private val _hearingLoss = MutableStateFlow(-1)
    val hearingLoss: StateFlow<Int> = _hearingLoss
    fun onHearingLossChange(value: Int) { _hearingLoss.value = value }

    val s2Survey = listOf(
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "familyHistory",
            questionDescription = "Does your family have a history of dementia?",
            questionTitle = "1) Family History",
            answers = listOf("Yes", "No")
        ),
        SurveyModel(
            questionType = QuestionType.TEXT,
            questionId = "weight",
            questionDescription = "Enter your current body weight in Kg",
            questionTitle = "2) Weight",
        ),
        SurveyModel(
            questionType = QuestionType.TEXT,
            questionId = "bodyTemperature",
            questionDescription = "What is your usual average temperature?",
            questionTitle = "3) Average Temperature",
        ),
        SurveyModel(
            questionType = QuestionType.TEXT,
            questionId = "heartRate",
            questionDescription = "What is your average resting heart-rate?",
            questionTitle = "4) Average Resting Heart-Rate",
        ),
        SurveyModel(
            questionType = QuestionType.TEXT,
            questionId = "bloodOxygen",
            questionDescription = "What is your average Blood Oxygen Level?",
            questionTitle = "5) Blood Oxygen Level",
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "apoeE4",
            questionDescription = "Do you possess the APOE gene?",
            questionTitle = "6) APOE",
            answers = listOf("Yes", "No")
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "diabetic",
            questionDescription = "Have you been diagnosed with diabetes?",
            questionTitle = "7) Diabetes",
            answers = listOf("Yes", "No")
        ),
        SurveyModel(
            questionType = QuestionType.TEXT,
            questionId = "alcoholLevel",
            questionDescription = "Enter your average weekly alcohol consumption in units",
            questionTitle = "8) Alcohol Level",
        ),
        SurveyModel(
            questionType = QuestionType.TEXT,
            questionId = "bloodPressure",
            questionDescription = "Enter your average blood pressure (systolic)",
            questionTitle = "9) Blood Pressure",
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "hearingLoss",
            questionDescription = "Have you experienced hearing loss?",
            questionTitle = "10) Hearing Loss",
            answers = listOf("Yes", "No")
        )
    )

    fun isS2Complete(): Boolean {
        return familyHistory.value != -1 &&
                weight.value > -1f &&
                bodyTemperature.value > -1f &&
                heartRate.value > -1 &&
                bloodOxygen.value > -1f &&
                apoeE4.value != -1 &&
                diabetic.value != -1 &&
                alcoholLevel.value > -1f &&
                bloodPressure.value != -1 &&
                hearingLoss.value != -1
    }

    private val _s3visi = MutableStateFlow(false)
    val s3visi: StateFlow<Boolean> = _s3visi
    fun onS3Change(newVisi: Boolean){_s3visi.value = newVisi}

    private val _smoked = MutableStateFlow(-1)
    val smoked: MutableStateFlow<Int> = _smoked
    fun onSmokedChange(v: Int) { _smoked.value = v }

    private val _physicalActivity = MutableStateFlow("")
    val physicalActivity: StateFlow<String> = _physicalActivity
    fun onPhysicalActivityChange(v: String) { _physicalActivity.value = v }

    private val _depressionStatus = MutableStateFlow(-1)
    val depressionStatus: StateFlow<Int> = _depressionStatus
    fun onDepressionStatusChange(v: Int) { _depressionStatus.value = v }

    private val _nutritionDiet = MutableStateFlow("")
    val nutritionDiet: StateFlow<String> = _nutritionDiet
    fun onNutritionDietChange(v: String) { _nutritionDiet.value = v }

    private val _sleepQuality = MutableStateFlow(-1)
    val sleepQuality: StateFlow<Int> =_sleepQuality
    fun onSleepQualityChange(v: Int) { _sleepQuality.value = v }

    val activityOptions = listOf("Sedentary", "Lightly Active", "Moderately Active")
    val dietOptions = listOf("Balanced", "Low Carb", "Mediterranean")

    val s3Survey = listOf(
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "smoked",
            questionTitle = "1) Do you smoke",
            questionDescription = "Are you, or have you previously been a regular smoker",
            answers = listOf("Yes", "No")
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "sleepQuality",
            questionTitle = "2) Sleep Quality",
            questionDescription = "Do you usually get good sleep",
            answers = listOf("Yes", "No")
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "depressionStatus",
            questionTitle = "3) Depression",
            questionDescription = "Have you been diagnosed with depression",
            answers = listOf("Yes", "No")
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "physicalActivity",
            questionTitle = "4) Activity Level",
            questionDescription = "Choose your level of physical activity",
            answers = activityOptions
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "nutritionDiet",
            questionTitle = "5) Diet",
            questionDescription = "Choose what best describes your usual diet",
            answers = dietOptions
        ),
    )

    fun isS3Complete(): Boolean {
        return smoked.value != -1 &&
                sleepQuality.value != -1 &&
                depressionStatus.value != -1 &&
                physicalActivity.value.isNotEmpty() &&
                nutritionDiet.value.isNotEmpty()
    }

    fun onSurveyAnswerChange(answers: Map<String, Set<String>>) {
        answers.forEach { (questionId, answerSet) ->
            val answer = answerSet.firstOrNull() ?: return@forEach
            val yesNoToInt = if (answer.equals("Yes", ignoreCase = true)) 1 else 0

            when (questionId) {
                "gender" -> onGenderChange(if (answer == "Male") 1 else 0)
                "dominantHand" -> onDominantHandChange(if (answer == "Right Handed") 1 else 0)
                "education" -> onEducationChange(answer)
                "age" -> onAgeChange(answer.toIntOrNull() ?: 0)

                "familyHistory" -> onFamilyHistoryChange(yesNoToInt)
                "apoeE4" -> onApoeE4Change(yesNoToInt)
                "weight" -> onWeightChange(answer.toFloatOrNull() ?: 0f)
                "bodyTemperature" -> onBodyTemperatureChange(answer.toFloatOrNull() ?: 0f)
                "heartRate" -> onHeartRateChange(answer.toIntOrNull() ?: 0)
                "bloodOxygen" -> onBloodOxygenChange(answer.toFloatOrNull() ?: 0f)
                "diabetic" -> onDiabeticChange(yesNoToInt)
                "alcoholLevel" -> onAlcoholLevelChange(answer.toFloatOrNull() ?: 0f)
                "bloodPressure" -> onBloodPressureChange(answer.toIntOrNull() ?: 0)
                "hearingLoss" -> onHearingLossChange(yesNoToInt)

                "smoked" -> onSmokedChange(yesNoToInt)
                "sleepQuality" -> onSleepQualityChange(yesNoToInt)
                "depressionStatus" -> onDepressionStatusChange(yesNoToInt)
                "physicalActivity" -> onPhysicalActivityChange(answer)
                "nutritionDiet" -> onNutritionDietChange(answer)
            }
        }
    }

    fun submitAnswers(id: Int){
        viewModelScope.launch {
            isLoading = true

            val result = repository.reportQuestionnaire(
                LifestyleRequest(
                    patientID = id,
                    gender = _gender.value,
                    age = _age.value,
                    dominantHand = _dominantHand.value,
                    weight = _weight.value,
                    bodyTemperature = _bodyTemperature.value,
                    heartRate = _heartRate.value,
                    bloodOxygen = _bloodOxygen.value,
                    familyHistory = _familyHistory.value,
                    smoked = _smoked.value,
                    apoeE4 = _apoeE4.value,
                    physicalActivity = _physicalActivity.value,
                    depressionStatus = _depressionStatus.value,
                    nutritionDiet = _nutritionDiet.value,
                    sleepQuality = _sleepQuality.value,
                    cumulativePrimary = _cumulativePrimary.value,
                    cumulativeSecondary = _cumulativeSecondary.value,
                    cumulativeDegree = _cumulativeDegree.value,
                    diabetic = _diabetic.value,
                    alcoholLevel = alcoholLevel.value,
                    bloodPressure = bloodPressure.value,
                    hearingLoss = hearingLoss.value,
                )
            )

            when (result) {
                is TestResult.Success -> {
                    onS3Change(false)
                    onSuccessChange(true)
                }
                is TestResult.Unauthorized -> {}
                is TestResult.UnknownError -> {}
            }

            resultChannel.trySend(result)
            isLoading = false
        }
    }

    private val _successVisi = MutableStateFlow(false)
    val successVisi: StateFlow<Boolean> = _successVisi
    private fun onSuccessChange(newVisi: Boolean){_successVisi.value = newVisi}
}
