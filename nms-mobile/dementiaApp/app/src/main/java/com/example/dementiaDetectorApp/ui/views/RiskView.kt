package com.example.dementiaDetectorApp.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.ColorFilter
import com.example.dementiaDetectorApp.viewModels.SharedVM

@Composable
fun RiskScreen(rVM: RiskVM, sharedVM: SharedVM, nc: NavController) {

    val score = sharedVM.riskScore.value

    LaunchedEffect(score) {
        rVM.onRiskChange(score)
    }

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
            contentDescription = null,
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
    Image(
        painter = painterResource(img),
        contentDescription = null,
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .fillMaxHeight(0.35f),
        contentScale = ContentScale.FillBounds,
        colorFilter = ColorFilter.tint(rVM.tint.collectAsState().value)
    )
}

@Composable
private fun RiskBreakdownSection(rVM: RiskVM) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Text(
            text = rVM.riskResult.value,
            fontSize = 27.5.sp,
            fontWeight = FontWeight.Bold,
            color = DarkPurple
        )
        HorizontalDivider(thickness = 2.dp, color = MidPurple)
        Text(
            text = rVM.riskMsg.value,
            fontSize = 22.5.sp,
            fontWeight = FontWeight.Medium,
            color = MidPurple
        )
    }
}

@Composable
private fun TestCountSection(sharedVM: SharedVM) {
    val done = sharedVM.testsDone.value
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        text = if (done == 4)
            "You have completed all the tests"
        else
            "You have completed $done/4 tests.\nThe more tests you complete the more accurate the results.",
        fontSize = 22.5.sp,
        fontWeight = FontWeight.Medium,
        color = DarkPurple
    )
}