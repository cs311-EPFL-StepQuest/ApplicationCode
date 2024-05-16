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

fun saveStepLocally(context: Context) {
  val sharedPreferences = context.getSharedPreferences("StepCounter", Context.MODE_PRIVATE)
  val cachedSteps = sharedPreferences.getInt("cachedSteps", 0)
  val editor = sharedPreferences.edit()
  editor.putInt("cachedSteps", cachedSteps + 1)
  editor.apply()
}

fun getCachedSteps(context: Context): Int {

  val sharedPreferences = context.getSharedPreferences("StepCounter", Context.MODE_PRIVATE)

  return sharedPreferences.getInt("cachedSteps", 0)
}

fun deleteCachedSteps(context: Context) {
  val sharedPreferences = context.getSharedPreferences("StepCounter", Context.MODE_PRIVATE)

  val editor = sharedPreferences.edit()
  editor.remove("cachedSteps")
  editor.apply()
}
