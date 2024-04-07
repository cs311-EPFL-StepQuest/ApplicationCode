package com.github.se.stepquest.map
// Map.kt

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.github.se.stepquest.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.model.cameraPosition

@Composable
fun Map(locationViewModel: LocationViewModel) {
  //  println("Here in Map")
  val context = LocalContext.current

  val launcherMultiplePermissions =
      rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
          permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        if (areGranted) {
          println("Permission true")
            locationViewModel.locationRequired.value = true
            locationViewModel.startLocationUpdates(context as ComponentActivity)
          Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
          println("Permission false")
          Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
      }

  val permissions =
      arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

  val map = remember { mutableStateOf<GoogleMap?>(null) }

  Box(modifier = Modifier.fillMaxSize().testTag("MapScreen")) {
      AndroidView(factory = { context ->
          MapView(context).apply {
              onCreate(null) // Lifecycle integration
              // Get the GoogleMap asynchronously
              getMapAsync { googleMap ->
                  map.value = googleMap
                  initMap(map.value!!)
              }
          }
      }, modifier = Modifier.fillMaxSize(),)


      val locationUpdated by locationViewModel.locationUpdated.observeAsState()
      LaunchedEffect(locationUpdated) {
          if (locationUpdated == true) {
              // Update the map content
              updateMap(map.value!!, locationViewModel)
              locationViewModel.locationUpdated.value = false
          }
      }

    // Button for creating a route
    FloatingActionButton(
        onClick = {
          if (permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
          }) {
            // Get the location
              locationViewModel.startLocationUpdates(context as ComponentActivity)
            println("Permission granted")
          } else {
            println("Ask Permission")
            launcherMultiplePermissions.launch(permissions)
          }
        },
        modifier =
            Modifier.padding(16.dp).align(Alignment.BottomStart).testTag("createRouteButton")) {
          Image(
              painter = painterResource(id = R.drawable.addbutton),
              contentDescription = "image description",
              contentScale = ContentScale.None)
        }
  }
}



fun updateMap(googleMap: GoogleMap, locationViewModel: LocationViewModel) {
    val allocations = locationViewModel.getAllocations() ?: return
//    println("all locations in map: $allocations")
    if (allocations.size == 1) {
        // Add marker for the only allocation
        googleMap.addMarker(MarkerOptions().position(allocations.first().toLatLng()))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(allocations.first().toLatLng(), 20f))
    } else {
        // Draw polyline connecting the last two allocations
//        println("Drawing polyline")
        val lastAllocation = allocations.last()
        val secondLastAllocation = allocations[allocations.size - 2]
        val polylineOptions = PolylineOptions().apply {
            color(Color.BLUE)
            width(10f)
            add(lastAllocation.toLatLng())
            add(secondLastAllocation.toLatLng())
        }
        googleMap.addPolyline(polylineOptions)
    }
    }


// Extension function to convert LocationDetails to LatLng
fun LocationDetails.toLatLng(): LatLng {
    return LatLng(latitude, longitude)
}

fun initMap(googleMap: GoogleMap){
//    println("Initiate map")
    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID)
    googleMap.uiSettings.isZoomControlsEnabled = true
}

// @Preview
// @Composable
// fun createRouteButtonPreview() {
//    createRouteButton()
// }
