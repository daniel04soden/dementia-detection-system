package com.example.dementiaDetectorApp.ui.views

import android.widget.Toast
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dementiaDetectorApp.viewModels.QViewModel

@Composable
fun QuestionnaireScreen(navController: NavController, viewModel: QViewModel) {
    val gender by viewModel.gender.collectAsState()
    val age by viewModel.age.collectAsState()
    val dHand by viewModel.dHand.collectAsState()
    val edu by viewModel.edu.collectAsState()
    val weight by viewModel.weight.collectAsState()
    val avgTemp by viewModel.avgTemp.collectAsState()
    val restingHR by viewModel.restingHR.collectAsState()
    val oxLv by viewModel.oxLv.collectAsState()
    val history by viewModel.history.collectAsState()
    val apoe by viewModel.apoe.collectAsState()
    val smoke by viewModel.smoke.collectAsState()
    val activityLv by viewModel.activityLv.collectAsState()
    val depressed by viewModel.depressed.collectAsState()
    val diet by viewModel.diet.collectAsState()
    val goodSleep by viewModel.goodSleep.collectAsState()

    var group1Visible by remember { mutableStateOf(true) }
    var group2Visible by remember { mutableStateOf(false) }
    var group3Visible by remember { mutableStateOf(false) }

    val genderOptions = listOf("Male", "Female")
    val dHandOptions = listOf("Left", "Right")
    val educationOptions = listOf(
        "No formal education", "Some primary education", "Completed primary education",
        "Some secondary education", "Completed secondary education", "Some college/university",
        "Associate degree", "Bachelor's degree", "Some postgraduate education",
        "Master's degree", "Professional degree", "Doctorate"
    )
    val yesNoOptions = listOf("No", "Yes")
    val activityOptions = listOf("Sedentary", "Lightly Active", "Moderately Active", "Very Active", "Extra Active")
    val dietOptions = listOf("Standard", "Vegetarian", "Vegan", "Low Carb", "Mediterranean", "Other")

    val context = LocalContext.current

    Scaffold(
        topBar = {
            Box(
                Modifier
                    .fillMaxHeight(0.1f)
                    .fillMaxWidth()
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .background(Color.Magenta)
            ) {
                Row(
                    Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Lifestyle Questionnaire",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding),
        ) {
            Column(
                Modifier.align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.fillMaxHeight(0.05f))

                androidx.compose.animation.AnimatedVisibility(
                    visible = group1Visible,
                    enter = slideInHorizontally() + fadeIn(),
                    exit = slideOutHorizontally() + fadeOut()
                ) {
                    var genderVal by remember { mutableStateOf(genderOptions[if (gender == 1) 0 else 1]) }
                    var dHandVal by remember { mutableStateOf(dHandOptions[dHand]) }
                    var selectedEducation by remember { mutableStateOf(educationOptions.getOrElse(educationOptions.indexOf(edu)) { educationOptions[0] }) }
                    var expanded by remember { mutableStateOf(false) }
                    var textFieldSize by remember { mutableStateOf(IntSize.Zero) }

                    Column(
                        Modifier.fillMaxSize().background(Color.LightGray),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            Modifier.fillMaxWidth().height(56.dp).background(Color.Green),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("General Questions", Modifier.padding(start = 15.dp), color = Color.Magenta, fontSize = 20.sp)
                        }
                        Spacer(Modifier.height(25.dp))
                        Row(
                            Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Gender: ", fontSize = 20.sp, modifier = Modifier.weight(0.4f))
                            Spacer(Modifier.weight(0.1f))
                            genderOptions.forEach { text ->
                                Column(
                                    Modifier.weight(0.30f).selectable(selected = (text == genderVal), role = Role.RadioButton, onClick = { genderVal = text }),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    RadioButton(selected = (text == genderVal), onClick = null)
                                    Text(text)
                                }
                            }
                        }
                        Spacer(Modifier.height(25.dp))
                        Row(
                            Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Age: ", fontSize = 20.sp, modifier = Modifier.weight(0.4f))
                            Spacer(Modifier.weight(0.1f))
                            TextField(
                                value = age.toString(),
                                onValueChange = { it.toIntOrNull()?.let(viewModel::onAgeChange) },
                                label = { Text("Age", fontSize = 18.sp) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxHeight().fillMaxWidth(0.6f),
                                colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.White),
                                textStyle = TextStyle(fontSize = 16.sp)
                            )
                        }
                        Spacer(Modifier.height(25.dp))
                        Row(
                            Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Dominant Hand: ", fontSize = 20.sp, modifier = Modifier.weight(0.4f))
                            Spacer(Modifier.weight(0.1f))
                            dHandOptions.forEach { text ->
                                Column(
                                    Modifier.weight(0.30f).selectable(selected = (text == dHandVal), role = Role.RadioButton, onClick = { dHandVal = text }),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    RadioButton(selected = (text == dHandVal), onClick = null)
                                    Text(text)
                                }
                            }
                        }
                        Spacer(Modifier.height(25.dp))
                        Row(
                            Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Education Level: ", fontSize = 20.sp, modifier = Modifier.weight(0.4f))
                            Spacer(Modifier.width(8.dp))
                            Box(Modifier.weight(0.6f)) {
                                TextField(
                                    value = selectedEducation,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Select") },
                                    trailingIcon = {
                                        Icon(
                                            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                                            contentDescription = null,
                                            modifier = Modifier.clickable { expanded = !expanded }
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth().onGloballyPositioned { textFieldSize = it.size }.clickable { expanded = !expanded }
                                )
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                                ) {
                                    educationOptions.forEach { option ->
                                        DropdownMenuItem(text = { Text(option) },onClick = {
                                            selectedEducation = option
                                            expanded = false
                                            viewModel.onEduChange(option)
                                        })
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.fillMaxHeight(0.7F))
                        Button(
                            modifier = Modifier.fillMaxWidth(0.5f),
                            onClick = {
                                group1Visible = false
                                group2Visible = true
                                viewModel.onGenderChange(if (genderVal == "Male") 1 else 0)
                                viewModel.onDHandChange(if (dHandVal == "Right") 1 else 0)
                                viewModel.onEduChange(selectedEducation)
                            }
                        ) { Text("Next: Measurements") }
                    }
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = group2Visible,
                    enter = slideInHorizontally() + fadeIn(),
                    exit = slideOutHorizontally() + fadeOut()
                ) {
                    var historyVal by remember { mutableStateOf(if (history) "Yes" else "No") }
                    var apoeVal by remember { mutableStateOf(if (apoe) "Yes" else "No") }
                    Column(
                        Modifier.fillMaxSize().background(Color.LightGray).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            Modifier.fillMaxWidth().background(Color.Green).padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Measurements & GP Questions", fontSize = 20.sp, color = Color.Magenta)
                        }
                        Spacer(Modifier.height(25.dp))
                        TextField(
                            value = weight.toString(),
                            onValueChange = { it.toFloatOrNull()?.let(viewModel::onWeightChange) },
                            label = { Text("Weight (kg)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(25.dp))
                        TextField(
                            value = avgTemp.toString(),
                            onValueChange = { it.toFloatOrNull()?.let(viewModel::onAvgTempChange) },
                            label = { Text("Average Body Temperature (°C)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(25.dp))
                        TextField(
                            value = restingHR.toString(),
                            onValueChange = { it.toIntOrNull()?.let(viewModel::onRestingHRChange) },
                            label = { Text("Resting Heart Rate (BPM)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(25.dp))
                        TextField(
                            value = oxLv.toString(),
                            onValueChange = { it.toIntOrNull()?.let(viewModel::onOxLvChange) },
                            label = { Text("Oxygen Level (%)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(25.dp))
                        Text("Previous Medical History?")
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            yesNoOptions.forEach { text ->
                                Row(
                                    Modifier.selectable(selected = (text == historyVal), role = Role.RadioButton, onClick = {
                                        historyVal = text
                                        viewModel.onHistoryChange(text == "Yes")
                                    }).padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(selected = (text == historyVal), onClick = null)
                                    Text(text)
                                }
                            }
                        }
                        Spacer(Modifier.height(25.dp))
                        Text("APOE Gene Detected?")
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            yesNoOptions.forEach { text ->
                                Row(
                                    Modifier.selectable(selected = (text == apoeVal), role = Role.RadioButton, onClick = {
                                        apoeVal = text
                                        viewModel.onApoeChange(text == "Yes")
                                    }).padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(selected = (text == apoeVal), onClick = null)
                                    Text(text)
                                }
                            }
                        }
                        Spacer(Modifier.fillMaxHeight(0.7F))
                        Button(
                            modifier = Modifier.fillMaxWidth(0.5f),
                            onClick = {
                                group2Visible = false
                                group3Visible = true
                            }
                        ) { Text("Next: Lifestyle") }
                    }
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = group3Visible,
                    enter = slideInHorizontally() + fadeIn(),
                    exit = slideOutHorizontally() + fadeOut()
                ) {
                    var smokeVal by remember { mutableStateOf(if (smoke) "Yes" else "No") }
                    var depressedVal by remember { mutableStateOf(if (depressed) "Yes" else "No") }
                    var goodSleepVal by remember { mutableStateOf(if (goodSleep) "Yes" else "No") }
                    var activityExpanded by remember { mutableStateOf(false) }
                    var dietExpanded by remember { mutableStateOf(false) }
                    var selectedActivity by remember { mutableStateOf(activityLv.ifEmpty { activityOptions.first() }) }
                    var selectedDiet by remember { mutableStateOf(diet.ifEmpty { dietOptions.first() }) }

                    Column(
                        Modifier.fillMaxSize().background(Color.LightGray).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            Modifier.fillMaxWidth().background(Color.Green).padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Lifestyle & Habits", fontSize = 20.sp, color = Color.Magenta)
                        }
                        Spacer(Modifier.height(25.dp))
                        Text(
                            "Answer the questions below about your daily habits and lifestyle to help us understand your profile.",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(20.dp))
                        Text("Do you smoke?")
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            yesNoOptions.forEach { text ->
                                Row(
                                    Modifier.selectable(selected = (text == smokeVal), role = Role.RadioButton, onClick = {
                                        smokeVal = text
                                        viewModel.onSmokeChange(text == "Yes")
                                    }).padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(selected = (text == smokeVal), onClick = null)
                                    Text(text)
                                }
                            }
                        }
                        Spacer(Modifier.height(25.dp))
                        Row(modifier = Modifier
                            .fillMaxWidth(1F),
                            horizontalArrangement = Arrangement.Start
                        ){
                            Text("Physical Activity Level: ", fontSize = 20.sp)
                        }
                        Box {
                            TextField(
                                value = selectedActivity,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    Icon(
                                        imageVector = if (activityExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                                        contentDescription = null,
                                        modifier = Modifier.clickable { activityExpanded = !activityExpanded }
                                    )
                                },
                                modifier = Modifier.fillMaxWidth().clickable { activityExpanded = !activityExpanded }
                            )
                            DropdownMenu(
                                expanded = activityExpanded,
                                onDismissRequest = { activityExpanded = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                activityOptions.forEach { option ->
                                    DropdownMenuItem(
                                        onClick = {
                                            selectedActivity = option
                                            activityExpanded = false
                                            viewModel.onActivityLvChange(option)
                                        },
                                        text = { Text(option) }
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(25.dp))
                        Row(modifier = Modifier
                            .fillMaxWidth(1F),
                            horizontalArrangement = Arrangement.Start
                        ){
                            Text("Diet: ", fontSize = 20.sp)
                        }
                        Box {
                            TextField(
                                value = selectedDiet,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    Icon(
                                        imageVector = if (dietExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                                        contentDescription = null,
                                        modifier = Modifier.clickable { dietExpanded = !dietExpanded }
                                    )
                                },
                                modifier = Modifier.fillMaxWidth().clickable { dietExpanded = !dietExpanded }
                            )
                            DropdownMenu(
                                expanded = dietExpanded,
                                onDismissRequest = { dietExpanded = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                dietOptions.forEach { option ->
                                    DropdownMenuItem(
                                        onClick = {
                                            selectedDiet = option
                                            dietExpanded = false
                                            viewModel.onDietChange(option)
                                        },
                                        text = { Text(option) }
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(25.dp))
                        Text("Are you currently or have you been depressed in the past?")
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            yesNoOptions.forEach { text ->
                                Row(
                                    Modifier.selectable(selected = (text == depressedVal), role = Role.RadioButton, onClick = {
                                        depressedVal = text
                                        viewModel.onDepressedChange(text == "Yes")
                                    }).padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(selected = (text == depressedVal), onClick = null)
                                    Text(text)
                                }
                            }
                        }
                        Spacer(Modifier.height(25.dp))
                        Text("Do you get 8 hours of sleep every night?")
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            yesNoOptions.forEach { text ->
                                Row(
                                    Modifier.selectable(selected = (text == goodSleepVal), role = Role.RadioButton, onClick = {
                                        goodSleepVal = text
                                        viewModel.onGoodSleepChange(text == "Yes")
                                    }).padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(selected = (text == goodSleepVal), onClick = null)
                                    Text(text)
                                }
                            }
                        }
                        Spacer(Modifier.fillMaxHeight(0.7F))
                        Button(
                            modifier = Modifier.fillMaxWidth(0.5f),
                            onClick = {
                                Toast.makeText(context, "Form submitted", Toast.LENGTH_SHORT).show()
                                navController.navigate("home")
                            }
                        ) {
                            Text("Submit")
                        }
                    }
                }
            }
        }
    }
}
