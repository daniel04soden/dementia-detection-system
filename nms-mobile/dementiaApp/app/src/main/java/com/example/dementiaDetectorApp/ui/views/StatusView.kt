package com.example.dementiaDetectorApp.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dementiaDetectorApp.R
import com.example.dementiaDetectorApp.models.Test
import com.example.dementiaDetectorApp.ui.composables.NavMenu
import com.example.dementiaDetectorApp.ui.composables.ReusableToast
import com.example.dementiaDetectorApp.ui.theme.DarkPurple
import com.example.dementiaDetectorApp.ui.theme.LightPurple
import com.example.dementiaDetectorApp.ui.theme.MidPurple
import com.example.dementiaDetectorApp.ui.theme.Yellow
import com.example.dementiaDetectorApp.viewModels.SharedVM

@Composable
fun StatusScreen(sharedVM: SharedVM, nc: NavController){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
    ){
        Column{
            Spacer(Modifier.height(35.dp))
            HeaderSection()
            TestSection(sharedVM, nc)
        }

        ReusableToast(alignment = Alignment.BottomCenter)

        NavMenu(
            sharedVM,
            nc,
            Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun HeaderSection(){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp),
        horizontalArrangement = Arrangement.spacedBy(35.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo",
            tint = Color.Unspecified,
            modifier = Modifier.height(45.dp)
        )
        Column {
            Text("Test Statuses", color = DarkPurple, fontSize = 25.sp)
            Text("Status information on each test", color = MidPurple, fontSize = 18.sp)
        }
    }
}

@Composable
private fun TestCard(test:Test, sharedVM: SharedVM, nc: NavController){
    Card(
        colors = CardDefaults.cardColors(containerColor = LightPurple),
        modifier = Modifier.clickable {
            if(test.route=="questionnaire"){
                sharedVM.CheckCompleted(test.route, {nc.navigate(test.route)}, {nc.navigate("submitQuestionnaire")})
            }else{
                sharedVM.CheckCompleted(test.route, {nc.navigate(test.route)})
            }
        }
    ){
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ){
            Text(
                text = test.name,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = DarkPurple
            )

            var status = when (test.route) {
                "test2" -> when (test.state) {
                    1 -> "Graded"
                    2 -> "Not Required"
                    3 -> "Ready to complete"  // Fixed
                    in 4..Int.MAX_VALUE -> "Graded"
                    else -> "Not Required"
                }
                else -> when (test.state) {
                    0 -> "Not Completed"
                    1 -> "Awaiting Grade"
                    2, 3, 4 -> "Graded"
                    5 -> "Can be graded with AI"
                    else -> "Not Completed"
                }
            }

            val col = when (test.route) {
                "test2" -> when (test.state) {
                    2 -> Color.White
                    3 -> Color.Yellow
                    1, in 4..Int.MAX_VALUE -> Color.Green
                    else -> Color.White
                }
                else -> when (test.state) {
                    0 -> Color.Red
                    1, 5 -> Color.Yellow
                    else -> Color.Green
                }
            }

            Text(
                    text = status,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = col
                )
            }
        }
    }

    @Composable
    private fun TestSection(sharedVM: SharedVM, nc: NavController){
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(35.dp)
        ){
            LazyColumn(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(35.dp)
            ){
                items(sharedVM.tests.value.size){idx ->
                    TestCard(sharedVM.tests.value[idx], sharedVM, nc)
                }
            }
        }
    }