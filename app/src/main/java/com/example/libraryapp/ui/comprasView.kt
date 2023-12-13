package com.example.libraryapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.libraryapp.model.resources.Order
import com.example.libraryapp.viewModel.comprasViewModel

@Composable
fun comprasView(navController: NavController, viewModel : comprasViewModel){
    Column {

    }
}

@Composable
fun orders(order: Order){
    var open by remember { mutableStateOf(false) }
    Column(
        modifier= Modifier
            .fillMaxWidth()
            .height(55.dp)
    ){
        Row(){
            Text(
                text = order.orderId
            )
        }
    }
}