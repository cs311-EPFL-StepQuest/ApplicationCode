package com.github.se.stepquest.map

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.CameraUpdateFactory
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
            //clean up the map
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

  fun fakeRouteDetail(googleMap: GoogleMap) {
    val points =
        listOf(
            LocationDetails(37.4220, -122.0841),
            LocationDetails(37.4230, -122.0851),
            LocationDetails(37.4240, -122.0861),
            LocationDetails(37.4250, -122.0871))
    val checkpoins =
        listOf(
            Checkpoint("checkpoint1", LocationDetails(37.4230, -122.0851)),
            Checkpoint("checkpoint2", LocationDetails(37.4240, -122.0861)),
        )

    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(points.first().toLatLng(), 15f))
    val fakeroutedetail = RouteDetails("fakeRoute", points, checkpoins, "fakeUserID")
    val marker =
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(points.first().latitude, points.first().longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .title("Route"))
    marker?.tag = fakeroutedetail
  }
}
