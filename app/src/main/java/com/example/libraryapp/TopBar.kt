package com.example.libraryapp


import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ModalDrawer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.rememberDrawerState
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.libraryapp.theme.gray
import com.example.libraryapp.ui.theme.GreenAppOpacity
import kotlinx.coroutines.launch
import coil.compose.AsyncImage
import com.example.libraryapp.theme.white
import com.example.libraryapp.viewModel.ShoppingCart
import com.example.libraryapp.viewModel.topBarViewModel


data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController,drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    var isOnCartScreen by remember { mutableStateOf(false) }

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            isOnCartScreen = destination.route == "cartDestination"
        }
        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = GreenAppOpacity),
        modifier = Modifier
            .zIndex(1f),
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
                    scope.launch {
                        if (drawerState.currentValue  == DrawerValue.Closed) {
                            drawerState.open()
                        } else {
                            drawerState.close()
                        }
                    }

                }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menú",
                    tint = if(drawerState.currentValue  == DrawerValue.Closed) gray else white,
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
                    tint = if (isOnCartScreen) white else gray,
                    modifier = Modifier.size(32.dp)
                )
            }
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun drawer( navController: NavController, drawerState: DrawerState, viewModel: topBarViewModel){
    viewModel.getProfileImage()
    val context = LocalContext.current
    var isOnCartScreen by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf<NavigationItem?>(null) }
    val userData by viewModel.userData.collectAsState()
    val profilePictureUrl by  viewModel.profilePictureUrl.collectAsState()
    val items = listOf(
        NavigationItem(
            title = "Perfil",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
            route = "profile"
        ),
        NavigationItem(
            title = "Cesta",
            selectedIcon = Icons.Filled.ShoppingCart,
            unselectedIcon = Icons.Outlined.ShoppingCart,
            route = "cartDestination",
        ),
        NavigationItem(
            title = "Autores",
            selectedIcon = Icons.Filled.AccountBox,
            unselectedIcon = Icons.Outlined.AccountBox,
            route = "AutoresDestination",
        ),
        NavigationItem(
            title = "Imprescindibles",
            selectedIcon = Icons.Filled.Book,
            unselectedIcon = Icons.Outlined.Book,
            route = "Category",
        ),
        NavigationItem(
            title = "Ficción",
            selectedIcon = Icons.Filled.Book,
            unselectedIcon = Icons.Outlined.Book,
            route = "Category"
        ),
        NavigationItem(
            title = "No Ficción",
            selectedIcon = Icons.Filled.Book,
            unselectedIcon = Icons.Outlined.Book,
            route = "Category"
        ),
        NavigationItem(
            title = "Infantil",
            selectedIcon = Icons.Filled.Book,
            unselectedIcon = Icons.Outlined.Book,
            route = "Category"
        ),
        NavigationItem(
            title = "Cómic y Manga",
            selectedIcon = Icons.Filled.Book,
            unselectedIcon = Icons.Outlined.Book,
            route = "Category"
        ),
        NavigationItem(
            title = "Ayuda",
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info,
            route = "ayuda",
        ),
    )
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            selectedItem = when {
                destination.route == "Category" -> {
                    val currentCategory = ShoppingCart.getSelectedCategory()
                    items.find { it.title == currentCategory }
                }

                items.any { it.route == destination.route } -> {
                    items.find { it.route == destination.route }
                }

                else -> null
            }
            isOnCartScreen = destination.route == "cartDestination"
        }
        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    LaunchedEffect(ShoppingCart.getSelectedCategory(), navController) {
        snapshotFlow { navController.currentBackStackEntry?.destination?.route }
            .collect { currentRoute ->
                selectedItem = when {
                    items.any { it.route == currentRoute } -> {
                        items.find { it.route == currentRoute }
                    }

                    currentRoute == "Category" -> {
                        val currentCategory = ShoppingCart.getSelectedCategory()
                        items.find { it.title == currentCategory }
                    }

                    else -> null
                }
                isOnCartScreen = currentRoute == "cartDestination"
            }

        // Añade un paso adicional para limpiar selectedItem si no estamos en una ruta de item
        if (!items.any { it.route == navController.currentBackStackEntry?.destination?.route }) {
            selectedItem = null
        }
    }

    LaunchedEffect(ShoppingCart.getSelectedCategory()) {
        snapshotFlow { ShoppingCart.getSelectedCategory() }
            .collect { newCategory ->
                // Opcional: Si deseas actualizar selectedItem basado en la categoría después de verificar la ruta.
                if (navController.currentBackStackEntry?.destination?.route == "Category") {
                    selectedItem = items.find { it.title == newCategory }
                }
            }
    }

    Spacer(modifier = Modifier.height(20.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Agrega aquí la lógica para mostrar la foto de perfil del usuario
        // Puedes usar AsyncImage, Image, u otros componentes según tus necesidades
        AsyncImage(
            model = profilePictureUrl,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .border(width = 2.dp, color = Color.Black, CircleShape)
                .clickable {
                    navController.navigate("Profile")
                    scope.launch {
                        drawerState.close()
                    }
                },
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Agrega el nombre del usuario debajo de la imagen
        Text(text = userData?.userName?: "Error", fontSize = 16.sp, color = gray)

    }
    Divider(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 30.dp, end = 30.dp), color = gray, thickness = 1.dp)
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
        //.padding(bottom = 65.dp),
        //contentPadding = PaddingValues(3.dp)
    ) {
        items.forEachIndexed { index, item ->
            item {
                NavigationDrawerItem(
                    label = { Text(text = item.title) },
                    selected = item == selectedItem,
                    onClick = {
                        if(item.title != "Perfil" && item.title != "Cesta" && item.title != "Ayuda"){
                            ShoppingCart.setSelectedCategory(item.title)
                        }
                        if(item.title == "Ayuda"){
                            Toast.makeText(context, "No implementado", Toast.LENGTH_SHORT).show()
                        }
                        navController.navigate(item.route)
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (item == selectedItem) {
                                item.selectedIcon
                            } else item.unselectedIcon,
                            contentDescription = item.title,
                            tint = gray
                        )
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                if (index == 1) {
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp, end = 30.dp),
                        color = gray,
                        thickness = 1.dp,

                        )
                }
            }
        }
    }
}