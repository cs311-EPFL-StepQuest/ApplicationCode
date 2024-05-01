package com.github.se.stepquest.map

import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sqrt

class LocationArea{
  var firebaseAuth: FirebaseAuth
  var database: FirebaseDatabase
  private lateinit var center: LatLng
  private var radius: Double = 1000.0

  init {
    firebaseAuth = FirebaseAuth.getInstance()
    database = FirebaseDatabase.getInstance()
  }

  fun createArea(centerLocation: LocationDetails, radius: Double = 1000.0) {
    this.radius = radius
    this.center = LatLng(centerLocation.latitude, centerLocation.longitude)
  }

  fun routesAroundLocation(
      googleMap: GoogleMap,
      selectedLocation: LocationDetails
  ): List<LocationDetails> {
    // Retrieve all routes from the database and check
    // if the starting point of the route is within the circle
    val routes = database.reference.child("routes")
    val routeList = mutableListOf<LocationDetails>()

    routes.get().addOnSuccessListener { snapshot ->
      for (routeID in snapshot.children) {
        val routeDataSnapshot = routeID.child("route").child("0")
        val latitude = routeDataSnapshot.child("latitude").getValue<Double>()
        val longitude = routeDataSnapshot.child("longitude").getValue<Double>()
        if (latitude != null && longitude != null) {
          val routeData = LocationDetails(latitude, longitude)
          if (checkInsideArea(routeData)) {
            routeList.add(routeData)
          }
        }

      }
    }

    return routeList
  }

  fun checkInsideArea(newLocation: LocationDetails): Boolean {
    val km = radius / 1000
    val kx = cos(Math.PI * center.latitude / 180) * 111
    val dx = abs(center.longitude - newLocation.longitude) * kx
    val dy = abs(center.latitude - newLocation.latitude) * 111
    return sqrt(dx * dx + dy * dy) <= km
  }

  fun drawRoutesOnMap(googleMap: GoogleMap, selectedLocation: LocationDetails) {
    val routes = routesAroundLocation(googleMap, selectedLocation)
    for (route in routes) {
      googleMap.addMarker(
          MarkerOptions()
              .position(LatLng(route.latitude, route.longitude))
              .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
              .title("Route")
      )
    }
  }
}
