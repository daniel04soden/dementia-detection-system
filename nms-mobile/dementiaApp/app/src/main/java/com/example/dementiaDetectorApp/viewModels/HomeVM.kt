package com.example.dementiaDetectorApp.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dementiaDetectorApp.api.feedback.FeedbackRepo
import com.example.dementiaDetectorApp.api.feedback.FeedbackResult
import com.example.dementiaDetectorApp.api.news.NewsRepo
import com.example.dementiaDetectorApp.api.news.NewsResult
import com.example.dementiaDetectorApp.models.NewsPiece
import com.example.dementiaDetectorApp.models.Test
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeVM @Inject constructor(
    private val newsRepo: NewsRepo,
    private val feedbackRepo: FeedbackRepo,
    @ApplicationContext private val context: Context
): ViewModel(){
    private val newsResChannel = Channel<NewsResult<Unit>>()
    val newsResults = newsResChannel.receiveAsFlow()

    private val feedbackResChannel = Channel<FeedbackResult<Unit>>()
    val feedbackResults = feedbackResChannel.receiveAsFlow()

    private var isLoading = false

    private val _fName = mutableStateOf("")
    val fName: State<String> = _fName

    //News Pieces
    private val _news = mutableStateOf<List<NewsPiece>>(emptyList())
    val news: State<List<NewsPiece>> = _news

    private fun getNews(){
        viewModelScope.launch{
            isLoading=true
            _news.value = newsRepo.getNews().data?: emptyList()
        }
    }

    //Ratings
    private val _rating = mutableIntStateOf(0)
    val rating: State<Int> = _rating
    fun onRatingChange(newRating: Int){_rating.intValue = newRating}

    private val _feedback = mutableStateOf("")
    val feedback: State<String> = _feedback
    fun onFeedbackChange(newFeedback:String){_feedback.value = newFeedback}

    private val _feedbackVisi = mutableStateOf(false)
    val feedbackVisi: State<Boolean> = _feedbackVisi
    fun onFeedbackVisiChange(){_feedbackVisi.value = !_feedbackVisi.value}

    private val _testsDone = mutableIntStateOf(0)

    fun countTestsDone(tests:List<Test>){
        var count=0
        for (test in tests){
            if (test.state!=0){
                count++
            }
        }
        _testsDone.intValue=count
        Log.d("Test count", "${_testsDone.intValue}")
    }

    private fun checkReviewConditions(){
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val reviewAsked = prefs.getBoolean("review_asked", false)
        Log.d("RevAsked", "$reviewAsked")
        if (_testsDone.intValue>-2 && !reviewAsked){onFeedbackVisiChange()}
    }

    fun submitReview(id:Int){
        viewModelScope.launch{
            isLoading=true
            val result = feedbackRepo.submitReview(
                id = id.toString(),
                score = _rating.intValue,
                critique = _feedback.value,
            )
            if (result is FeedbackResult.Authorized){
                onFeedbackVisiChange()
                val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                prefs.edit {
                    putBoolean("review_asked", true)
                    apply()
                    checkReviewConditions()
                }
                Log.d("Review submit", "Prefs edited")
            }
        }
    }

    init {
        getNews()
    }
}