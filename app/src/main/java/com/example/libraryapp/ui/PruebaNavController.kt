package com.example.libraryapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun NavigationController(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "SearchView"  ){
        composable("SearchView"){
            ListOfBooks(navController = navController)
        }
        composable("BookDetailsView"){
            BookDetailsScreen(navController = navController)
        }
    }
}