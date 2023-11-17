package com.example.libraryapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.libraryapp.theme.gray
import com.example.libraryapp.theme.green
import kotlinx.coroutines.launch
import coil.compose.AsyncImage
import com.example.libraryapp.theme.white


data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var selectedItem by remember { mutableStateOf<NavigationItem?>(null) }
    var isOnCartScreen by remember { mutableStateOf(false) }

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
            route = "cartDestination"
        ),
        NavigationItem(
            title = "Imprescindibles",
            selectedIcon = Icons.Filled.Book,
            unselectedIcon = Icons.Outlined.Book,
            route = "must"
        ),
        NavigationItem(
            title = "Ficción",
            selectedIcon = Icons.Filled.Book,
            unselectedIcon = Icons.Outlined.Book,
            route = "fiction"
        ),
        NavigationItem(
            title = "No Ficción",
            selectedIcon = Icons.Filled.Book,
            unselectedIcon = Icons.Outlined.Book,
            route = "not_fiction"
        ),
        NavigationItem(
            title = "Infantil",
            selectedIcon = Icons.Filled.Book,
            unselectedIcon = Icons.Outlined.Book,
            route = "kids"
        ),
        NavigationItem(
            title = "Cómic y Manga",
            selectedIcon = Icons.Filled.Book,
            unselectedIcon = Icons.Outlined.Book,
            route = "manga"
        ),
        NavigationItem(
            title = "Ofertas",
            selectedIcon = Icons.Filled.Book,
            unselectedIcon = Icons.Outlined.Book,
            route = "sales"
        ),
        NavigationItem(
            title = "Ayuda",
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info,
            route = "ayuda"
        )
    )
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            selectedItem = items.find { it.route == destination.route }
            isOnCartScreen = destination.route == "cartDestination"
        }
        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
    DismissibleNavigationDrawer(drawerContent = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, color = green)
        ) {
            DismissibleDrawerSheet {
                Spacer(modifier = Modifier.height(55.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Agrega aquí la lógica para mostrar la foto de perfil del usuario
                    // Puedes usar AsyncImage, Image, u otros componentes según tus necesidades
                    AsyncImage(
                        model = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png",
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Agrega el nombre del usuario debajo de la imagen
                    Text(text = "Nombre de Usuario", fontSize = 16.sp, color = gray)
                }
                Divider(modifier = Modifier.fillMaxWidth().padding(start = 30.dp, end = 30.dp), color = gray, thickness = 1.dp)
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.title) },
                        selected = item == selectedItem,
                        onClick = {
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
                        Divider(modifier = Modifier.fillMaxWidth().padding(start = 30.dp, end = 30.dp), color = gray, thickness = 1.dp)
                    }
                }
            }
        }
    },
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        modifier = Modifier.width(250.dp).border(5.dp, green)
    ) {

    }
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