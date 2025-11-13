package com.example.dementiaDetectorApp.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Question(premise: String, type: Int, num: Int, ){
    Box(modifier = Modifier
        .fillMaxSize(1F)
    ){
        Column(modifier = Modifier
            .fillMaxSize(1F),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(modifier = Modifier
                .fillMaxHeight(0.1F)
            ){
                Text("Question $num", fontSize = 20.sp, color = Color.Magenta, modifier = Modifier.padding(start=15.dp))
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.05F))
            Row(modifier = Modifier
                .fillMaxHeight(0.333F)
                .fillMaxWidth(0.8F)
            ){
                Text(premise, fontSize = 18.sp, color = Color.Black)
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.05F))
            Row(modifier = Modifier
                .fillMaxHeight(0.5F)
            ){
                if(type==1){    //If Text entry answer
                    /*TextField(modifier = Modifier
                        .fillMaxHeight(1F)
                        .fillMaxWidth(0.8F)
                    ){

                    }*/
                }
            }
        }
    }
}