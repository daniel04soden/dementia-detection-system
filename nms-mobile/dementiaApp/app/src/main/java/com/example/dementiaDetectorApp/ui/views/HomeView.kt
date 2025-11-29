package com.example.dementiaDetectorApp.ui.views

import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.example.dementiaDetectorApp.R
import com.example.dementiaDetectorApp.models.NewsPiece
import com.example.dementiaDetectorApp.models.Test
import com.example.dementiaDetectorApp.ui.composables.NavMenu
import com.example.dementiaDetectorApp.ui.theme.DarkPurple
import com.example.dementiaDetectorApp.ui.theme.Gray
import com.example.dementiaDetectorApp.ui.theme.LightPurple
import com.example.dementiaDetectorApp.ui.theme.MidPurple
import com.example.dementiaDetectorApp.ui.util.standardQuadFromTo
import com.example.dementiaDetectorApp.viewModels.HomeVM
import com.example.dementiaDetectorApp.viewModels.SharedVM

@Composable
fun HomeScreen(homeVM: HomeVM, sharedVM: SharedVM, nc: NavController){
    Box(modifier = Modifier
        .background(Color.White)
        .fillMaxSize()
    ){
        Column{
            Spacer(Modifier.height(35.dp))
            Header(sharedVM)
            HeaderPrompts(sharedVM, nc)
            NewsSection(homeVM)
        }
        NavMenu(
            sharedVM = sharedVM,
            nc = nc,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun Header(sharedVM: SharedVM){
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
            Text("Hello ${sharedVM.account.value.fName}", color = DarkPurple, fontSize = 25.sp)
            Text("What can we do for you today", color = MidPurple, fontSize = 18.sp)
        }
    }
}

@Composable
fun HeaderPrompts(
    sharedVM: SharedVM,
    nc: NavController
){
    val prompts: List<Test> = sharedVM.tests.value
    Column(Modifier.fillMaxWidth()){
        LazyRow(
            contentPadding = PaddingValues(start = 7.5.dp, end = 7.5.dp)
        ){
            items(prompts.size){
                Box(Modifier.width(350.dp)){
                    TestPrompt(test=prompts[it], nc)
                }
            }
        }
    }
}

@Composable
fun TestPrompt(
    test: Test,
    nc: NavController
){
    Row(modifier = Modifier
        .padding(15.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(MidPurple)//CHANGE
        .padding(horizontal = 15.dp , vertical = 20.dp)
        .fillMaxWidth()
        .clickable {nc.navigate(test.route)},
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(LightPurple)//CHANGE
                .padding(5.dp),
            contentAlignment = Alignment.Center
        ){
            Icon(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Class Icon",
                Modifier.size(30.dp)
            )
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text("Test ${test.step}", color = Color.White)
            Text(test.name, color = Color.White)
        }
    }
}

@Composable
fun NewsSection(homeVM: HomeVM){
    val news = homeVM.news.value
    Column(modifier = Modifier.fillMaxWidth())
    {
        Text(
            text="News Pieces",
            color = MidPurple,
            //style=
            fontSize = 20.sp,
            modifier = Modifier.padding(15.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 7.5.dp, end = 7.5.dp, bottom = 100.dp),
            modifier=Modifier.fillMaxHeight()
        ){
            items(news.size){
                NewsBox(news=news[it])
            }
        }
    }
}

@Composable
fun NewsBox(
    news: NewsPiece
){
    val ctx = LocalContext.current
    val urlIntent = Intent(
        Intent.ACTION_VIEW,
        news.link.toUri()
    )
    BoxWithConstraints(
        modifier = Modifier
            .padding(7.5.dp)
            .aspectRatio(1F)
            .clip(RoundedCornerShape(10.dp))
            .background(DarkPurple)
            .clickable {ctx.startActivity(urlIntent)}
    ){
        //Colour waves
        val width = constraints.maxWidth
        val height = constraints.maxHeight

        //Medium coloured path
        val mediumColoredPoint1 = Offset(0f, height * 0.3f)
        val mediumColoredPoint2 = Offset(width * 0.1f, height * 0.35f)
        val mediumColoredPoint3 = Offset(width * 0.4f, height * 0.05f)
        val mediumColoredPoint4 = Offset(width * 0.75f, height * 0.7f)
        val mediumColoredPoint5 = Offset(width * 1.4f, -height.toFloat())

        val mediumColoredPath = Path().apply {
            moveTo(mediumColoredPoint1.x, mediumColoredPoint1.y)
            standardQuadFromTo(mediumColoredPoint1, mediumColoredPoint2)
            standardQuadFromTo(mediumColoredPoint2, mediumColoredPoint3)
            standardQuadFromTo(mediumColoredPoint3, mediumColoredPoint4)
            standardQuadFromTo(mediumColoredPoint4, mediumColoredPoint5)
            lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
            lineTo(-100f, height.toFloat() + 100f)
            close()
        }

        // Light colored path
        val lightPoint1 = Offset(0f, height * 0.35f)
        val lightPoint2 = Offset(width * 0.1f, height * 0.4f)
        val lightPoint3 = Offset(width * 0.3f, height * 0.35f)
        val lightPoint4 = Offset(width * 0.65f, height.toFloat())
        val lightPoint5 = Offset(width * 1.4f, -height.toFloat() / 3f)

        val lightColoredPath = Path().apply {
            moveTo(lightPoint1.x, lightPoint1.y)
            standardQuadFromTo(lightPoint1, lightPoint2)
            standardQuadFromTo(lightPoint2, lightPoint3)
            standardQuadFromTo(lightPoint3, lightPoint4)
            standardQuadFromTo(lightPoint4, lightPoint5)
            lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
            lineTo(-100f, height.toFloat() + 100f)
            close()
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            drawPath(
                path = mediumColoredPath,
                color = MidPurple
            )
            drawPath(
                path = lightColoredPath,
                color = LightPurple
            )
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
        ){
            Column(verticalArrangement = Arrangement.SpaceBetween){
                Text(
                    text = news.headline,
                    lineHeight = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .offset(0.dp, -10.dp)
                )
                HorizontalDivider(thickness = 1.dp, color = Color.White)
                Text(
                    text = news.description,
                    lineHeight = 13.sp,
                    color = Gray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .offset(0.dp,10.dp)
                )
            }
        }
    }
}