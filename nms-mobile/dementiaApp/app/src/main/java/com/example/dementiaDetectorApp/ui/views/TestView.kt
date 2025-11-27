package com.example.dementiaDetectorApp.ui.views

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dementiaDetectorApp.R
import com.example.dementiaDetectorApp.ui.composables.OptionRow
import com.example.dementiaDetectorApp.viewModels.TestViewModel

@Composable
fun TestScreen(navController: NavController, viewModel: TestViewModel) {
    // AUTO-DISMISS PANEL STATE
    var autoDismissVisible by remember { mutableStateOf(true) }

    // Section 1 states
    val dateA by viewModel.dateA.collectAsState()
    val clockA by viewModel.clockA.collectAsState()
    val newsA by viewModel.newsA.collectAsState()
    val recallFName by viewModel.recallFName.collectAsState()
    val recallLName by viewModel.recallLName.collectAsState()
    val recallNum by viewModel.recallNum.collectAsState()
    val recallStreet by viewModel.recallStreet.collectAsState()
    val recallArea by viewModel.recallArea.collectAsState()

    // Section 2 states
    val rememberA by viewModel.rememberA.collectAsState()
    val conversationA by viewModel.conversationA.collectAsState()
    val speakingA by viewModel.speakingA.collectAsState()
    val financialA by viewModel.financialA.collectAsState()
    val medicationA by viewModel.medicationA.collectAsState()
    val transportA by viewModel.transportA.collectAsState()

    // Section 1 panels visibility
    var section1Panel1Visible by remember { mutableStateOf(false) } // Date
    var section1Panel2Visible by remember { mutableStateOf(false) } // Clock
    var section1Panel3Visible by remember { mutableStateOf(false) } // News
    var section1Panel4Visible by remember { mutableStateOf(false) } // Recall

    // Section 2 panel visibility
    var section2Visible by remember { mutableStateOf(false) }

    // Clock Dropdown
    val clockOptions = listOf(1, 2, 3)
    var clockDropdownExpanded by remember { mutableStateOf(false) }
    var clockSelectedText by remember { mutableStateOf(clockOptions.getOrElse(clockA) { 1 }.toString()) }

    val context = androidx.compose.ui.platform.LocalContext.current

    // Auto-hide the panel after 15 seconds and start Section 1 flow
    if (autoDismissVisible) {
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(15_000L)
            autoDismissVisible = false
            section1Panel1Visible = true
        }
    }

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
                        when {
                            autoDismissVisible -> "Recall Test"
                            section2Visible -> "Stage 2: Memory and Cognition"
                            else -> "Section 1: Initial Details"
                        },
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

                // AUTO-DISMISS PANEL
                AnimatedVisibility(visible = autoDismissVisible) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            "Remember this information as you will be asked to recall it later",
                            fontSize = 24.sp
                        )
                        Spacer(Modifier.height(20.dp))
                        Text("First name: John", fontSize = 18.sp)
                        Text("Last Name: Brown", fontSize = 18.sp)
                        Text("Number: 42", fontSize = 18.sp)
                        Text("Street: West (St)", fontSize = 18.sp)
                        Text("Area: Kensington", fontSize = 18.sp)
                    }
                }

                // SECTION 1: PANEL 1 - DATE ENTRY
                AnimatedVisibility(
                    visible = section1Panel1Visible,
                    enter = slideInHorizontally() + fadeIn(),
                    exit = slideOutHorizontally() + fadeOut()
                ) {
                    Column(
                        Modifier.fillMaxSize().background(Color.LightGray),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            Modifier.fillMaxWidth().height(56.dp).background(Color.Green),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Date Entry",
                                Modifier.padding(start = 15.dp),
                                color = Color.Magenta,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.height(25.dp))
                        Text(
                            "Enter the exact date",
                            Modifier.padding(start = 15.dp),
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(20.dp))
                        TextField(
                            value = dateA,
                            onValueChange = viewModel::onDateAChange,
                            label = { Text("Date (YYYY-MM-DD)") },
                            modifier = Modifier.fillMaxWidth(0.8f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.White)
                        )
                        Spacer(Modifier.fillMaxHeight(0.8f))
                        Button(
                            modifier = Modifier.fillMaxWidth(0.5f),
                            onClick = {
                                section1Panel1Visible = false
                                section1Panel2Visible = true
                            }
                        ) { Text("Next") }
                    }
                }
                AnimatedVisibility(
                    visible = section1Panel2Visible,
                    enter = slideInHorizontally() + fadeIn(),
                    exit = slideOutHorizontally() + fadeOut()
                ) {
                    Column(
                        Modifier.fillMaxSize().background(Color.LightGray),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Green header bar only
                        Row(
                            Modifier.fillMaxWidth().height(56.dp).background(Color.Green),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Clock Selection",
                                Modifier.padding(start = 15.dp),
                                color = Color.Magenta,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(Modifier.height(25.dp)) // clear space below green bar

                        // Instruction text
                        Text(
                            "Please select the image of the clock which displays the time 11:10",
                            fontSize = 24.sp
                        )

                        Spacer(Modifier.height(16.dp))

                        // The dropdown selector with clock options
                        Box(Modifier.fillMaxWidth(0.8f)) {
                            Text(
                                clockSelectedText,
                                Modifier
                                    .fillMaxWidth()
                                    .clickable { clockDropdownExpanded = true }
                                    .background(Color.White)
                                    .padding(16.dp),
                                fontSize = 16.sp
                            )
                            DropdownMenu(
                                expanded = clockDropdownExpanded,
                                onDismissRequest = { clockDropdownExpanded = false },
                                modifier = Modifier.fillMaxWidth(0.8f)
                            ) {
                                clockOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option.toString()) },
                                        onClick = {
                                            clockSelectedText = option.toString()
                                            clockDropdownExpanded = false
                                            viewModel.onClockAChange(option)
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        // Image displayed below dropdown, updates based on selection
                        val imageRes = when (clockSelectedText) {
                            "1" -> R.drawable.clock1
                            "2" -> R.drawable.clock2
                            "3" -> R.drawable.clock3
                            else -> R.drawable.clock1
                        }
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = "Clock image $clockSelectedText",
                            modifier = Modifier.height(150.dp)
                        )

                        Spacer(Modifier.height(25.dp)) // increased space before button

                        // Next button visibly spaced below image, outside green bar
                        Button(
                            modifier = Modifier.fillMaxWidth(0.5f),
                            onClick = {
                                section1Panel2Visible = false
                                section1Panel3Visible = true
                            }
                        ) {
                            Text("Next")
                        }
                    }
                }
            }


                // SECTION 1: PANEL 3 - NEWS TEXTFIELD
            AnimatedVisibility(
                visible = section1Panel3Visible,
                enter = slideInHorizontally() + fadeIn(),
                exit = slideOutHorizontally() + fadeOut()
            ) {
                Column(
                    Modifier.fillMaxSize().background(Color.LightGray),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        Modifier.fillMaxWidth().height(56.dp).background(Color.Green),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "News Entry",
                            Modifier.padding(start = 15.dp),
                            color = Color.Magenta,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(25.dp))
                    Text(
                        "Enter a story you have seen in the news recently",
                        Modifier.padding(start = 15.dp),
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(20.dp))
                    TextField(
                        value = newsA,
                        onValueChange = viewModel::onNewsAChange,
                        label = { Text("News") },
                        modifier = Modifier.fillMaxWidth(0.8f),
                        colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.White)
                    )
                    Spacer(Modifier.fillMaxHeight(0.8f))
                    Button(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        onClick = {
                            section1Panel3Visible = false
                            section1Panel4Visible = true
                        }
                    ) { Text("Next") }
                }
            }

            // SECTION 1: PANEL 4 - RECALL 4 TEXTFIELDS
            AnimatedVisibility(
                visible = section1Panel4Visible,
                enter = slideInHorizontally() + fadeIn(),
                exit = slideOutHorizontally() + fadeOut()
            ) {
                Column(
                    Modifier.fillMaxSize().background(Color.LightGray),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        Modifier.fillMaxWidth().height(56.dp).background(Color.Green),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Recall Information",
                            Modifier.padding(start = 15.dp),
                            color = Color.Magenta,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(25.dp))
                    Text(
                        "Enter the information you were asked to remember",
                        Modifier.padding(start = 15.dp),
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(20.dp))
                    Column(Modifier.fillMaxWidth(0.8f)) {
                        TextField(
                            value = recallFName,
                            onValueChange = viewModel::onRecallFNameChange,
                            label = { Text("First Name") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.White)
                        )
                        Spacer(Modifier.height(16.dp))
                        TextField(
                            value = recallLName,
                            onValueChange = viewModel::onRecallLNameChange,
                            label = { Text("Last Name") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.White)
                        )
                        Spacer(Modifier.height(16.dp))
                        TextField(
                            value = recallNum,
                            onValueChange = viewModel::onRecallNumChange,
                            label = { Text("Number") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.White)
                        )
                        Spacer(Modifier.height(16.dp))
                        TextField(
                            value = recallStreet,
                            onValueChange = viewModel::onRecallStreetChange,
                            label = { Text("Street") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.White)
                        )
                        Spacer(Modifier.height(16.dp))
                        TextField(
                            value = recallArea,
                            onValueChange = viewModel::onRecallAreaChange,
                            label = { Text("Area") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.White)
                        )
                    }
                    Spacer(Modifier.fillMaxHeight(0.1f))
                    Button(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        onClick = {
                            section1Panel4Visible = false
                            section2Visible = true
                            Toast.makeText(context, "Section 1 completed", Toast.LENGTH_SHORT).show()
                        }
                    ) { Text("Next: Stage 2") }
                }
            }

            // SECTION 2: COGNITIVE ASSESSMENT PANEL
                    AnimatedVisibility(
                        visible = section2Visible,
                        enter = slideInHorizontally() + fadeIn(),
                        exit = slideOutHorizontally() + fadeOut()
                    ) {
                        LazyColumn(
                            Modifier.fillMaxSize().background(Color.LightGray).padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            content = {
                                item {
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .height(56.dp)
                                            .background(Color.White),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            "Stage 2: Informant Questions",
                                            color = Color.Magenta,
                                            fontSize = 24.sp,  // Slightly bigger font
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(start = 15.dp)
                                        )
                                    }
                                    Spacer(Modifier.height(25.dp))
                                    Text(
                                            "Have a caretaker or another individual answer these questions",
                                            color = Color.Black,
                                            fontSize = 24.sp,  // Slightly bigger font
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(start = 15.dp)
                                        )
                                    Spacer(Modifier.height(25.dp))
                                }

                                val firstOptions = listOf("Yes", "No", "Don't know")
                                val lastOptions = listOf("Yes", "No", "Don't know", "N/A")

                                val questions = listOf(
                                    "1. Does the patient have more trouble remembering things that have happened recently than before?",
                                    "2. Does s/he have more trouble recalling conversations a few days later?",
                                    "3. When speaking, does s/he have more difficulty finding the right word or tend to use the wrong words more often?"
                                )
                                val firstOptionsSelected = listOf(rememberA, conversationA, speakingA)
                                val firstOptionsCallbacks = listOf<(String) -> Unit>(
                                    { option -> viewModel.onRememberAChange(firstOptions.indexOf(option)) },
                                    { option -> viewModel.onConversationAChange(firstOptions.indexOf(option)) },
                                    { option -> viewModel.onSpeakingAChange(firstOptions.indexOf(option)) }
                                )

                                itemsIndexed(questions) { index, question ->
                                    Text(
                                        question,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Normal,
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    OptionRow(
                                        selectedValue = firstOptionsSelected[index],
                                        options = firstOptions,
                                        onSelect = firstOptionsCallbacks[index]
                                    )
                                    Spacer(Modifier.height(25.dp))
                                }

                                val lastQuestions = listOf(
                                    "4. Is s/he less able to manage money and financial affairs (e.g. paying bills and budgeting)?",
                                    "5. Is s/he less able to manage his or her medication independently?",
                                    "6. Does s/he need more assistance with transport (either private or public)? If due to physical problems only, tick 'No'."
                                )
                                val lastOptionsSelected = listOf(financialA, medicationA, transportA)
                                val lastOptionsCallbacks = listOf<(String) -> Unit>(
                                    { option -> viewModel.onFinancialAChange(lastOptions.indexOf(option)) },
                                    { option -> viewModel.onMedicationAChange(lastOptions.indexOf(option)) },
                                    { option -> viewModel.onTransportAChange(lastOptions.indexOf(option)) }
                                )

                                itemsIndexed(lastQuestions) { index, question ->
                                    Text(
                                        question,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Normal,
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    OptionRow(
                                        selectedValue = lastOptionsSelected[index],
                                        options = lastOptions,
                                        onSelect = lastOptionsCallbacks[index]
                                    )
                                    Spacer(Modifier.height(25.dp))
                                }

                                item {
                                    Spacer(Modifier.fillMaxHeight(0.7f))
                                    Button(
                                        modifier = Modifier.fillMaxWidth(0.5f),
                                        onClick = {
                                            Toast.makeText(context, "Stage 2 complete", Toast.LENGTH_SHORT).show()
                                            navController.navigate("home")
                                        }
                                    ) {
                                        Text("Submit", fontSize = 22.sp)
                                    }
                                }
                            }
                        )
                    }
        }
    }
}