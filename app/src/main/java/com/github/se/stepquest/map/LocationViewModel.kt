package com.github.se.stepquest.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.google.android.gms.location.*

data class LocationDetails(val latitude: Double, val longitude: Double)



class LocationViewModel : ViewModel() {
  private var locationCallback: LocationCallback? = null
  private var fusedLocationClient: FusedLocationProviderClient? = null
  private val locationRequired = MutableLiveData<Boolean>()
  val currentLocation = MutableLiveData<LocationDetails>()

  init {
    locationRequired.value = false
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
}

