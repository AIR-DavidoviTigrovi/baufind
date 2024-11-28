package hr.foi.air.baufind.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val items = listOf(
        BottomNavItem(
            route = "workersSearchScreen",
            icon = IconType.Vector(imageVector = Icons.Default.Search),
            label = "Search"),

            BottomNavItem(
            route = "myUserProfileScreen",
            icon = IconType.Vector(imageVector = Icons.Default.Person), label = "Profile")
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    when (item.icon) {
                        is IconType.Vector -> {
                            Icon(
                                imageVector = item.icon.imageVector,
                                contentDescription = item.label
                            )
                        }
                        is IconType.Drawable -> {
                            Icon(
                                painter = painterResource(id = item.icon.drawableId),
                                contentDescription = item.label
                            )
                        }
                    }
                },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
