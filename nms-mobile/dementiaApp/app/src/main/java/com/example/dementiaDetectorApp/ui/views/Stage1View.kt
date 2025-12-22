package com.example.dementiaDetectorApp.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dementiaDetectorApp.ui.composables.Footer
import com.example.dementiaDetectorApp.ui.composables.SubmittedSection
import com.example.dementiaDetectorApp.ui.theme.DarkPurple
import com.example.dementiaDetectorApp.ui.theme.Gray
import com.example.dementiaDetectorApp.ui.theme.MidPurple
import com.example.dementiaDetectorApp.ui.theme.buttonColours
import com.example.dementiaDetectorApp.ui.theme.outLinedTFColours
import com.example.dementiaDetectorApp.viewModels.SharedVM
import com.example.dementiaDetectorApp.viewModels.Stage1VM

@Composable
fun Stage1Screen(tVM: Stage1VM, sVM: SharedVM, nc: NavController){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MidPurple)
    ){
        Column(
            Modifier.padding(bottom = 50.dp)
        ){
            if(tVM.prefaceVisi.collectAsState().value){
                Spacer(Modifier.height(100.dp))
            }
            else{
                Spacer(Modifier.height(35.dp))
            }
            PrefaceSection(tVM)
            FormSection(tVM, sVM, nc)
        }
        if(!tVM.timedVisi.collectAsState().value && !tVM.prefaceVisi.collectAsState().value){
            Footer(Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
private fun PrefaceSection(tVM: Stage1VM){
    AnimatedVisibility(
        visible =tVM.prefaceVisi.collectAsState().value,
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
                    text="GP Cognitive Test Part 1",
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
                        text = "The following is a questionnaire that will ask you 4 questions\n\n" +
                                "The test will begin by displaying information that will remain on screen for 10 seconds that you must remember for a later question\n\n"+
                                "If you don't have an answer for a question, leave it blank",
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
                            tVM.onVisiChange(false)
                            tVM.onTimedVisiChange(true)}
                    ){
                        Text(
                            text = "Continue",
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FormSection(tVM: Stage1VM, sVM: SharedVM, nc: NavController){
    AnimatedVisibility(
        visible = !(tVM.prefaceVisi.collectAsState().value),
    ){
        Column{
            if (tVM.timedVisi.collectAsState().value){
                Spacer(Modifier.height(60.dp))
                tVM.visiTimer()
            }
            AnimatedVisibility(
                visible = tVM.timedVisi.collectAsState().value
            ){
                TimedSection()
            }
            AnimatedVisibility(
                visible = tVM.q1Visi.collectAsState().value,
            ){
                Question1(tVM)
            }
            AnimatedVisibility(
                visible = tVM.q2Visi.collectAsState().value,
            ){
                Question2(tVM)
                AnimatedVisibility(
                    visible = tVM.confirmVisi.collectAsState().value
                ){
                    ConfirmationSection(tVM)
                }
            }
            AnimatedVisibility(
                visible = tVM.q3Visi.collectAsState().value,
            ){
                Question3(tVM)
            }
            AnimatedVisibility(
                visible = tVM.q4Visi.collectAsState().value,
            ){
                Question4(tVM, sVM)
            }
            AnimatedVisibility(
                visible = tVM.successVisi.collectAsState().value,
            ){
                SubmittedSection(nc)
            }
        }
    }
}

@Composable
private fun TimedSection() {
    Column(Modifier
        .fillMaxSize()
        .background(MidPurple),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text="Recall Preface",
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
                text = "The following is information you will be asked to recall for a future question.\n"+
                        "Do not note it down, try to remember without any external help",
                fontSize = 20.sp,
                color = Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 35.dp, start = 15.dp, end = 15.dp)
            )
            Text(
                text = "First Name: John\nSecond Name: Brown\nNumber: 42\nStreet: West Street\nCity: Kensington",
                fontSize = 20.sp,
                color = Gray,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp)
            )
        }
    }
}

@Composable
private fun Question1(tVM: Stage1VM){
    val answer = tVM.date.collectAsState().value

    Column(Modifier
        .fillMaxSize()
        .background(Color.White)
    ){
        Spacer(Modifier.height(35.dp))
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically){
            Text(
                text = "1) What is the date?",
                fontSize = 30.sp,
                color = DarkPurple
            )
        }
        Spacer(Modifier.height(35.dp))
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Enter the exact date below in the format dd/mm/yyyy",
                fontSize = 22.5.sp,
                color = MidPurple,
                modifier = Modifier.padding(start = 15.dp, end = 15.dp))
        }
        Spacer(Modifier.height(35.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedTextField(
                value = answer,
                onValueChange = {if(it.length<=10){tVM.onDateChange(it)}},
                placeholder = {Text(text = "dd/mm/yyyy")},
                singleLine = true,
                colors = outLinedTFColours(),
                modifier = Modifier.width(300.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Row(modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ){
            Button(
                onClick = {
                    tVM.onQ1VisiChange(false)
                    tVM.onQ2VisiChange(true)
                },
                enabled = tVM.date.collectAsState().value.length==10,
                colors = buttonColours(),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .width(300.dp)
                    .padding(bottom = 25.dp)
            ) {
                Text(text = "Next Question", fontSize = 25.sp, color = Color.White)
            }
        }
    }
}

@Composable
private fun Question2(tVM: Stage1VM){
    Column(Modifier
        .fillMaxSize()
        .background(Color.White)
    ) {
        Spacer(Modifier.height(35.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "2) Select the Correct Clock",
                fontSize = 30.sp,
                color = DarkPurple,
                lineHeight = 32.5.sp,
                modifier = Modifier.padding(horizontal = 7.5.dp)
            )
        }
        Spacer(Modifier.height(35.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Select the clock that correctly depicts the time 11:10 AM",
                fontSize = 22.5.sp,
                color = MidPurple,
                modifier = Modifier.padding(start = 15.dp, end = 15.dp)
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 7.5.dp, end = 7.5.dp, bottom = 100.dp),
            modifier = Modifier
                .fillMaxHeight(0.9F)
                //.padding(7.5.dp)
                .background(Color.White)
        ){
            items(tVM.clocks.size){
                ClockImage(
                    drawingName = tVM.clocks[it],
                    tVM = tVM,
                    index = it
                )
            }
        }
    }
}

@Composable
private fun Question3(tVM:Stage1VM){
    val answer = tVM.newsEntry.collectAsState().value

    Column(Modifier
        .fillMaxSize()
        .background(Color.White)
    ) {
        Spacer(Modifier.height(35.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "3) Recalling Recent News",
                fontSize = 30.sp,
                color = DarkPurple
            )
        }
        Spacer(Modifier.height(35.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Name and describe a news story that has occurred recently (within the last 2 to 3 weeks)",
                fontSize = 22.5.sp,
                color = MidPurple,
                modifier = Modifier.padding(start = 15.dp, end = 15.dp)
            )}
        Spacer(Modifier.height(35.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedTextField(
                value = answer,
                onValueChange = {tVM.onNewsEntryChange(it)},
                placeholder = {Text(text = "News Story Here")},
                colors = outLinedTFColours(),
                modifier = Modifier.width(300.dp)
            )
        }
        Row(modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ){
            Button(
                onClick = {
                    tVM.onQ3VisiChange(false)
                    tVM.onQ4VisiChange(true)
                },
                enabled = tVM.newsEntry.collectAsState().value.isNotEmpty(),
                colors = buttonColours(),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .width(300.dp)
                    .padding(bottom = 25.dp)
            ) {
                Text(text = "Next Question", fontSize = 25.sp, color = Color.White)
            }
        }
    }
}

@Composable
private fun Question4(tVM:Stage1VM, sharedVM: SharedVM){
    Column(Modifier
        .fillMaxSize()
        .background(Color.White)
    ) {
        Spacer(Modifier.height(35.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "4) Recall Question",
                fontSize = 30.sp,
                color = DarkPurple
            )
        }
        Spacer(Modifier.height(5.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Enter in each respective text field what you were asked to remember at the beginning of this test",
                fontSize = 22.5.sp,
                color = MidPurple,
                modifier = Modifier.padding(start = 15.dp, end = 15.dp)
            )}

        Spacer(Modifier.height(5.dp))
        val fName = tVM.fName.collectAsState().value
        Text(
            text = "First Name",
            color = MidPurple,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 50.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedTextField(
                value = fName,
                onValueChange = {tVM.onFNChange(it)},
                placeholder = {Text(text = "First name")},
                colors = outLinedTFColours(),
                modifier = Modifier.width(300.dp)
            )
        }
        Spacer(Modifier.height(5.dp))
        val lName = tVM.lName.collectAsState().value
        Text(
            text = "Last Name",
            color = MidPurple,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 50.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedTextField(
                value = lName,
                onValueChange = {tVM.onLNChange(it)},
                placeholder = {Text(text = "Last name")},
                colors = outLinedTFColours(),
                modifier = Modifier.width(300.dp)
            )
        }

        Spacer(Modifier.height(5.dp))
        val num = tVM.number.collectAsState().value
        Text(
            text = "Number",
            color = MidPurple,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 50.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedTextField(
                value = num,
                onValueChange = {tVM.onNumberChange(it)},
                placeholder = {Text(text = "House number")},
                colors = outLinedTFColours(),
                modifier = Modifier.width(300.dp)
            )
        }

        Spacer(Modifier.height(5.dp))
        val street = tVM.street.collectAsState().value
        Text(
            text = "Street",
            color = MidPurple,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 50.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedTextField(
                value = street,
                onValueChange = {tVM.onStreetChange(it)},
                placeholder = {Text(text = "House street")},
                colors = outLinedTFColours(),
                modifier = Modifier.width(300.dp)
            )
        }

        Spacer(Modifier.height(5.dp))
        val city = tVM.city.collectAsState().value
        Text(
            text = "City",
            color = MidPurple,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 50.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedTextField(
                value = city,
                onValueChange = {tVM.onAreaChange(it)},
                placeholder = {Text(text = "House area")},
                colors = outLinedTFColours(),
                modifier = Modifier.width(300.dp)
            )
        }

        Row(modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ){
            Button(
                onClick = {
                    tVM.submitAnswers(sharedVM.id.value,{sharedVM.onTestSubmission(1)})
                },
                enabled = tVM.q4FullyAnswered.value,
                colors = buttonColours(),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .width(300.dp)
                    .padding(bottom = 25.dp)
            ) {
                Text(text = "Submit Answers", fontSize = 25.sp, color = Color.White)
            }
        }
    }
}

@Composable
private fun ClockImage(drawingName: String, tVM: Stage1VM, index: Int){
    val context = LocalContext.current
    // Get drawable resource ID from the resource name string
    val drawableId = remember(drawingName) {
        context.resources.getIdentifier(drawingName, "drawable", context.packageName)
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(15.dp)
        .clickable {
            tVM.onConfChange(true)
            tVM.onClockChange(index)
        }
    ){
        Icon(
            painter = painterResource(id = drawableId),
            contentDescription = "Clock image",
            tint = Color.Unspecified,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun ConfirmationSection(tVM: Stage1VM){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
    ){
        Column(
            verticalArrangement = Arrangement.spacedBy(35.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(0.dp)
        ){
            Text(
                text = "Confirm Choice",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = DarkPurple
            )
            Text(
                text = "Are you sure you want to select clock ${tVM.clock.collectAsState().value+1}?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MidPurple,
                modifier = Modifier.padding(horizontal = 7.5.dp)
            )

            val context = LocalContext.current
            val drawingName = tVM.clocks[tVM.clock.collectAsState().value]
            val drawableId = remember(drawingName) {
                context.resources.getIdentifier(drawingName, "drawable", context.packageName)
            }

            Icon(
                painter = painterResource(id = drawableId),
                contentDescription = "Clock image",
                tint = Color.Unspecified,
                modifier = Modifier
                    .height(300.dp)
                    .aspectRatio(1F)
            )

            Row(modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ){
                Button(
                    onClick = {
                        tVM.onConfChange(false)
                    },
                    colors = buttonColours(),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .width(200.dp)
                        .padding(bottom = 25.dp)
                ) {
                    Text(text = "Cancel", fontSize = 25.sp, color = Color.White)
                }
                Spacer(Modifier.width(10.dp))
                Button(
                    onClick = {
                        tVM.onQ2VisiChange(false)
                        tVM.onQ3VisiChange(true)
                    },
                    colors = buttonColours(),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .width(200.dp)
                        .padding(bottom = 25.dp)
                ) {
                    Text(text = "Confirm", fontSize = 25.sp, color = Color.White)
                }
            }
        }
    }
}