package com.github.se.stepquest.map

class FollowRoute() {
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
}
