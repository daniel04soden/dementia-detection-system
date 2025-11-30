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

    // Preface
    private val _prefaceVisi = MutableStateFlow(true)
    val prefaceVisi: StateFlow<Boolean> = _prefaceVisi
    fun onVisiChange(newVisi: Boolean){_prefaceVisi.value = newVisi}

    // ---------------------- S1: General Questions ----------------------
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
            onSecondaryChange(true)
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

    private val _familyHistory = MutableStateFlow(-1)
    val familyHistory: StateFlow<Int> = _familyHistory
    fun onFamilyHistoryChange(v: Int) { _familyHistory.value = v }

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
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "familyHistory",
            questionTitle = "5) Family History",
            questionDescription = "Does your family have a history of dementia?",
            answers = listOf("Yes", "No")
        )
    )

    fun isS1Complete(): Boolean {
        return gender.value != -1 &&
                age.value > 0 &&
                dominantHand.value != -1 &&
                education.value.isNotEmpty() &&
                familyHistory.value != -1
    }

    // ---------------------- S2: Medical / Measurements ----------------------
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

    private val _mriDelay = MutableStateFlow(-1.0f)
    val mriDelay: StateFlow<Float> = _mriDelay
    fun onMRIDelayChange(value: Float){ _mriDelay.value = value }

    private val _cognitiveTestScores = MutableStateFlow(-1)
    val cognitiveTestScores: StateFlow<Int> = _cognitiveTestScores
    fun onCognitiveTestScoresChange(v: Int){ _cognitiveTestScores.value = v }

    private val _medicationHistory = MutableStateFlow(-1)
    val medicationHistory: StateFlow<Int> = _medicationHistory
    fun onMedicationHistoryChange(v: Int){ _medicationHistory.value = v }

    private val _chronicHealthConditions = MutableStateFlow("")
    val chronicHealthConditions: StateFlow<String> = _chronicHealthConditions
    fun onChronicHealthConditionsChange(v: String){ _chronicHealthConditions.value = v }

    val s2Survey = listOf(
        SurveyModel(
            questionType = QuestionType.TEXT,
            questionId = "weight",
            questionTitle = "1) Weight",
            questionDescription = "Enter your current body weight in Kg"
        ),
        SurveyModel(
            questionType = QuestionType.TEXT,
            questionId = "bodyTemperature",
            questionTitle = "2) Average Body Temperature",
            questionDescription = "Enter your average body temperature"
        ),
        SurveyModel(
            questionType = QuestionType.TEXT,
            questionId = "heartRate",
            questionTitle = "3) Resting Heart Rate",
            questionDescription = "Enter your average resting heart rate"
        ),
        SurveyModel(
            questionType = QuestionType.TEXT,
            questionId = "bloodOxygen",
            questionTitle = "4) Blood Oxygen Level",
            questionDescription = "Enter your average blood oxygen level"
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "apoeE4",
            questionTitle = "5) APOE Gene",
            questionDescription = "Do you possess the APOE gene?",
            answers = listOf("Yes", "No")
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "diabetic",
            questionTitle = "6) Diabetes",
            questionDescription = "Have you been diagnosed with diabetes?",
            answers = listOf("Yes", "No")
        ),
        SurveyModel(
            questionType = QuestionType.TEXT,
            questionId = "alcoholLevel",
            questionTitle = "7) Alcohol Consumption",
            questionDescription = "Enter your average weekly alcohol consumption in units"
        ),
        SurveyModel(
            questionType = QuestionType.TEXT,
            questionId = "bloodPressure",
            questionTitle = "8) Blood Pressure",
            questionDescription = "Enter your average blood pressure (systolic)"
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "hearingLoss",
            questionTitle = "9) Hearing Loss",
            questionDescription = "Have you experienced hearing loss?",
            answers = listOf("Yes", "No")
        ),
        SurveyModel(
            questionType = QuestionType.TEXT,
            questionId = "mriDelay",
            questionTitle = "10) MRI Delay",
            questionDescription = "Enter average delay between MRI scans (months)"
        ),
        SurveyModel(
            questionType = QuestionType.TEXT,
            questionId = "cognitiveTestScores",
            questionTitle = "11) Cognitive Test Score",
            questionDescription = "Enter latest cognitive test score (e.g., MMSE)"
        ),
        SurveyModel(
            questionType = QuestionType.TEXT,
            questionId = "medicationHistory",
            questionTitle = "12) Medication History",
            questionDescription = "Enter the number of current medications"
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "chronicHealthConditions",
            questionTitle = "13) Chronic Health Conditions",
            questionDescription = "Do you have any of the following chronic health conditions",
            answers = listOf("Hypertension", "Heart disease", "Diabetes", "N/A")
        )
    )

    fun isS2Complete(): Boolean {
        return weight.value > -1f &&
                bodyTemperature.value > -1f &&
                heartRate.value > -1 &&
                bloodOxygen.value > -1f &&
                apoeE4.value != -1 &&
                diabetic.value != -1 &&
                alcoholLevel.value > -1f &&
                bloodPressure.value != -1 &&
                hearingLoss.value != -1 &&
                mriDelay.value > -1f &&
                cognitiveTestScores.value > -1 &&
                medicationHistory.value > -1 &&
                chronicHealthConditions.value != ""
    }

    // ---------------------- S3: Hobbies / Lifestyle ----------------------
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

    private val _dementiaStatus = MutableStateFlow("")
    val dementiaStatus: StateFlow<String> = _dementiaStatus
    fun onDementiaStatusChange(v: String){ _dementiaStatus.value = v }

    val activityOptions = listOf("Sedentary", "Lightly Active", "Moderately Active")
    val dietOptions = listOf("Balanced", "Low Carb", "Mediterranean")
    val dementiaOptions = listOf("No Dementia", "Mild", "Moderate", "Severe")

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
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "dementiaStatus",
            questionTitle = "6) Dementia Status",
            questionDescription = "Select your current dementia status",
            answers = dementiaOptions
        )
    )

    fun isS3Complete(): Boolean {
        return smoked.value != -1 &&
                sleepQuality.value != -1 &&
                depressionStatus.value != -1 &&
                physicalActivity.value.isNotEmpty() &&
                nutritionDiet.value.isNotEmpty() &&
                dementiaStatus.value.isNotEmpty()
    }

    // ---------------------- Survey Answer Handling ----------------------
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

                "weight" -> onWeightChange(answer.toFloatOrNull() ?: 0f)
                "bodyTemperature" -> onBodyTemperatureChange(answer.toFloatOrNull() ?: 0f)
                "heartRate" -> onHeartRateChange(answer.toIntOrNull() ?: 0)
                "bloodOxygen" -> onBloodOxygenChange(answer.toFloatOrNull() ?: 0f)
                "apoeE4" -> onApoeE4Change(yesNoToInt)
                "diabetic" -> onDiabeticChange(yesNoToInt)
                "alcoholLevel" -> onAlcoholLevelChange(answer.toFloatOrNull() ?: 0f)
                "bloodPressure" -> onBloodPressureChange(answer.toIntOrNull() ?: 0)
                "hearingLoss" -> onHearingLossChange(yesNoToInt)
                "mriDelay" -> onMRIDelayChange(answer.toFloatOrNull() ?: 0f)
                "cognitiveTestScores" -> onCognitiveTestScoresChange(answer.toIntOrNull() ?: 0)
                "medicationHistory" -> onMedicationHistoryChange(answer.toIntOrNull() ?: 0)
                "chronicHealthConditions" -> onChronicHealthConditionsChange(answer)

                "smoked" -> onSmokedChange(yesNoToInt)
                "sleepQuality" -> onSleepQualityChange(yesNoToInt)
                "depressionStatus" -> onDepressionStatusChange(yesNoToInt)
                "physicalActivity" -> onPhysicalActivityChange(answer)
                "nutritionDiet" -> onNutritionDietChange(answer)
                "dementiaStatus" -> onDementiaStatusChange(answer)
            }
        }
    }

    private val _successVisi = MutableStateFlow(false)
    val successVisi: StateFlow<Boolean> = _successVisi
    private fun onSuccessChange(newVisi: Boolean){_successVisi.value = newVisi}


    fun submitAnswers(id: Int){
        val request = LifestyleRequest(
            patientID = id,

            age = age.value,
            gender = gender.value,
            dominantHand = dominantHand.value,
            familyHistory = familyHistory.value,

            weight = weight.value,
            bodyTemperature = bodyTemperature.value,
            heartRate = heartRate.value,
            bloodOxygen = bloodOxygen.value,
            apoeE4 = apoeE4.value,
            diabetic = diabetic.value,
            alcoholLevel = alcoholLevel.value,
            bloodPressure = bloodPressure.value,
            hearingLoss = hearingLoss.value,
            mriDelay = mriDelay.value,
            cognitiveTestScores = cognitiveTestScores.value,
            medicationHistory = medicationHistory.value,
            chronicHealthConditions = chronicHealthConditions.value,

            smoked = smoked.value,
            sleepQuality = sleepQuality.value,
            depressionStatus = depressionStatus.value,
            physicalActivity = physicalActivity.value,
            nutritionDiet = nutritionDiet.value,
            dementiaStatus = dementiaStatus.value,

            cumulativePrimary = _cumulativePrimary.value,
            cumulativeSecondary = _cumulativeSecondary.value,
            cumulativeDegree = _cumulativeDegree.value
        )

        viewModelScope.launch {
            isLoading = true
            try {
                val result = repository.reportQuestionnaire(request)
                resultChannel.send(result)
            } catch (e: Exception) {
                Log.e("QViewModel", "submitAnswers: ", e)
            }
            isLoading = false
        }
    }
}
