package com.github.se.stepquest.map

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sqrt

class LocationArea {
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
  ): List<StoreRoute.Route> {
    // Retrieve all routes from the database and check
    // if the starting point of the route is within the circle
    val routes = database.reference.child("routes")
    val routeList = mutableListOf<StoreRoute.Route>()

    routes.get().addOnSuccessListener { snapshot ->
      for (route in snapshot.children) {
        val routeData = route.getValue(StoreRoute.Route::class.java)
        if (routeData != null) {
          if (checkInsideArea(routeData.route?.get(0)!!)) {
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
}
