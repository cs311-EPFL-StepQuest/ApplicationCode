package com.github.se.stepquest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.se.stepquest.map.LocationViewModel
import com.github.se.stepquest.map.Map
import com.github.se.stepquest.screens.DatabaseLoadingScreen
import com.github.se.stepquest.screens.FriendsListScreen
import com.github.se.stepquest.screens.HomeScreen
import com.github.se.stepquest.screens.LoginScreen
import com.github.se.stepquest.screens.NewPlayerScreen
import com.github.se.stepquest.screens.NotificationScreen
import com.github.se.stepquest.ui.navigation.NavigationActions
import com.github.se.stepquest.ui.navigation.TopLevelDestination

@Composable
fun BuildNavigationBar(navigationController: NavHostController) {
  val screens =
      listOf(
          Routes.HomeScreen,
          Routes.MapScreen,
          Routes.ProgressionScreen,
      )
  val navigationBackStack by navigationController.currentBackStackEntryAsState()
  val currentPage = navigationBackStack?.destination?.route
  val navigationActions = remember(navigationController) { NavigationActions(navigationController) }

  NavigationBar(containerColor = Color.White, contentColor = Color.Black) {
    screens.forEach { screen ->
      NavigationBarItem(
          label = { Text(text = screen.title, fontSize = 16.sp) },
          selected = currentPage == screen.routName,
          onClick = {
            navigationActions.navigateTo(destination = TopLevelDestination(screen.routName))
          },
          icon = { Icons.Filled.Home },
          colors =
              NavigationBarItemDefaults.colors(
                  unselectedTextColor = Color.Black,
                  selectedTextColor = colorResource(id = R.color.blueTheme)),
      )
    }
  }
}

@Composable
fun AppNavigationHost(
    modifier: Modifier = Modifier,
    navigationController: NavHostController = rememberNavController(),
    startDestination: String = Routes.LoginScreen.routName,
    locationviewModel: LocationViewModel = remember { LocationViewModel() }
) {
  val navigationActions = remember(navigationController) { NavigationActions(navigationController) }
  val context = LocalContext.current
  NavHost(
      modifier = modifier,
      navController = navigationController,
      startDestination = startDestination) {
        composable(Routes.LoginScreen.routName) { LoginScreen(navigationActions) }
        composable(Routes.DatabaseLoadingScreen.routName) {
          DatabaseLoadingScreen(navigationActions, context)
        }
        composable(Routes.NewPlayerScreen.routName) { NewPlayerScreen(navigationActions, context) }
        composable(Routes.MainScreen.routName) { BuildMainScreen() }
        composable(Routes.HomeScreen.routName) { HomeScreen(navigationActions) }
        composable(Routes.ProgressionScreen.routName) { ProgressionPage(IUserRepository()) }
        composable(Routes.MapScreen.routName) { Map(locationviewModel) }
        composable(Routes.ProfileScreen.routName) { ProfilePageLayout(navigationActions) }
        composable(Routes.FriendsListScreen.routName) {
          FriendsListScreen(navigationActions = navigationActions)
        }
        composable(Routes.NotificationScreen.routName) { NotificationScreen(IUserRepository()) }
      }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BuildMainScreen() {
  val navigationController: NavHostController = rememberNavController()
  Scaffold(bottomBar = { BuildNavigationBar(navigationController = navigationController) }) {
      paddingValues ->
    Box(modifier = Modifier.padding(paddingValues)) {
      AppNavigationHost(
          navigationController = navigationController,
          startDestination = Routes.HomeScreen.routName)
    }
  }
}
