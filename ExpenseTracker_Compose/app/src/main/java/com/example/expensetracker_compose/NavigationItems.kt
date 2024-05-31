package com.example.expensetracker_compose

import com.example.expensetracker_compose.R

sealed class NavigationItem(var route: String, var icon: Int, var title: String){
    data object Home: NavigationItem("home",R.drawable.home_icon,"Home")
    data object History: NavigationItem("history",R.drawable.history_icon,"History")
    data object Settings: NavigationItem("stettings",R.drawable.ic_launcher_foreground,"Settings")
}

