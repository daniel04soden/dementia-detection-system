package com.example.dementiaDetectorApp.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dementiaDetectorApp.viewModels.QViewModel
import kotlinx.coroutines.selects.select

@Composable
fun QuestionnaireScreen(viewModel: QViewModel){
    //Value variables
    val gender by viewModel.gender.collectAsState()
    val age by viewModel.age.collectAsState()
    val dHand by viewModel.dHand.collectAsState()
    val weight by viewModel.weight.collectAsState()
    val avgTemp by viewModel.avgTemp.collectAsState()
    val restingHR by viewModel.restingHR.collectAsState()
    val oxLv by viewModel.oxLv.collectAsState()
    val history by viewModel.history.collectAsState()
    val smoke by viewModel.smoke.collectAsState()
    val apoe by viewModel.apoe.collectAsState()
    val activityLv by viewModel.activityLv.collectAsState()
    val depressed by viewModel.depressed.collectAsState()
    val diet by viewModel.diet.collectAsState()
    val goodSleep by viewModel.goodSleep.collectAsState()
    val edu by viewModel.edu.collectAsState()

    //Visbility variables
    var genderQVisible by remember { mutableStateOf(true) }
    var ageQVisible by remember { mutableStateOf(true) }
    var dHandQVisible by remember { mutableStateOf(true) }
    var weightQVisible by remember { mutableStateOf(true) }
    var avgTempQVisible by remember { mutableStateOf(true) }
    var restingHRQVisible by remember { mutableStateOf(true) }
    var oxLvQVisible by remember { mutableStateOf(true) }
    var historyQVisible by remember { mutableStateOf(true) }
    var smokeQVisible by remember { mutableStateOf(true) }
    var apoeQVisible by remember { mutableStateOf(true) }
    var activityLvQVisible by remember { mutableStateOf(true) }
    var depressedQVisible by remember { mutableStateOf(true) }
    var dietQVisible by remember { mutableStateOf(true) }
    var goodSleepQVisible by remember { mutableStateOf(true) }
    var eduQVisible by remember { mutableStateOf(true) }


    Scaffold(
        topBar = {
            Box(modifier = Modifier
                .fillMaxHeight(0.1F)
                .fillMaxWidth()
                .padding(WindowInsets.statusBars.asPaddingValues())
            ){
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(color = Color.Magenta),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically)
                {
                    Text("Lifestyle Questionnaire", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        },
        //bottomBar = {}
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize(1F)
                .background(color = Color.White)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.05F))
                Column(
                    modifier = Modifier
                        .fillMaxHeight(0.9F)
                        .fillMaxWidth(0.86F)
                ) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = genderQVisible,
                        enter = slideInHorizontally() + fadeIn(),
                        exit = slideOutHorizontally() + fadeOut()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.LightGray),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.1F)
                                    .background(color = Color.Green),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            )
                            {
                                Text(
                                    "Gender",
                                    modifier = Modifier
                                        .padding(start = Dp(15F)),
                                    color = Color.Magenta,
                                    fontSize = 20.sp
                                )
                            }
                            Spacer(modifier = Modifier.fillMaxHeight(0.05F))
                            val genderOptions = listOf("Male", "Female")
                            val (genderVal, onOptionSelected) = remember {
                                mutableStateOf(
                                    genderOptions[0]
                                )
                            }
                            genderOptions.forEach { text ->
                                Row(
                                    modifier = Modifier
                                        .height(56.dp)
                                        .fillMaxWidth(1F)
                                        .padding(horizontal = 16.dp)
                                        .selectable(
                                            selected = (text == genderVal),
                                            role = Role.RadioButton,
                                            onClick = { onOptionSelected(text) }
                                        ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = (text == genderVal),
                                        onClick = null
                                    )
                                    Text(
                                        text = text,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.fillMaxHeight(0.7F))
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth(0.5F),
                                onClick = {
                                    genderQVisible = false
                                    if (genderVal == "Male") {
                                        viewModel.onGenderChange(1)
                                    } else {
                                        viewModel.onGenderChange(0)
                                    }
                                }
                            ) {
                                Text("Next Question")
                            }
                        }
                    }
                    androidx.compose.animation.AnimatedVisibility(
                        visible = ageQVisible,
                        enter = slideInHorizontally() + fadeIn(),
                        exit = slideOutHorizontally() + fadeOut()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.LightGray),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.1F)
                                    .background(color = Color.Green),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            )
                            {
                                Text(
                                    "Age",
                                    modifier = Modifier
                                        .padding(start = Dp(15F)),
                                    color = Color.Magenta,
                                    fontSize = 20.sp
                                )
                            }
                            Spacer(modifier = Modifier.fillMaxHeight(0.05F))
                            TextField(
                                modifier = Modifier
                                    .fillMaxHeight(1F)
                                    .weight(0.45F),
                                value = age.toString(),
                                onValueChange = {viewModel.onAgeChange(it.toInt())},
                                label = { Text(text = "Age", fontSize = 18.sp) },
                                textStyle = TextStyle(fontSize = 16.sp),
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.White
                                )
                            )

                            Spacer(modifier = Modifier.fillMaxHeight(0.7F))
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth(0.5F),
                                onClick = {
                                    genderQVisible = false
                                    if ("a" != "") {
                                        viewModel.onAgeChange(1)
                                    }
                                }
                            ) {
                                Text("Next Question")
                            }
                        }
                    }
                }
            }
        }
    }
}