package com.example.libraryapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.libraryapp.R
import com.example.libraryapp.model.resources.Author
import com.example.libraryapp.model.resources.Book
import com.example.libraryapp.ui.theme.GreenApp
import com.example.libraryapp.viewModel.CategoryViewModel
import java.lang.Math.floor
@Composable
fun CategoryView (navController: NavController, ViewModel: CategoryViewModel){
    //val selectedCategory by rememberUpdatedState(newValue = ViewModel.selectedCategory)
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
    )
    {
        Spacer(modifier = Modifier.height(55.dp))
        ViewModel.selectedCategory?.let {
            Text(
                text = it,
                style = TextStyle(
                    fontSize = 48.sp,
                    fontWeight = FontWeight(700),
                    color = GreenApp,
                    textAlign = TextAlign.Center,
                ) ,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(25.dp)
            )
        }
        Box(
            modifier = Modifier
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
                .fillMaxWidth()
                .height(2.dp)
                .background(Color.Black)
        )
        LazyRow(){
            items(1){
                for (i in 0 until ViewModel.novedades.size){
                    BookPreview(ViewModel.novedades[i])
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
            BookPreviewWide(ViewModel.filtrados[i])
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
            .width(149.dp)
            .height(27.dp)
            .clickable {
                expanded = !expanded
            }
            .border(width = 1.dp, color = Color(0xFF000000))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
 //               .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(imageVector = Icons.Default.List, contentDescription = null)
            Text(text = selectedCategory ?: "Select Category")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
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

@Preview(showBackground = true)
@Composable
fun PreviewCategory() {
    val navController = rememberNavController()
    CategoryView(navController , CategoryViewModel())
}
//ComposeQuadrantApp()
