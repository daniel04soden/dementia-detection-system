package com.example.dementiaDetectorApp.ui.composables

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri

@Composable
fun HomePageBox(header: String, description: String, linkText: String, link: String, linkAction: ()-> Unit) {
    Box(
        modifier = Modifier
            .fillMaxHeight(1F)
            .width(175.dp)
            .background(color = Color.Cyan)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(1F)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight(0.2F)
                    .fillMaxWidth(1F),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(header, fontSize = 20.sp, color = Color.Magenta)
            }
            Row(
                modifier = Modifier
                    .fillMaxHeight(0.70F)
                    .fillMaxWidth(1F),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(description, fontSize = 12.sp, color = Color.Black)
            }
            val regex =
                Regex("https?://[\\w.-]+(?:\\.[\\w.-]+)+[/\\w.-]*") //Will be a match for most formats of URL
            if (regex.containsMatchIn(link)) { //If link is a URL
                val ctx = LocalContext.current
                val urlIntent = Intent(
                    Intent.ACTION_VIEW,
                    link.toUri()
                )

                Row(
                    modifier = Modifier
                        .fillMaxSize(1F)
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxSize(1F),
                        shape = RectangleShape,
                        onClick = {
                            ctx.startActivity(urlIntent)
                        }
                    ) {
                        Text(linkText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            } else { //Else is a local link to another screen
                Row(
                    modifier = Modifier
                        .fillMaxSize(1F)
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxSize(1F),
                        shape = RectangleShape,
                        onClick = {
                            //navController shenangins
                            linkAction()
                        }
                    ) {
                        Text(linkText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}