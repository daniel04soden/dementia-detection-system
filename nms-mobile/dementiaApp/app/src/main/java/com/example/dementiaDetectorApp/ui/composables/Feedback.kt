package com.example.dementiaDetectorApp.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dementiaDetectorApp.ui.theme.DarkPurple
import com.example.dementiaDetectorApp.ui.theme.LightPurple
import com.example.dementiaDetectorApp.ui.theme.MidPurple
import com.example.dementiaDetectorApp.ui.theme.buttonColours
import com.example.dementiaDetectorApp.ui.theme.outLinedTFColours
import com.example.dementiaDetectorApp.viewModels.HomeVM
import com.example.dementiaDetectorApp.viewModels.SharedVM

@Composable
fun FeedbackPanel(
    homeVM: HomeVM,
    sharedVM: SharedVM,
    modifier: Modifier = Modifier
){
    AnimatedVisibility(
        visible = homeVM.feedbackVisi.value,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ){
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 150.dp, bottom = 200.dp)
                .background(DarkPurple)
                .border(width = 5.dp, color = LightPurple),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(35.dp)
        ){
            Spacer(Modifier.height(35.dp))
            Text(
                text = "Rate & Feedback",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // 5-Star Rating
            StarRatingBar(
                homeVM,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                text = "Rating: ${homeVM.rating.value}/5",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // Feedback TextField
            OutlinedTextField(
                value = homeVM.feedback.value,
                onValueChange = {homeVM.onFeedbackChange(it)},
                label = { Text(
                    text = "Your feedback (optional)",
                    color = DarkPurple,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.background(Color.White)
                )},
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.55f)
                    .padding(horizontal = 15.dp),
                maxLines = 4,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = LightPurple,
                    unfocusedLabelColor = MidPurple
                )
            )

            // Submit/Cancel Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        homeVM.submitReview(sharedVM.id.value)
                        homeVM.onFeedbackVisiChange()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    shape = RectangleShape,
                    modifier = Modifier
                        .width(175.dp)
                        .height(40.dp)
                ) {
                    Text(
                        text = "Submit",
                        color = DarkPurple,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Button(
                    onClick = {
                        homeVM.onFeedbackVisiChange()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    shape = RectangleShape,
                    modifier = Modifier
                        .width(175.dp)
                        .height(40.dp)
                ){
                    Text(
                        text = "Maybe Later",
                        color = DarkPurple,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun StarRatingBar(
    homeVM: HomeVM,
    stars: Int = 5,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(stars) { index ->
            Icon(
                imageVector = if (index < homeVM.rating.value) Icons.Default.Star else Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { homeVM.onRatingChange(index + 1)},
                tint = if (index < homeVM.rating.value) Color(0xFFFFD700) else Color.LightGray
            )
        }
    }
}
