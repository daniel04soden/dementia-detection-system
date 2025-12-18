package com.example.dementiaDetectorApp.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dementiaDetectorApp.api.news.NewsRepo
import com.example.dementiaDetectorApp.api.tests.TestResult
import com.example.dementiaDetectorApp.models.NewsPiece
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeVM @Inject constructor(
    private val repository: NewsRepo
): ViewModel(){
    private val resultChannel = Channel<TestResult<Unit>>()
    val testResults = resultChannel.receiveAsFlow()

    var isLoading = false
        private set

    private val _fName = mutableStateOf("")
    val fName: State<String> = _fName

    //News Pieces
    private val _news = mutableStateOf<List<NewsPiece>>(emptyList())
    val news: State<List<NewsPiece>> = _news

    private fun getNews(){
        viewModelScope.launch{
            isLoading=true
            _news.value = repository.getNews().data?: emptyList()
        }
    }
    
    init {
        getNews()
    }
}