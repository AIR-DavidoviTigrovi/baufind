package hr.foi.air.baufind.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
            route = "jobDetailsScreen",
            icon = IconType.Vector(imageVector = Icons.Default.Add),
            label = "Add job"),
            BottomNavItem(
            route = "jobSearchScreen",
            icon = IconType.Vector(imageVector = Icons.Default.Search), label = "Search jobs"),
            BottomNavItem(
            route = "myUserProfileScreen",
            icon = IconType.Vector(imageVector = Icons.Default.Person), label = "Profile"),


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
