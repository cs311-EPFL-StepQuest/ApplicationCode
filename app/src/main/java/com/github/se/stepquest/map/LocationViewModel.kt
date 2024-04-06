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
    locationRequired.value = false
    locationUpdated.value = false
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
              println("Location in view: ${currentLocation.value}")
              appendCurrentLocationToAllocations()
              println("Allocations: ${_allocations.value}")
            }
          }
        }

    locationCallback?.let {
      val locationRequest =
          LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
          }
      fusedLocationClient?.requestLocationUpdates(locationRequest, it, Looper.getMainLooper())
    }
  }

  fun appendCurrentLocationToAllocations() {
    val currentAllocations = _allocations.value ?: emptyList()
    val current = currentLocation.value
    val last = currentAllocations.lastOrNull()

    if (current != null && (last == null || calculateDistance(last, current) > 1)) {
      _allocations.value = currentAllocations + current
      locationUpdated.value = true
    }
  }

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
