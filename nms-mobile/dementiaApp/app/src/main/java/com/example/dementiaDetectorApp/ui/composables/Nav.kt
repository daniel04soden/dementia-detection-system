package com.example.dementiaDetectorApp.ui.composables

import com.example.dementiaDetectorApp.R
import com.example.dementiaDetectorApp.models.NavBarContent
import com.example.dementiaDetectorApp.ui.theme.Purple40
import com.example.dementiaDetectorApp.ui.theme.Purple80

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dementiaDetectorApp.ui.theme.DarkPurple
import com.example.dementiaDetectorApp.ui.theme.LightPurple
import com.example.dementiaDetectorApp.ui.theme.MidPurple
import com.example.dementiaDetectorApp.viewModels.SharedVM

@Composable
fun NavMenu(
    sharedVM: SharedVM,
    nc: NavController,
    modifier: Modifier = Modifier
) {
    val items = sharedVM.navItems.value
    Row(modifier=modifier
        .fillMaxWidth()
        .background(Color.White)//CHANGE
        .padding(15.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ){
        items.forEachIndexed{index, item ->
            NavBarItem(
                item=item,
                isSelected = index==sharedVM.navIndex.value,
                activeHighlight = LightPurple,
                activeTextColor = DarkPurple,
                inactiveTextColor = LightPurple,
                nc = nc
            ){
                sharedVM.onNavIndexChange(index)
            }
        }
    }
}

@Composable
fun NavBarItem(
    item: NavBarContent,
    isSelected: Boolean = false,
    activeHighlight: Color = DarkPurple,//CHANGE
    activeTextColor: Color = LightPurple,//CHANGE
    inactiveTextColor: Color = DarkPurple,//CHANGE
    nc: NavController,
    onItemClick: () -> Unit
){
    Column(modifier = Modifier
        .clickable{
            onItemClick()
            nc.navigate(item.route)
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Box(modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if(isSelected) activeHighlight else Color.Transparent)
            .padding(10.dp),
            contentAlignment = Alignment.Center
        ){
            Icon(
                painter = painterResource(R.drawable.search),
                contentDescription = item.title,
                tint = (if(isSelected) activeTextColor else inactiveTextColor),
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = item.title,
            color = (if (isSelected) activeTextColor else inactiveTextColor)
        )
    }
}