package com.github.se.stepquest.map

fun compareImages(referenceLocation: LocationDetails, newImageLocation: LocationDetails): Float {
  val distance = calculateDistance(referenceLocation, newImageLocation)
  if (distance >= 20f) {
    return 0f
  }
  return distance
}
