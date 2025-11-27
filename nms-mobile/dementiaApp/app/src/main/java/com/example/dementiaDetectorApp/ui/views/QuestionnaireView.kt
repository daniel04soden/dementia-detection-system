package com.example.dementiaDetectorApp.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dementiaDetectorApp.ui.theme.Gray
import com.example.dementiaDetectorApp.ui.theme.MidPurple
import com.example.dementiaDetectorApp.ui.theme.buttonColours
import com.example.dementiaDetectorApp.viewModels.QViewModel
import com.zekierciyas.library.view.SurveyScreen

@Composable
fun QuestionnaireScreen(qVM: QViewModel, nc: NavController){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MidPurple)
    ){
        Column{
            if(qVM.prefaceVisi.collectAsState().value){
                Spacer(Modifier.height(200.dp))
            }
            PrefaceSection(qVM)
            FormSection(qVM)
        }
        if(!(qVM.prefaceVisi.collectAsState().value)){
            ProgressDots(Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
fun PrefaceSection(qVM: QViewModel){
    AnimatedVisibility(
        visible =qVM.prefaceVisi.collectAsState().value,
        exit = slideOutHorizontally() + fadeOut()
    ){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        ){
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text="Lifestyle Questionnaire",
                    textAlign = TextAlign.Center,
                    fontSize = 32.5.sp,
                    lineHeight = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                HorizontalDivider(
                    thickness = 2.dp,
                    color = Color.White,
                )
                Column(modifier = Modifier
                    .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(125.dp)
                ){
                    Text(
                        text = "The following is a questionnaire covering general questions, habits and certain readings that we recommend you get from your GP\n\n" +
                                "Although not 100% accurate this questionnaire can help predict the presence of dementia",
                        fontSize = 20.sp,
                        color = Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 35.dp, start = 15.dp, end = 15.dp)
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth(0.75F),
                        colors = buttonColours(),
                        onClick = {
                            qVM.onVisiChange(false)
                            qVM.onS1Change(true)}
                    ){
                        Text(
                            text = "Take Questionnaire"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FormSection(qVM: QViewModel){
    AnimatedVisibility(
        visible = !(qVM.prefaceVisi.collectAsState().value),
    ){
        Column{
            AnimatedVisibility(
                visible = qVM.s1visi.collectAsState().value,
                //enter = slideInHorizontally() + fadeIn(),
                //exit = slideOutHorizontally() + fadeOut(),
            ){
                S1Section(qVM)
            }
            AnimatedVisibility(
                visible = qVM.s2visi.collectAsState().value,
                //enter = slideInHorizontally() + fadeIn(),
                //exit = slideOutHorizontally() + fadeOut(),
            ){
                S2Section(qVM)
            }
            AnimatedVisibility(
                visible = qVM.s3visi.collectAsState().value,
                //enter = slideInHorizontally() + fadeIn(),
                //exit = slideOutHorizontally() + fadeOut(),
            ){
                S3Section(qVM)
            }
        }
    }
}

@Composable
fun S1Section(qVM: QViewModel){
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(Modifier.height(35.dp))
        Row(modifier = Modifier
            .background(Color.White)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text( "General Questions", fontSize = 35.sp, fontWeight = FontWeight.Bold, color = MidPurple)
        }
        Row(Modifier.fillMaxHeight(0.9F)){
            SurveyScreen(survey = qVM.s1Survey)
                //backgroundColor = LightPurple,)
        }
        Button(
            onClick = {
                qVM.onS1Change(false)
                qVM.onS2Change(true)},
            colors = buttonColours(),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth(0.75F)
        ){
            Text(
                text = "Next Questions",
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun S2Section(qVM: QViewModel){
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(Modifier.height(35.dp))
        Row(modifier = Modifier
            .background(Color.White)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text( "GP Reading Questions", fontSize = 35.sp, fontWeight = FontWeight.Bold, color = MidPurple)
        }

        Row(Modifier.fillMaxHeight(0.9F)){
            SurveyScreen(survey = qVM.s2Survey)
        }
        Button(
            onClick = {
                qVM.onS2Change(false)
                qVM.onS3Change(true)},
            colors = buttonColours(),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth(0.75F)
        ){
            Text(
                text = "Next Questions",
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun S3Section(qVM: QViewModel){
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(Modifier.height(35.dp))
        Row(modifier = Modifier
            .background(Color.White)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text( "Habbit Questions", fontSize = 35.sp, fontWeight = FontWeight.Bold, color = MidPurple)
        }
        Row(Modifier.fillMaxHeight(0.9F)){
            SurveyScreen(survey = qVM.s3Survey)
        }
        Button(
            onClick = {
                qVM.onS3Change(false)
                //Api call to send off a POST request
            },
            colors = buttonColours(),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth(0.75F)
        ){
            Text(
                text = "Submit Answers",
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }
}


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