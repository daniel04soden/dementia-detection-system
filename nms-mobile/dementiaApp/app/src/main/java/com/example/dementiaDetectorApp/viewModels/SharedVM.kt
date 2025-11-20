package com.example.dementiaDetectorApp.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.dementiaDetectorApp.models.Account
import com.example.dementiaDetectorApp.models.NavBarContent
import com.example.dementiaDetectorApp.models.Test

class SharedVM: ViewModel(){
    //User
    private val _account = mutableStateOf<Account>(Account("","","","","","",""))
    val account: State<Account> = _account

    //NavBar
    private val _navItems = mutableStateOf<List<NavBarContent>>(navList())
    val navItems: State<List<NavBarContent>> = _navItems

    private val _navIndex = mutableIntStateOf(0)
    val navIndex: State<Int> = _navIndex

    fun onNavIndexChange(newIDX: Int){_navIndex.intValue=newIDX}

    fun navList():List<NavBarContent> {
        val testNav = listOf(
            NavBarContent(
                title = "Home",
                iconId = 0,
                "home"
            ),

            NavBarContent(
                title = "Test Status",
                iconId = 0,
                "home"
            ),

            NavBarContent(
                title = "Risk Assessment",
                iconId = 0,
                "home"
            ),

            NavBarContent(
                title = "Contact",
                iconId = 0,
                "home"
            )
        )
        return testNav
    }

    //Tests
    private val _tests = mutableStateOf<List<Test>>(testList())
    val tests: State<List<Test>> = _tests

    fun testList():List<Test> {
        val testList = listOf(
            Test(
                name = "Lifestyle questionnaire",
                route = "questionnaire",
                step = 1
            ),

            Test(
                name = "GP Cognitive Test part 1",
                route = "test1",
                step = 2
            ),

            Test(
                name = "GP Cognitive Test part 2",
                route = "test2",
                step = 3
            ),
        )
        return testList
    }

}