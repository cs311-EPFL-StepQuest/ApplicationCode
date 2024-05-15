package com.github.se.stepquest.map

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class FollowRoute() {
  var userOnRoute = MutableLiveData<Boolean>()
  // Define a Job to manage the coroutine
  private var checkRouteJob: Job? = null

  // taking the points on the route with the interval of 20 meters
  fun createTrackpoint(
      route: List<LocationDetails>,
      interval: Double = 20.0
  ): List<LocationDetails> {
    val trackpoints = mutableListOf<LocationDetails>()
    var currentLocation = route[0]
    var totaldistance = 0.0
    for (i in 1 until route.size) {
      val distance = calculateDistance(currentLocation, route[i])
      totaldistance += distance
      if (totaldistance >= interval) {
        trackpoints.add(route[i])
        totaldistance = 0.0
      }
      currentLocation = route[i]
    }
    return trackpoints
  }

  fun checkIfOnRoute(
      locationViewModel: LocationViewModel,
      trackpoints: List<LocationDetails>,
      threshold: Double = 30.0
  ) {
    // Cancel any existing job before starting a new one
    checkRouteJob?.cancel()

    // Launch a new coroutine in the background
    checkRouteJob =
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
          var remainingTrackpoints = trackpoints
          while (remainingTrackpoints.isNotEmpty() &&
              isActive) { // Check if the coroutine is still active
            val trackpoint = remainingTrackpoints[0]
            val currentLocation = locationViewModel.currentLocation.value
            if (currentLocation == null) {
              withContext(Dispatchers.Main) {
                Log.d("FollowRoute", "Current location is null, stopping the coroutine")
              }
            } else {
              val distance = calculateDistance(trackpoint, currentLocation)
              // If the distance is less than 5 meters, the user has passed this point
              if (distance <= 5) {
                withContext(Dispatchers.Main) {
                  userOnRoute.value = true
                  Log.d("FollowRoute", "You have reached the next point")
                }
                remainingTrackpoints = remainingTrackpoints.drop(1)
              } else if (distance <= threshold && distance > 5) {
                withContext(Dispatchers.Main) {
                  userOnRoute.postValue(true)
                  Log.d("FollowRoute", "You are on the route")
                }
              } else {
                withContext(Dispatchers.Main) {
                  userOnRoute.postValue(false)
                  Log.d("FollowRoute", "You are off the route")
                }
              }
            }
            // Optionally, add a delay to prevent continuous checking
            delay(1000)
          }
        }
  }

  // Function to stop the checkIfOnRoute coroutine
  fun stopCheckIfOnRoute() {
    checkRouteJob?.cancel()
  }
}
