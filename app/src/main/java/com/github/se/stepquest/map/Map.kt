package com.github.se.stepquest.map
// Map.kt

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.github.se.stepquest.R
import com.google.maps.android.compose.GoogleMap
import android.widget.Toast
import androidx.core.content.ContextCompat

@Composable
fun Map(viewModel: LocationViewModel) {
  //  println("Here in Map")
  val context = LocalContext.current


    MapContent(viewModel, context)

}

@Composable
fun MapContent(viewModel: LocationViewModel, context: Context) {

    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            println("Permission true")
//            locationRequired = true
//            startLocationUpdates()

        } else {
            println("Permission false")
        }
    }

    val permissions =
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

  Box(modifier = Modifier.fillMaxSize()) {
      //Google map
      GoogleMap(
          modifier = Modifier.fillMaxSize(),
      ) {}

      //Button for creating a route
    FloatingActionButton(
        onClick = {
//          viewModel.checkLocationPermission(context as ComponentActivity)
            if (permissions.all {
                    ContextCompat.checkSelfPermission(
                        context,
                        it
                    ) == PackageManager.PERMISSION_GRANTED
                }) {
                // Get the location
//                startLocationUpdates()
                println("Permission granted")
            } else {
                println("Ask Permission")
                launcherMultiplePermissions.launch(permissions)
            }
        },
        modifier =
            Modifier.padding(16.dp).align(Alignment.BottomEnd).testTag("createRouteButton")) {
          Image(
              painter = painterResource(id = R.drawable.addbutton),
              contentDescription = "image description",
              contentScale = ContentScale.None)
        }
  }
}

// @Preview
// @Composable
// fun createRouteButtonPreview() {
//    createRouteButton()
// }
