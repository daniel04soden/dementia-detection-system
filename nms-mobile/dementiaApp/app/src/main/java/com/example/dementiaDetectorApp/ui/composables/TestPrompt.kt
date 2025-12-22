package com.example.dementiaDetectorApp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dementiaDetectorApp.R
import com.example.dementiaDetectorApp.models.Test
import com.example.dementiaDetectorApp.ui.theme.LightPurple
import com.example.dementiaDetectorApp.ui.theme.MidPurple
import com.example.dementiaDetectorApp.viewModels.SharedVM

@Composable
fun TestPrompt(
    test: Test,
    sharedVM: SharedVM,
    nc: NavController
){
    Row(modifier = Modifier
        .padding(15.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(MidPurple)//CHANGE
        .padding(horizontal = 15.dp , vertical = 20.dp)
        .fillMaxWidth()
        .clickable {sharedVM.CheckCompleted(test.route,{nc.navigate(test.route)})},
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(LightPurple)
                .padding(5.dp),
            contentAlignment = Alignment.Center
        ){
            Icon(
                painter = painterResource(R.drawable.todo),
                contentDescription = "Test Icon",
                Modifier.size(30.dp)
            )
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(test.name, color = Color.White)
            val state = deriveState(test)
            Text(state, color = Color.White)
        }
    }
}

private fun deriveState(test: Test): String{
    val stateVal = test.state
    if (stateVal == 1){
        return "Awaiting grading"
    }
    else if (stateVal>=2){
        test.route = "status"
        return "Graded"
    }
    else{
        return "To be completed"
    }
}