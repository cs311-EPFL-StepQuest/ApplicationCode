package com.github.se.stepquest.map

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class StoreRoute {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
    }

    data class Route(
        val route: List<LocationDetails>?,
        val checkpoints: List<String>? //Change to the correct checkpoints data type
    )

    data class GlobalRoute(
        val route: List<LocationDetails>?,
        val checkpoints: List<String>?, //Change to the correct checkpoints data type
        val userid: String
    )

    fun addRoute(route: List<LocationDetails>?, checkpoints: List<String>?) {
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            val newroute = Route(route, checkpoints)
            val globalroute= GlobalRoute(route, checkpoints, userId)
            // Generate a unique route ID
            val routeId = database.reference.child("routes").push().key.toString()
            println("Route ID: $routeId")
            val globalRouteRef = database.reference.child("routes").child(routeId)
            val routeRef = database.reference.child("users").child(userId).child("new_route").child(routeId)

            globalRouteRef.setValue(globalroute)
            routeRef.setValue(newroute)
        }
    }

}