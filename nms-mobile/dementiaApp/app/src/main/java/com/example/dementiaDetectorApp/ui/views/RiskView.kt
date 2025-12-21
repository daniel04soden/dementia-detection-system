package com.example.dementiaDetectorApp.ui.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dementiaDetectorApp.R
import com.example.dementiaDetectorApp.ui.composables.NavMenu
import com.example.dementiaDetectorApp.ui.theme.DarkPurple
import com.example.dementiaDetectorApp.ui.theme.MidPurple
import com.example.dementiaDetectorApp.viewModels.RiskVM
import com.example.dementiaDetectorApp.viewModels.SharedVM

@Composable
fun RiskScreen(rVM: RiskVM, sharedVM: SharedVM, nc: NavController) {
    rVM.onRiskChange(sharedVM.getRiskScore())
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column {
            Spacer(Modifier.height(35.dp))
            HeaderSection()
            ImageSection(rVM)
            RiskBreakdownSection(rVM)
            TestCountSection(sharedVM)
        }
        NavMenu(
            sharedVM = sharedVM,
            nc = nc,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun HeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalArrangement = Arrangement.spacedBy(35.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo",
            tint = Color.Unspecified,
            modifier = Modifier.height(45.dp)
        )
        Column {
            Text("Risk Assessment", color = DarkPurple, fontSize = 25.sp)
            Text("An analysis on the results of our tests", color = MidPurple, fontSize = 18.sp)
        }
    }
}

@Composable
private fun ImageSection(rVM: RiskVM) {
    val img = rVM.img.collectAsState().value
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Image(
            painter = painterResource(img),
            contentDescription = "Image to describe",
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.35f),
            contentScale = ContentScale.FillBounds
        )
    }
}

@Composable
private fun RiskBreakdownSection(rVM: RiskVM){
   Column(modifier = Modifier
       .fillMaxWidth()
       .padding(top = 15.dp)
       .padding(horizontal = 15.dp),
       horizontalAlignment = Alignment.CenterHorizontally,
       verticalArrangement = Arrangement.spacedBy(15.dp)
   ){
       Text(
          text=rVM.riskResult.value,
          fontSize = 27.5.sp,
          fontWeight = FontWeight.Bold,
          color = DarkPurple,
          lineHeight = 37.5.sp
       )
       HorizontalDivider(
           thickness = 2.dp,
           color = MidPurple,
       )

       Text(
           text=rVM.riskMsg.value,
           fontSize = 22.5.sp,
           fontWeight = FontWeight.Medium,
           color = MidPurple,
           lineHeight = 30.sp
       )
   }
}

@Composable
private fun TestCountSection(sharedVM: SharedVM){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 35.dp)
        .padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        if(sharedVM.getTestsDone()==4){
            Text(
                text = "You have completed all the tests",
                fontSize = 22.5.sp,
                fontWeight = FontWeight.Medium,
                color = DarkPurple,
                lineHeight = 30.sp
            )
        }
        else {
            Text(
                text = "You have completed ${sharedVM.getTestsDone()}/4 tests.\nThe more tests you complete the more accurate the results.",
                fontSize = 22.5.sp,
                fontWeight = FontWeight.Medium,
                color = DarkPurple,
                lineHeight = 30.sp
            )
        }
    }
}