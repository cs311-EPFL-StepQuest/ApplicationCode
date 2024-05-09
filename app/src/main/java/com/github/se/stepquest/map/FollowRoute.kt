package com.github.se.stepquest.map

import android.annotation.SuppressLint
import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class FollowRoute() {
    @SuppressLint("PotentialBehaviorOverride")
    fun drawRouteDetail(googleMap: GoogleMap) {
        googleMap.setOnMarkerClickListener { clickedMarker ->
            val route = clickedMarker.tag as? RouteDetails
            route?.let { it ->
                val routeID = it.routeID
                val routedetail = it.routeDetails
                val routeUserID = it.userID
                val points = routedetail?.map { LatLng(it.latitude, it.longitude) }
                if (points != null) {
                    if (points.isNotEmpty()) {
                        // Add a red marker at the start point
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(points.first())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                .title("Start Point")
                        )

                        // Add a green marker at the end point if there are multiple points
                        if (points.size > 1) {
                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(points.last())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                    .title("End Point")
                            )

                            // Draw a polyline connecting all the points
                            googleMap.addPolyline(
                                PolylineOptions()
                                    .addAll(points)
                                    .color(Color.BLUE)
                                    .width(5f)
                            )
                        }
                    }
                }
            }
            true  // Return true to indicate that we have handled the event
        }
    }

}