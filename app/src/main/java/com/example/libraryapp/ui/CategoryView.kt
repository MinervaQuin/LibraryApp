package com.example.libraryapp.ui

import android.app.ProgressDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.libraryapp.ui.theme.GreenApp
import com.example.libraryapp.viewModel.CategoryViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CategoryView (navController: NavController, ViewModel: CategoryViewModel,categoria: String ){
    //val selectedCategory by rememberUpdatedState(newValue = ViewModel.selectedCategory)
    //ViewModel.updateSelectedCategory(categoria)
    val executedOnce = remember { mutableSetOf<Boolean>() }
    val context = LocalContext.current
    val loading by ViewModel.loading.collectAsState()

    if (executedOnce.add(true)) {
        ViewModel.updateSelectedCategory(categoria)
    }

    if (loading) {
        DisposableEffect(Unit) {
            val progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Cargando...")
            progressDialog.setMessage("Por favor, espere")
            progressDialog.setCancelable(true)
            progressDialog.show()

            onDispose {
                progressDialog.dismiss()
            }
        }
    }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
    )
    {
        ViewModel.selectedCategory?.let {
            Text(
                text = it,
                style = TextStyle(
                    fontSize = 43.sp,
                    fontWeight = FontWeight(700),
                    color = GreenApp,
                    textAlign = TextAlign.Center,
                ) ,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(20.dp)
            )
        }
        Box(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .height(2.dp)
                .background(Color.Black)
        )
        Text(
            text = "Últimas Novedades",
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight(700),
                color = Color(0xFF000000),
            ),
            modifier = Modifier
                .align(alignment = Alignment.Start)
                .padding(10.dp)
        )
        Box(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .height(2.dp)
                .background(Color.Black)
        )
        LazyRow(){
            items(1){
                for (i in 0 until ViewModel.novedades.size){
                    ViewModel.novedades[i]?.let { it1 -> BookPreview(it1,navController, ViewModel) }
                }
            }
        }
        BookCategoriesDropdown(
            categories = ViewModel.categories,
            selectedCategory = ViewModel.selectedCategory,
            onCategorySelected = { category ->
                ViewModel.updateSelectedCategory(category)
            }
        )
        for (i in 0 until ViewModel.filtrados.size){
            BookPreviewWide(ViewModel.filtrados[i],navController, ViewModel)
        }
    }
}

@Composable
fun BookCategoriesDropdown(
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    // Dropdown menu
    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .width(149.dp)
            .height(35.dp)
            .clickable {
                expanded = !expanded
            }
            .border(width = 1.dp, color = Color(0xFF000000))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
 //               .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(imageVector = Icons.Default.List, contentDescription = null)
            Text(text = selectedCategory ?: "Select Category")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .height(400.dp)
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(text = category)},
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                }
                )
            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun PreviewCategory() {
    val navController = rememberNavController()
    CategoryView(navController, CategoryViewModel(), "Ficción")
}*/
//ComposeQuadrantApp()
