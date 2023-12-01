package com.example.libraryapp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.libraryapp.theme.gray
import com.example.libraryapp.theme.green
import androidx.navigation.NavController
import com.example.libraryapp.theme.white
import com.example.libraryapp.ui.theme.GreenAppOpacity


@Composable
fun BottomBar(navController: NavController){
    var currentRoute by remember { mutableStateOf("") }
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            currentRoute = destination.route.toString()
        }
        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
    BottomAppBar(
        containerColor = GreenAppOpacity,
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp),
        content= {
            BottomNavigationItem(
                icon = {
                    Icon(Icons.Default.Person,
                        contentDescription = "profile",
                        tint = if (currentRoute == "profile") white else gray,
                        modifier = Modifier.size(32.dp)) },
                selected = navController.currentDestination?.route == "profile",
                onClick = {
                    navController.navigate("profile")
                }
            )
            BottomNavigationItem(
                icon = {
                    Icon(Icons.Default.Home,
                        contentDescription = "homePage",
                        tint = if (currentRoute == "homePage") white else gray,
                        modifier = Modifier.size(32.dp)) },
                selected = navController.currentDestination?.route == "homePage",
                onClick = {
                    navController.navigate("homePage")
                }
            )
            BottomNavigationItem(
                icon = {
                    Icon(Icons.Default.LocationOn,
                        contentDescription = "location",
                        tint = if (currentRoute == "map") white else gray,
                        modifier = Modifier.size(32.dp)) },
                selected = navController.currentDestination?.route == "map",
                onClick = {
                    navController.navigate("map")
                }
            )
        }
    )
}
