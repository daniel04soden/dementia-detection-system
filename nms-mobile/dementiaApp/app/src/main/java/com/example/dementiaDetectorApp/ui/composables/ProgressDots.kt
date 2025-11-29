package com.example.dementiaDetectorApp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dementiaDetectorApp.ui.theme.MidPurple

@Composable
fun ProgressDots(
    modifier: Modifier
){
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(15.dp)
        .background(MidPurple),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text("Progress dots here")
    }
}