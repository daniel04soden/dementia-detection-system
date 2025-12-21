package com.example.dementiaDetectorApp.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
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

    private var isLoading = false

    // Preface
    private val _prefaceVisi = MutableStateFlow(true)
    val prefaceVisi: StateFlow<Boolean> = _prefaceVisi
    fun onVisiChange(newVisi: Boolean){_prefaceVisi.value = newVisi}

    // ---------------------- S1: General Questions ----------------------
    private val _s1visi = MutableStateFlow(false)
    val s1visi: StateFlow<Boolean> = _s1visi
    fun onS1Change(newVisi: Boolean){_s1visi.value = newVisi}

    private val _gender = MutableStateFlow(-1)
    private val gender: StateFlow<Int> = _gender
    private fun onGenderChange(newGender: Int){
        _gender.value=newGender
        isS1Complete()
    }

    private val _age = MutableStateFlow(0)
    private val age: StateFlow<Int> = _age
    private fun onAgeChange(newAge: Int) {
        _age.value = newAge
        isS1Complete()
    }

    private val _dominantHand = MutableStateFlow(-1)
    private val dominantHand: StateFlow<Int> = _dominantHand
    private fun onDominantHandChange(newValue: Int) {
        _dominantHand.value = newValue
        isS1Complete()
    }

    private val _education = MutableStateFlow("")
    private val education: StateFlow<String> = _education
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
        isS1Complete()
    }

    private val _cumulativePrimary = MutableStateFlow("")
    private fun onPrimaryChange(change:Boolean){_cumulativePrimary.value = change.toString().uppercase()}

    private val _cumulativeSecondary = MutableStateFlow("")
    private fun onSecondaryChange(change:Boolean){_cumulativeSecondary.value = change.toString().uppercase()}

    private val _cumulativeDegree = MutableStateFlow("")
    private fun onDegreeChange(change:Boolean){_cumulativeDegree.value = change.toString().uppercase()}

    private val _familyHistory = MutableStateFlow(-1)
    private val familyHistory: StateFlow<Int> = _familyHistory
    private fun onFamilyHistoryChange(v: Int) {
        _familyHistory.value = v
        isS1Complete()
    }

    private val eduOptions = listOf("Primary Level", "Secondary Level", "Tertiary Level")

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

    private val _s1Complete = mutableStateOf(false)
    val s1Complete:State<Boolean> = _s1Complete

    private fun isS1Complete(){
        _s1Complete.value = (
                gender.value != -1 &&
                age.value > 0 &&
                dominantHand.value != -1 &&
                education.value.isNotEmpty() &&
                familyHistory.value != -1
                )
    }

    // ---------------------- S2: Medical / Measurements ----------------------
    private val _s2visi = MutableStateFlow(false)
    val s2visi: StateFlow<Boolean> = _s2visi
    fun onS2Change(newVisi: Boolean){
        _s2visi.value = newVisi
        isS2Complete()
    }

    private val _weight = MutableStateFlow(-1.0F)
    private val weight: StateFlow<Float> = _weight
    private fun onWeightChange(newWeight: Float){
        _weight.value = newWeight
        isS2Complete()
    }

    private val _bodyTemperature = MutableStateFlow(-1.0F)
    private val bodyTemperature: StateFlow<Float> = _bodyTemperature
    private fun onBodyTemperatureChange(v: Float) {
        _bodyTemperature.value = v
        isS2Complete()
    }

    private val _heartRate = MutableStateFlow(-1)
    private val heartRate: MutableStateFlow<Int> = _heartRate
    private fun onHeartRateChange(v: Int) {
        _heartRate.value = v
        isS2Complete()
    }

    private val _bloodOxygen = MutableStateFlow(-1.0F)
    private val bloodOxygen: StateFlow<Float> = _bloodOxygen
    private fun onBloodOxygenChange(v: Float) {
        _bloodOxygen.value = v
        isS2Complete()
    }

    private val _apoeE4 = MutableStateFlow(-1)
    private val apoeE4: StateFlow<Int> = _apoeE4
    private fun onApoeE4Change(v: Int) {
        _apoeE4.value = v
        isS2Complete()
    }

    private val _diabetic = MutableStateFlow(-1)
    private val diabetic: StateFlow<Int> = _diabetic
    private fun onDiabeticChange(value: Int) {
        _diabetic.value = value
        isS2Complete()
    }

    private val _alcoholLevel = MutableStateFlow(-1.0f)
    private val alcoholLevel: StateFlow<Float> = _alcoholLevel
    private fun onAlcoholLevelChange(value: Float) {
        _alcoholLevel.value = value
        isS2Complete()
    }

    private val _bloodPressure = MutableStateFlow(-1)
    private val bloodPressure: StateFlow<Int> = _bloodPressure
    private fun onBloodPressureChange(value: Int) {
        _bloodPressure.value = value
        isS2Complete()
    }

    private val _hearingLoss = MutableStateFlow(-1)
    private val hearingLoss: StateFlow<Int> = _hearingLoss
    private fun onHearingLossChange(value: Int) { _hearingLoss.value = value }

    private val _mriDelay = MutableStateFlow(-1.0f)
    private val mriDelay: StateFlow<Float> = _mriDelay
    private fun onMRIDelayChange(value: Float){
        _mriDelay.value = value
        isS2Complete()
    }

    private val _cognitiveTestScores = MutableStateFlow(-1)
    private val cognitiveTestScores: StateFlow<Int> = _cognitiveTestScores
    private fun onCognitiveTestScoresChange(v: Int){
        _cognitiveTestScores.value = v
        isS2Complete()
    }

    private val _medicationHistory = MutableStateFlow(-1)
    private val medicationHistory: StateFlow<Int> = _medicationHistory
    private fun onMedicationHistoryChange(v: Int){
        _medicationHistory.value = v
        isS2Complete()
    }

    private val _chronicHealthConditions = MutableStateFlow("")
    private val chronicHealthConditions: StateFlow<String> = _chronicHealthConditions
    private fun onChronicHealthConditionsChange(v: String){
        _chronicHealthConditions.value = v
        isS2Complete()
    }

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

    private val _s2Complete = mutableStateOf(false)
    val s2Complete:State<Boolean> = _s2Complete

    private fun isS2Complete(){
        _s2Complete.value = (
                weight.value > -1f &&
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
                )
    }

    // ---------------------- S3: Hobbies / Lifestyle ----------------------
    private val _s3visi = MutableStateFlow(false)
    val s3visi: StateFlow<Boolean> = _s3visi
    fun onS3Change(newVisi: Boolean){
        _s3visi.value = newVisi
        isS3Complete()
    }

    private val _smoked = MutableStateFlow(-1)
    private val smoked: MutableStateFlow<Int> = _smoked
    private fun onSmokedChange(v: Int) {
        _smoked.value = v
        isS3Complete()
    }

    private val _physicalActivity = MutableStateFlow("")
    private val physicalActivity: StateFlow<String> = _physicalActivity
    private fun onPhysicalActivityChange(v: String) {
        _physicalActivity.value = v
        isS3Complete()
    }

    private val _depressionStatus = MutableStateFlow(-1)
    private val depressionStatus: StateFlow<Int> = _depressionStatus
    private fun onDepressionStatusChange(v: Int) {
        _depressionStatus.value = v
        isS3Complete()
    }

    private val _nutritionDiet = MutableStateFlow("")
    private val nutritionDiet: StateFlow<String> = _nutritionDiet
    private fun onNutritionDietChange(v: String) {
        _nutritionDiet.value = v
        isS3Complete()
    }

    private val _sleepQuality = MutableStateFlow(-1)
    private val sleepQuality: StateFlow<Int> =_sleepQuality
    private fun onSleepQualityChange(v: Int) {
        _sleepQuality.value = v
        isS3Complete()
    }

    private val _dementiaStatus = MutableStateFlow("")
    private val dementiaStatus: StateFlow<String> = _dementiaStatus
    private fun onDementiaStatusChange(v: String){
        _dementiaStatus.value = v
        isS3Complete()
    }

    private val activityOptions = listOf("Sedentary", "Lightly Active", "Moderately Active")
    private val dietOptions = listOf("Balanced", "Low Carb", "Mediterranean")
    private val dementiaOptions = listOf("No Dementia", "Mild", "Moderate", "Severe")

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

    private val _s3Complete = mutableStateOf(false)
    val s3Complete: State<Boolean> =_s3Complete

    private fun isS3Complete(){
        _s3Complete.value = (
                smoked.value != -1 &&
                sleepQuality.value != -1 &&
                depressionStatus.value != -1 &&
                physicalActivity.value.isNotEmpty() &&
                nutritionDiet.value.isNotEmpty() &&
                dementiaStatus.value.isNotEmpty()
        )
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
    fun onSuccessChange(newVisi: Boolean){_successVisi.value = newVisi}

    private val _paymentVisi = MutableStateFlow(false)
    val paymentVisi: StateFlow<Boolean> = _paymentVisi
    fun isPaymentReq(paid:Boolean){_paymentVisi.value = paid}


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
