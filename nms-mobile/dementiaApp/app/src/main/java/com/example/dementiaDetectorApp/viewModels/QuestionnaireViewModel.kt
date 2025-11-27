package com.example.dementiaDetectorApp.viewModels

import androidx.lifecycle.ViewModel
import com.zekierciyas.library.model.QuestionType
import com.zekierciyas.library.model.SurveyModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class QViewModel: ViewModel(){
    //Preface visibility
    private val _prefaceVisi = MutableStateFlow(true)
    val prefaceVisi: StateFlow<Boolean> = _prefaceVisi
    fun onVisiChange(newVisi: Boolean){_prefaceVisi.value = newVisi}

    //Section 1
    private val _s1visi = MutableStateFlow(false)
    val s1visi: StateFlow<Boolean> = _s1visi
    fun onS1Change(newVisi: Boolean){_s1visi.value = newVisi}

    private val _gender = MutableStateFlow(0) //0 = Female 1 = Male
    val gender: StateFlow<Int> = _gender
    val genderOptions = listOf("Male", "Female")
    fun onGenderChange(newGender: Int){_gender.value=newGender}

    private val _age = MutableStateFlow(0)
    val age: StateFlow<Int> = _age
    fun onAgeChange(newAge: Int) { _age.value = newAge }

    private val _dHand = MutableStateFlow(0) // Left = 0 Right = 1
    val dHand: StateFlow<Int> = _dHand
    val dHandOptions= listOf("Left Handed", "Right Handed")
    fun onDHandChange(newDHand: Int) { _dHand.value = newDHand }

    private val _edu = MutableStateFlow("")
    val edu: StateFlow<String> = _edu
    fun onEduChange(newEdu: String) { _edu.value = newEdu }
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

    //Section 2
    private val _s2visi = MutableStateFlow(false)
    val s2visi: StateFlow<Boolean> = _s2visi
    fun onS2Change(newVisi: Boolean){_s2visi.value = newVisi}

    private val _weight = MutableStateFlow(0.0F)
    val weight: StateFlow<Float> = _weight
    fun onWeightChange(newWeight: Float){_weight.value = newWeight}

    private val _avgTemp = MutableStateFlow(0.0F)
    val avgTemp: StateFlow<Float> = _avgTemp
    fun onAvgTempChange(newAvgTemp: Float) { _avgTemp.value = newAvgTemp }

    private val _restingHR = MutableStateFlow(0)
    val restingHR: MutableStateFlow<Int> = _restingHR
    fun onRestingHRChange(newHR: Int) { _restingHR.value = newHR }

    private val _oxLv = MutableStateFlow(0)
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
            questionDescription = "Do you usally get good (8+ hours) of sleep",
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
}