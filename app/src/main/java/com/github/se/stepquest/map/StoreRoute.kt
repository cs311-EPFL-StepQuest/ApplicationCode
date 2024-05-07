package com.github.se.stepquest.map

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class StoreRoute {
  private var firebaseAuth: FirebaseAuth
  private var database: FirebaseDatabase

  init {
    firebaseAuth = FirebaseAuth.getInstance()
    database = FirebaseDatabase.getInstance()
  }

  data class Route(
      val route: List<LocationDetails>?,
      val checkpoints: List<Checkpoint>? // Change to the correct checkpoints data type
  )

  data class GlobalRoute(
      val route: List<LocationDetails>?,
      val checkpoints: List<Checkpoint>?, // Change to the correct checkpoints data type
      val userid: String
  )

  fun getUserid(): String? {
    return firebaseAuth.currentUser?.uid
  }

  fun addRoute(userId: String?, route: List<LocationDetails>?, checkpoints: List<Checkpoint>?) {
    if (userId != null) {
      val newroute = Route(route, checkpoints)
      val globalroute = GlobalRoute(route, checkpoints, userId)
      val routeId = database.reference.child("routes").push().key.toString()
      val globalRouteRef = database.reference.child("routes").child(routeId)
      val routeRef =
          database.reference.child("users").child(userId).child("new_route").child(routeId)
      globalRouteRef.setValue(globalroute)
      routeRef.setValue(newroute)
    }
  }
}
