package com.github.se.stepquest.ui.navigation

import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

object Route {
    const val MAP = "map"
}

data class TopLevelDestination(
    val route: String,
    val icon: @Composable (() -> Unit)? = null,
    val textId: Int? = null
)

//val TOP_LEVEL_DESTINATIONS =
//    listOf(
//        TopLevelDestination(
//            Route.OVERVIEW,
//            { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "overview") },
//            R.string.overview),
//        TopLevelDestination(
//            Route.MAP, { Icon(Icons.Default.Search, contentDescription = "map") }, R.string.map),
//    )

class NavigationActions(private val navController: NavHostController) {
    fun navigateTo(destination: TopLevelDestination, uid: String? = null) {
        val routeWithUid =
            if (uid != null) {
                "${destination.route}/$uid"
            } else {
                destination.route
            }
        navController.navigate(routeWithUid) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    fun goBack() {
        navController.navigateUp()
    }
}
