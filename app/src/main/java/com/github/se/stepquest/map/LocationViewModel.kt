package com.github.se.stepquest.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.lifecycle.*
import com.google.android.gms.location.*

data class LocationDetails(val latitude: Double, val longitude: Double)

data class Checkpoint(val name: String, val location: LocationDetails)

class LocationViewModel : ViewModel() {
  var locationCallback: LocationCallback? = null
  var fusedLocationClient: FusedLocationProviderClient? = null
  var currentLocation = MutableLiveData<LocationDetails>()
  var _allocations = MutableLiveData<List<LocationDetails>?>()
  var locationUpdated = MutableLiveData<Boolean>()
  var checkpoints = MutableLiveData<List<Checkpoint>>()
  var create_route_start = MutableLiveData<Boolean>()
  val isFollowingRoute = MutableLiveData<Boolean>(false)

  init {
    locationUpdated.postValue(false)
    create_route_start.postValue(false)
  }

  @SuppressLint("MissingPermission")
  fun startLocationUpdates(context: Context) {
    if (fusedLocationClient == null) {
      fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }
    locationCallback =
        object : LocationCallback() {
          override fun onLocationResult(p0: LocationResult) {
            for (lo in p0.locations) {
              // Update UI with location data
              currentLocation.value = LocationDetails(lo.latitude, lo.longitude)

              // TODO: delete this fake location
              //              currentLocation.value=fakeRoute(currentLocation.value!!)

              if (create_route_start.value == true) {
                val updatedValues =
                    appendCurrentLocationToAllocations(
                        _allocations.value ?: emptyList(),
                        currentLocation.value!!,
                        locationUpdated.value!!)
                if (updatedValues != null) {
                  val (updatedAllocations, updatedLocation) = updatedValues
                  _allocations.postValue(updatedAllocations)
                  locationUpdated.postValue(updatedLocation)
                }
              }
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

  fun appendCurrentLocationToAllocations(
      currentAllocations: List<LocationDetails>,
      current: LocationDetails,
      updatelocation: Boolean
  ): Pair<List<LocationDetails>, Boolean>? {
    val last = currentAllocations.lastOrNull()

    if ((last == null || calculateDistance(last, current) > 1) && !updatelocation) {
      val alllocation = currentAllocations + current
      val update = true
      return Pair(alllocation, update)
    }
    return null
  }

  //  //TODO: Delete this fake route
  //  //    // Here is for testing purposes: create a faking route by adding each time 1.2 meters to
  // the previous location
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
  //            Math.cos(latRadians)))
  //    i+=1
  //    println("i: $i")
  //    return LocationDetails(newLatitude, newLongitude)
  //  }

  fun getAllocations(): List<LocationDetails>? {
    return _allocations.value
  }

  fun addNewCheckpoint(name: String): Boolean {
    val newCheckpointList = checkpoints.value?.toMutableList() ?: mutableListOf()
    val currLocation = currentLocation.value
    return if (currLocation == null) {
      false
    } else {
      val newCheckpoint = Checkpoint(name, currentLocation.value!!)
      newCheckpointList.add(newCheckpoint)
      checkpoints.postValue(newCheckpointList)
      true
    }
  }

  fun cleanAllocations() {
    _allocations.postValue(null)
  }

  // stop update location
  fun onPause() {
    if (fusedLocationClient == null || locationCallback == null) {
      return
    } else {
      locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }
    }
  }
}

// Function to calculate distance between two locations in meters
fun calculateDistance(location1: LocationDetails, location2: LocationDetails): Float {
  val results = FloatArray(1)
  Location.distanceBetween(
      location1.latitude, location1.longitude, location2.latitude, location2.longitude, results)
  return results[0]
}
