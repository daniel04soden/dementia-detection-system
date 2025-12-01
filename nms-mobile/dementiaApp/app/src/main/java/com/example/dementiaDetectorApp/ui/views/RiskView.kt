package com.example.dementiaDetectorApp.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dementiaDetectorApp.R
import com.example.dementiaDetectorApp.ui.composables.NavMenu
import com.example.dementiaDetectorApp.ui.theme.DarkPurple
import com.example.dementiaDetectorApp.ui.theme.MidPurple
import com.example.dementiaDetectorApp.viewModels.SharedVM
import com.example.dementiaDetectorApp.viewModels.StatusVM

@Composable
fun RiskScreen(/*rVM: RiskVM,*/ sharedVM: SharedVM, nc: NavController){
    //sVM.initTests(sharedVM.id.value)
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
    ){
        Column{
            Spacer(Modifier.height(35.dp))
            HeaderSection()
        }
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
            Text("Risk Assessment", color = DarkPurple, fontSize = 25.sp)
            Text("How at risk are you of having dementia", color = MidPurple, fontSize = 18.sp)
        }
    }
}
