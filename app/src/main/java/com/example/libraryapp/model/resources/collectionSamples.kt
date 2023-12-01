package com.example.libraryapp.model.resources

import androidx.compose.ui.graphics.Color

data class CollectionSamples(
    val title: String,
    val color: Color,
    val imageUrl: String,
    val route: String
)
data class LongCollectionSamples(
    val title: String,
    val color: Color,
    val imageRes: Int, // Cambiado de imageUrl a imageRes
    val route: String,
    val isComplete: Boolean
)
