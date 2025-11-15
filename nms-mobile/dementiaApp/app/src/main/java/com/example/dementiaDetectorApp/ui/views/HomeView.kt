package com.example.dementiaDetectorApp.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dementiaDetectorApp.ui.composables.HomePageBox
import com.example.dementiaDetectorApp.viewModels.AuthViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: AuthViewModel){
    val fName by viewModel.fName.collectAsState()

    Scaffold(
        topBar = {
            Box(modifier = Modifier
                .fillMaxHeight(0.15F)
                .fillMaxWidth(1F)
                .padding(WindowInsets.statusBars.asPaddingValues())
                .background(color = Color.Transparent)
            ){
                Box(modifier = Modifier
                    .fillMaxSize(1F)
                    .background(color = Color.Magenta)
                ){
                    Column( modifier = Modifier
                        .fillMaxSize(1F),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ){
                        Row(modifier = Modifier
                            .fillMaxHeight(0.6F)
                            .fillMaxSize(1F),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ){
                            Text("Hello $fName", fontSize = 34.sp, fontWeight = FontWeight.Bold, color = Color.Yellow)
                        }
                        Row(modifier = Modifier
                            .fillMaxSize(1F)
                        ){
                            Text("What can we do for you today?", fontSize = 25.sp, color = Color.White)
                        }
                    }
                }
            }
        },
        bottomBar = {
            Box(modifier = Modifier
                .fillMaxHeight(0.125F)
                .fillMaxWidth(1F)
                .background(color = Color.Magenta)
            ){
                Row(modifier = Modifier
                    .fillMaxSize(1F),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Button(modifier = Modifier
                        .fillMaxWidth(0.25F)
                        .fillMaxHeight(1F),
                        shape = RectangleShape,
                        onClick = {}
                    ){
                        Text("Home", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }

                    Button(modifier = Modifier
                        .fillMaxWidth(0.333F)
                        .fillMaxHeight(1F),
                        shape = RectangleShape,
                        onClick = {navController.navigate("status")}
                    ){
                        Text("Test Status", fontSize = 15.sp)
                    }

                    Button( modifier = Modifier
                        .fillMaxWidth(0.5F)
                        .fillMaxHeight(1F),
                        shape = RectangleShape,
                        onClick = {navController.navigate("risk")}
                    ){
                        Text("Risk", fontSize = 15.sp)
                    }

                    Button( modifier = Modifier
                        .fillMaxSize(1F),
                        shape = RectangleShape,
                        onClick = {navController.navigate("contact")}
                    ){
                        Text("Contact", fontSize = 15.sp)
                    }
                }
            }
        }
    ){
            innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize(1F)
            .background(color = Color.White)
            .padding(innerPadding)
        ){
            Column(modifier = Modifier
                .fillMaxSize(1F),
                verticalArrangement = Arrangement.Center
            ){
                Column(modifier = Modifier
                    .fillMaxHeight(0.333F)
                    .fillMaxWidth(1F)
                    .padding(all=8.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment =Alignment.Start
                ){
                    Row(modifier = Modifier
                        .fillMaxHeight(0.2F)
                        .fillMaxWidth(1F),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text("Tests", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Color.Magenta)
                    }

                    Row(modifier = Modifier
                        .fillMaxSize(1F)
                        .background(color = Color.Green),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        //Lazy row of tests
                        LazyRow(modifier = Modifier
                            .fillMaxSize(1F),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            item {
                                HomePageBox(
                                    "Questionnaire",
                                    "A 2 stage questionnaire that will help determine if you are at risk of having dementia",
                                    link = "questionnaire",
                                    linkText = "Take test")
                                    {navController.navigate("questionnaire")}
                            }
                            item {
                                HomePageBox(
                                    header = "GP Cognitive Test ",
                                    description = "A test used by GPs to determine if one is at risk of dementia",
                                    link = "assessment",
                                    linkText = "Take Test"
                                ){navController.navigate("assessment")}
                            }
                        }
                    }
                }
                Column(modifier = Modifier
                    .fillMaxSize(1F)
                    .padding(all=8.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ){
                    Row(modifier = Modifier
                        .fillMaxHeight(0.1F)
                        .fillMaxWidth(1F),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text("News", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Color.Magenta)
                    }
                    Row(modifier = Modifier
                        .fillMaxSize(1F)
                        .background(color = Color.Blue)
                    ){
                        //Lazy column of News
                    }
                }
            }
        }
    }
}