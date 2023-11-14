package com.example.libraryapp

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.libraryapp.theme.gray
import com.example.libraryapp.theme.green

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = green),
        title = {
            Text(
                text = "Palabras en Papel",
                fontSize = 20.sp,
                color = gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    // Navega al destino del carrito
                    navController.navigate("menuDestination")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menú",
                    tint = gray,
                    modifier = Modifier.size(32.dp)
                )

            }
        },
         actions = {
            IconButton(
                onClick = {
                    // Navega al destino del menú hamburguesa
                    navController.navigate("cartDestination")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Carrito",
                    tint = gray,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    )
}