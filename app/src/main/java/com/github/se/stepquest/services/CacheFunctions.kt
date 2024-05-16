package com.github.se.stepquest.services

import android.content.Context
import android.net.ConnectivityManager

fun isOnline(context: Context): Boolean {

  val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

  val networkInfo = manager.activeNetworkInfo

  return networkInfo != null && networkInfo.isConnected
}

fun cacheUserInfo(context: Context, userId: String, username: String) {

  val sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)

  val editor = sharedPreferences.edit()
  editor.putString("userId", userId)
  editor.putString("username", username)
  editor.apply()
}

fun getCachedInfo(context: Context): Pair<String, String>? {

  val sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)

  val userId = sharedPreferences.getString("userId", null)
  val username = sharedPreferences.getString("username", null)

  return if (userId != null && username != null) Pair(userId, username) else null
}

fun cacheStepGoals(context: Context, dailyStepGoal: Int, weeklyStepGoal: Int) {
  val sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
  val editor = sharedPreferences.edit()
  editor.putInt("dailyStepGoal", dailyStepGoal)
  editor.putInt("weeklyStepGoal", weeklyStepGoal)
  editor.apply()
}

fun cacheSteps(context: Context, dailySteps: Int, weeklySteps: Int) {
  val sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
  val editor = sharedPreferences.edit()
  editor.putInt("dailySteps", dailySteps)
  editor.putInt("weeklySteps", weeklySteps)
  editor.apply()
}

fun getCachedStepInfo(context: Context): Map<String, Int> {
  val sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
  val dailyStepGoal = sharedPreferences.getInt("dailyStepGoal", 5000) // Default value is 5000
  val dailySteps = sharedPreferences.getInt("dailySteps", 0) // Default value is 0
  val weeklyStepGoal = sharedPreferences.getInt("weeklyStepGoal", 35000) // Default value is 35000
  val weeklySteps = sharedPreferences.getInt("weeklySteps", 0) // Default value is 0
  val totalSteps = sharedPreferences.getInt("totalSteps", 0) // Default value is 0

  return mapOf(
      "dailyStepGoal" to dailyStepGoal,
      "dailySteps" to dailySteps,
      "weeklyStepGoal" to weeklyStepGoal,
      "weeklySteps" to weeklySteps,
      "totalSteps" to totalSteps)
}
