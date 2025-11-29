package com.example.dementiaDetectorApp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dementiaDetectorApp.ui.theme.DarkPurple
import com.example.dementiaDetectorApp.ui.theme.MidPurple
import com.example.dementiaDetectorApp.ui.theme.buttonColours

@Composable
fun SubmittedSection(nc: NavController){
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Submission Successful",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = DarkPurple
        )
        Text(
            text = "Your answers have been submitted.\n\n\nPlease wait while we have medical professionals provide us with their opinion on your responses",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MidPurple
        )
        Row(modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ){
            Button(
                onClick = {
                    nc.navigate("home")
                },
                colors = buttonColours(),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .width(300.dp)
                    .padding(bottom = 25.dp)
            ) {
                Text(text = "Continue", fontSize = 25.sp, color = Color.White)
            }
        }
    }
}