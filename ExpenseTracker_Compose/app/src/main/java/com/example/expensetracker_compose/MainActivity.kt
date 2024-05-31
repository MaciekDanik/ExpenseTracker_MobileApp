package com.example.expensetracker_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker_compose.ui.theme.ExpenseTracker_ComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseTracker_ComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen(){
    val navController = rememberNavController()
    Scaffold(
        topBar = { TopBar()},
        bottomBar = { BottomBar(navController = navController)},
        content = {padding ->
            Box(modifier = Modifier.padding(padding)){
                Navigation(navController = navController)
            }
        }
    )
}

@Composable
fun Navigation(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = NavigationItem.Home.route){
        composable(NavigationItem.Home.route){
            HomeScreen()
        }
        composable(NavigationItem.History.route){
            HistoryScreen()
        }
        composable(NavigationItem.Settings.route){
            AdditionalScreen()
        }
    }
}

@Composable
fun TopBar(){
    TopAppBar(
        title = {
            androidx.compose.material.Text(text = stringResource(R.string.app_name),
                fontSize = 18.sp) },
        backgroundColor = colorResource(id = R.color.purple_500),
        contentColor = Color.White
    )
}

@Composable
fun BottomBar(navController: NavController){
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.History,
        NavigationItem.Settings
    )

    BottomNavigation(
        backgroundColor = colorResource(id = R.color.white),
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach{item ->
            BottomNavigationItem(
                icon = { Icon(
                    painterResource(id = item.icon),
                    contentDescription = item.title
                )},
                label = {Text(text = item.title)},
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route){
                        navController.graph.startDestinationRoute?.let {route ->
                            popUpTo(route){
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExpenseTracker_ComposeTheme {

    }
}