package com.example.libraryapp.ui

import android.app.ProgressDialog
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RangeSlider
import androidx.compose.material.SliderColors
import androidx.compose.material.SliderDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.libraryapp.ui.theme.AnimatedRatingBar
import com.example.libraryapp.ui.theme.GreenApp
import com.example.libraryapp.ui.theme.GreenAppOpacity
import com.example.libraryapp.ui.theme.rojoSangre
import com.example.libraryapp.ui.theme.rositaGracioso
import com.example.libraryapp.ui.theme.sendComment
import com.example.libraryapp.viewModel.CategoryViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterialApi
fun CategoryView (navController: NavController, ViewModel: CategoryViewModel,categoria: String ){

    //val selectedCategory by rememberUpdatedState(newValue = ViewModel.selectedCategory)
    //ViewModel.updateSelectedCategory(categoria)
    val executedOnce = remember { mutableSetOf<Boolean>() }
    val context = LocalContext.current
    val loading by ViewModel.loading.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp)
        ){
            BookCategoriesDropdown(
                categories = ViewModel.categories,
                selectedCategory = ViewModel.selectedCategory,
                onCategorySelected = { category ->
                    ViewModel.updateSelectedCategory(category)
                }
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .width(160.dp)
                    .height(35.dp)
                    .clickable {
                        showDialog = true
                    }
                    .border(width = 1.dp, color = Color(0xFF000000))
                    .align(alignment = Alignment.CenterEnd)
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxSize()
                    //               .padding(16.dp)
                ){
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(imageVector = Icons.Default.Checklist, contentDescription = null)
                    Text(text = "Filtrar")
                }
            }
        }
        if (showDialog) {
            filtrado(ViewModel,onDismiss = { showDialog = false })
        }
        booksortDropdown(
            selectedShort= ViewModel.selectedShort,
            onShortSelected = { short ->
                ViewModel.updateShort(short)
            }
        )

        for (i in 0 until ViewModel.filtrados.size){
            BookPreviewWide(ViewModel.filtrados[i],navController, ViewModel)
        }
    }
}

@Composable
fun booksortDropdown(
    selectedShort: String?,
    onShortSelected: (String) -> Unit,
) {
    val categories = listOf("Precio ascendente", "Precio descendente", "Mas valorados", "Título A-Z", "Título Z-A")
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(start = 10.dp, top = 10.dp, end = 10.dp)
            .fillMaxWidth()
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
            Text(text = selectedShort?: "Select Short")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(text = category)},
                    onClick = {
                        onShortSelected(category)
                        expanded = false
                    }
                )
            }
        }
        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null,modifier = Modifier.align(alignment = Alignment.TopEnd).padding(10.dp))
    }
}


@Composable
@ExperimentalMaterialApi
fun filtrado(viewModel: CategoryViewModel ,onDismiss: () -> Unit){
    val range = 1.0f..60.0f
    val steps = 11
    var price by remember { mutableStateOf(20f..40.0f) }
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .shadow(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                ),
                shape = RoundedCornerShape(10.dp),
                ) {
                Text(
                    text = "Precio: ${price.start.toInt()}€ - ${price.endInclusive.toInt()}€",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFF000000),
                    ),
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .padding(top = 15.dp)
                )
                RangeSlider(
                    value = price,
                    valueRange = range,
                    steps = steps,
                    onValueChange = { price = it },
                    modifier = Modifier
                        .width(250.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                        .padding(vertical = 10.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = GreenApp,
                        activeTrackColor = GreenAppOpacity,
                        inactiveTrackColor = Color.LightGray.copy(alpha = 0.24f),
                        activeTickColor = GreenAppOpacity,
                        inactiveTickColor = Color.LightGray.copy(alpha = 0.24f)
                    )

                )
                Button(
                    onClick = {
                        viewModel.updateFiltrados(price.start.toInt(),price.endInclusive.toInt())
                        onDismiss()
                    },
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .padding(bottom = 10.dp),
                    colors= ButtonDefaults.buttonColors(
                        containerColor= GreenApp),
                ){
                    Text("Aplicar")
                }
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
            .width(160.dp)
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
