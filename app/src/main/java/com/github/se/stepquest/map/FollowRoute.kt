package com.github.se.stepquest.map

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class FollowRoute() {

    var userOnRoute = MutableLiveData<Boolean>()
    // Define a Job to manage the coroutine
    private var checkRouteJob: Job? = null
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

        trackpoints.add(route.last())
        return trackpoints
    }

    fun checkIfOnRoute(
        locationViewModel: LocationViewModel,
        trackpoints: List<LocationDetails>,
        context: Context,
        threshold_onRoute: Double = 30.0,
        threshold_arrived: Double = 5.0,
        threshold_outRoute: Double = 50.0
    ) {
        // Cancel any existing job before starting a new one
        checkRouteJob?.cancel()

        // Launch a new coroutine in the background
        checkRouteJob =
            CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
                var remainingTrackpoints = trackpoints
                while (remainingTrackpoints.isNotEmpty() &&
                    isActive
                ) { // Check if the coroutine is still active
                    val trackpoint = remainingTrackpoints[0]
                    val finish_point= trackpoints.last()
                    val currentLocation = locationViewModel.currentLocation.value
                    if (currentLocation == null) {
                        withContext(Dispatchers.Main) {
                            AlertDialog.Builder(context)
                                .apply {
                                    setTitle("Finish route") // Set the title of the dialog to the checkpoint title
                                    // put image here
                                    setPositiveButton("OK") { dialog, which -> dialog.dismiss()
                                    setMessage("You have reached the finish point. Congratulation!")}
                                    // If you have an image and want to display it, consider using a custom layout or
                                    // fetching the image asynchronously
                                }
                                .create()
                                .show()
                            Log.d("FollowRoute", "Current location is null, stopping the coroutine")
                            followingRoute.postValue(false)
                        }
                    } else {
                        val distance = calculateDistance(trackpoint, currentLocation)
                        Log.d("FollowRoute","Distance: $distance\n")
                        val finish_distance = calculateDistance(finish_point, currentLocation)
                        if (finish_distance <= threshold_arrived) {
                            //check reach final point or not
                            withContext(Dispatchers.Main) {
                                userOnRoute.value = true
                                Log.d("FollowRoute", "You have reached the finish point. Congrat")
                            }
                        }
                        else{
                            // If the distance is less than 5 meters, the user has passed this point
                            if (distance <= threshold_arrived) {
                                withContext(Dispatchers.Main) {
                                    userOnRoute.value = true
                                    Log.d("FollowRoute", "You have reached the next point")
                                }
                                remainingTrackpoints = remainingTrackpoints.drop(1)
                            }
                            else if (distance <= threshold_onRoute && distance > threshold_arrived) {
                                withContext(Dispatchers.Main) {
                                    userOnRoute.postValue(true)
                                    Log.d("FollowRoute", "You are on the route")
                                }
                            }
                            else if (distance <= threshold_outRoute && distance > threshold_onRoute) {
                                withContext(Dispatchers.Main) {
                                    userOnRoute.postValue(false)
                                    Toast.makeText(context, "Warning: You are off the route, please go back to route.", Toast.LENGTH_LONG).show()
                                    Log.d("FollowRoute", "Warning: You are off the route")
                                }
                            }
                            else if (distance > threshold_outRoute) {
                                withContext(Dispatchers.Main) {
                                    userOnRoute.postValue(false)
                                    AlertDialog.Builder(context)
                                        .apply {
                                            setTitle("Finish route") // Set the title of the dialog to the checkpoint title
                                            // put image here
                                            setPositiveButton("OK") { dialog, which -> dialog.dismiss()
                                                setMessage("You are off the route. Stop following the route. Bye!")}
                                            // If you have an image and want to display it, consider using a custom layout or
                                            // fetching the image asynchronously
                                        }
                                        .create()
                                        .show()
                                    Log.d("FollowRoute", "You are off the route, Bye")
                                    followingRoute.postValue(false)
                                }
                            }
                        }
                    }
                    // Optionally, add a delay to prevent continuous checking
                    delay(1000)
                }
            }
    }

    // Function to stop the checkIfOnRoute coroutine
    fun stopCheckIfOnRoute() {
        checkRouteJob?.cancel()
    }
}
