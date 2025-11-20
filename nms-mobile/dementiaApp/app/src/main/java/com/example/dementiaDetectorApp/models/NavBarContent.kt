package com.example.dementiaDetectorApp.models

import androidx.annotation.DrawableRes

data class NavBarContent(
        val title: String,
        @DrawableRes val iconId:Int,
        val route: String
    )