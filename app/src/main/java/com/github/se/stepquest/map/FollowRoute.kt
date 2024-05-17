package com.github.se.stepquest.map


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class FollowRoute() {
  var followingRoute = MutableLiveData<Boolean>()

  init {
    followingRoute.postValue(false)
  }

  @SuppressLint("PotentialBehaviorOverride")
  fun drawRouteDetail(googleMap: GoogleMap, context: Context) {
    googleMap.setOnMarkerClickListener { clickedMarker ->
      if (clickedMarker.title == "Route") {

        val route = clickedMarker.tag as? RouteDetails
        route?.let { it ->
          val routeID = it.routeID
          val routedetail = it.routeDetails
          val routeUserID = it.userID
          val checkpoints = it.checkpoints
          val points = routedetail?.map { LatLng(it.latitude, it.longitude) }
          if (points != null) {
            followingRoute.postValue(true)
            // clean up the map
            googleMap.clear()
            if (points.isNotEmpty()) {
              // Add a red marker at the start point
              googleMap.addMarker(
                  MarkerOptions()
                      .position(points.first())
                      .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                      .title("Start Point"))

              // Add a green marker at the end point if there are multiple points
              if (points.size > 1) {
                googleMap.addMarker(
                    MarkerOptions()
                        .position(points.last())
                        .icon(
                            BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_GREEN))
                        .title("End Point"))

                // Draw a polyline connecting all the points
                googleMap.addPolyline(PolylineOptions().addAll(points).color(Color.BLUE).width(5f))
              }
            }
          }

          // Add checkpoints to the map
          checkpoints?.forEach { checkpoint ->
            googleMap
                .addMarker(
                    MarkerOptions()
                        .position(
                            LatLng(checkpoint.location.latitude, checkpoint.location.longitude))
                        .icon(
                            BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_ORANGE))
                        .title("Checkpoint"))
                ?.tag = checkpoint
          }
        }
      } else if (clickedMarker.title == "Checkpoint") {
        // Handle checkpoint click, show image and title
        val checkpoint = clickedMarker.tag as? Checkpoint
        checkpoint?.let {
          AlertDialog.Builder(context)
              .apply {
                setTitle(it.name) // Set the title of the dialog to the checkpoint title
                // put image here
                setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
                // If you have an image and want to display it, consider using a custom layout or
                // fetching the image asynchronously
              }
              .create()
              .show()
        }
      }
      true // Return true to indicate that we have handled the event
    }
  }

    // taking the points on the route with the interval of 20 meters
    fun createTrackpoint(
        route: List<LocationDetails>,
        interval: Double = 20.0
    ): List<LocationDetails> {
        val trackpoints = mutableListOf<LocationDetails>()
        var currentLocation = route[0]
        var totaldistance = 0.0
        for (i in 1 until route.size) {
            val distance = calculateDistance(currentLocation, route[i])
            totaldistance += distance
            if (totaldistance >= interval) {
                trackpoints.add(route[i])
                totaldistance = 0.0
            }
            currentLocation = route[i]
        }
        return trackpoints
    }
}
