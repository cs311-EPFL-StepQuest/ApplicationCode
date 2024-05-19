package com.github.se.stepquest.map

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.MutableLiveData
import com.github.se.stepquest.R
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
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.dialog_image, null)

            val imageView: ImageView = view.findViewById(R.id.dialog_image)
            BitmapFactory.decodeStream(it.image.inputStream()).also { bitmap ->
              imageView.setImageBitmap(bitmap)
            }

            builder.setView(view)
                .setTitle(it.name) // Set the title of the dialog to the checkpoint title
                .setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
                .create()
                .show()
        }
      }
      true // Return true to indicate that we have handled the event
    }
  }
}
