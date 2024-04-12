package com.github.se.stepquest.map

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.google.android.gms.location.*

data class LocationDetails(val latitude: Double, val longitude: Double)

class LocationViewModel : ViewModel() {
  private var locationCallback: LocationCallback? = null
  private var fusedLocationClient: FusedLocationProviderClient? = null
  val locationRequired = MutableLiveData<Boolean>()
  val currentLocation = MutableLiveData<LocationDetails>()
  private val _allocations = MutableLiveData<List<LocationDetails>>()
  val locationUpdated = MutableLiveData<Boolean>()

  init {
    locationRequired.postValue(false)
    locationUpdated.postValue(false)
  }

  @SuppressLint("MissingPermission")
  fun startLocationUpdates(context: ComponentActivity) {
    if (fusedLocationClient == null) {
      fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }
    locationCallback =
        object : LocationCallback() {
          override fun onLocationResult(p0: LocationResult) {
            for (lo in p0.locations) {
              // Update UI with location data
              currentLocation.value = LocationDetails(lo.latitude, lo.longitude)
              //              println("Location in view: ${currentLocation.value}")
              appendCurrentLocationToAllocations()
              //              println("Allocations: ${_allocations.value}")
            }
          }
        }

    locationCallback?.let {
      val locationInterval = 100
      val locationFastestInterval = 50
      val locationRequest =
          LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, locationInterval.toLong())
              .setWaitForAccurateLocation(false)
              .setMinUpdateIntervalMillis(locationFastestInterval.toLong())
              .build()

      fusedLocationClient?.requestLocationUpdates(locationRequest, it, Looper.getMainLooper())
    }
  }

  fun appendCurrentLocationToAllocations() {
    val currentAllocations = _allocations.value ?: emptyList()
    var current = currentLocation.value
    val last = currentAllocations.lastOrNull()

    //  // Here is for testing purposes: create a faking route by adding each time 1.2 meters to the
    // previous location
    //    current=fakeRoute(current!!)

    if (current != null &&
        (last == null || calculateDistance(last, current) > 1) &&
        locationUpdated.value == false) {
      _allocations.value = currentAllocations + current
      locationUpdated.value = true
    }
  }

  //  // Here is for testing purposes: create a faking route by adding each time 1.2 meters to the
  // previous location
  //  var i = 0
  //  fun fakeRoute(current: LocationDetails): LocationDetails {
  //
  //    // Calculate new latitude and longitude with a distance of 1 meter
  //    val latRadians = Math.toRadians(current.latitude)
  //    val lonRadians = Math.toRadians(current.longitude)
  //    val earthRadius = 6371000 // Earth's radius in meters
  //    val meterIncrement = 1.2*i // Increment distance in meters
  //
  //    val newLatitude = Math.toDegrees(latRadians + meterIncrement / earthRadius)
  //    val newLongitude = Math.toDegrees(lonRadians + meterIncrement / (earthRadius *
  // Math.cos(latRadians)))
  //    i+=1
  //    println("i: $i")
  //    return LocationDetails(newLatitude, newLongitude)
  //  }

  fun getAllocations(): List<LocationDetails>? {
    return _allocations.value
  }

  fun onResume(context: ComponentActivity) {
    locationRequired.value?.let {
      if (it) {
        startLocationUpdates(context)
      }
    }
  }

  fun onPause() {
    locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }
  }

  // Function to calculate distance between two locations in meters
  private fun calculateDistance(location1: LocationDetails, location2: LocationDetails): Float {
    val results = FloatArray(1)
    Location.distanceBetween(
        location1.latitude, location1.longitude, location2.latitude, location2.longitude, results)
    return results[0]
  }
}
