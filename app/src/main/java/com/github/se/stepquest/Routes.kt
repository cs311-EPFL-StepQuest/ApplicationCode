package com.github.se.stepquest

open class Routes(val routName: String, val title: String) {
  object LoginScreen : Routes(routName = "/login", title = "Login")

  object HomeScreen : Routes(routName = "/home", title = "Home")

  object MapScreen : Routes(routName = "/map", title = "Map")

  object ProgressionScreen : Routes(routName = "/progression", title = "Progression")

  object MainScreen : Routes(routName = "/main", title = "Main")
}
