package com.github.se.stepquest.map
// Map.kt
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker
import com.github.se.stepquest.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.flow.MutableStateFlow

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun Map(locationViewModel: LocationViewModel) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var checkpointTitle by remember { mutableStateOf("") }

    // Instantiate all necessary variables to take pictures
    val images = remember {MutableStateFlow<List<ImageBitmap>>(emptyList())}
    var photoFile = getPhotoFile(context)
    val fileProvider =
        FileProvider.getUriForFile(context, "com.github.se.stepquest.map.fileprovider", photoFile)
    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    takePicture.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
    val resultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result
            ->
            if (result.resultCode == Activity.RESULT_OK) {
                val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
                images.value += takenImage.asImageBitmap()
            }
        }
    var isPictureTaken by remember { mutableStateOf(false) }

    val launcherMultiplePermissions =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                permissionsMap ->
            val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
            println("launcherMultiplePermissions")
            if (areGranted) {
                println("Permission Granted")
                locationViewModel.locationRequired.value = true
                locationViewModel.startLocationUpdates(context as ComponentActivity)
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                println("Permission Denied")
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    val permissions =
        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

    val map = remember { mutableStateOf<GoogleMap?>(null) }
    val locationUpdated by locationViewModel.locationUpdated.observeAsState()

    Scaffold(
        content = {
            Box(modifier = Modifier.fillMaxSize().testTag("MapScreen")) {
                // Google Map
                AndroidView(
                    factory = { context ->
                        MapView(context).apply {
                            onCreate(null) // Lifecycle integration
                            // Get the GoogleMap asynchronously
                            getMapAsync { googleMap ->
                                map.value = googleMap
                                initMap(map.value!!)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize().testTag("GoogleMap"))

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
                        locationPermission(
                            locationViewModel, context, launcherMultiplePermissions, permissions)
                    },
                    modifier =
                    Modifier.size(85.dp)
                        .padding(16.dp)
                        .align(Alignment.BottomEnd)
                        .offset(y = (-150).dp)
                        .testTag("createRouteButton")) {
                    Image(
                        painter = painterResource(id = R.drawable.addbutton),
                        contentDescription = "image description",
                        contentScale = ContentScale.None)
                }

                // Check point button
                FloatingActionButton(
                    onClick = { showDialog = true },
                    modifier =
                    Modifier.padding(16.dp)
                        .align(Alignment.BottomEnd)
                        .offset(y = (-96).dp)
                        .size(48.dp)) {
                    Box(
                        modifier = Modifier.size(48.dp).background(Color(0xff00b3ff), CircleShape),
                        contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(R.drawable.map_marker),
                            contentDescription = "Add checkpoint",
                            tint = Color.Red)
                    }
                }
            }
        },
        floatingActionButton = {
            if (showDialog) {
                AlertDialog(
                    shape = RoundedCornerShape(16.dp),
                    onDismissRequest = { showDialog = false },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "New Checkpoint",
                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                                modifier = Modifier.weight(1f))
                            IconButton(onClick = { showDialog = false }, modifier = Modifier.size(36.dp)) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Black)
                            }
                        }
                    },
                    text = {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Checkpoint name",
                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                                modifier = Modifier.padding(bottom = 8.dp))
                            TextField(
                                value = checkpointTitle,
                                shape = RoundedCornerShape(8.dp),
                                onValueChange = { checkpointTitle = it },
                                label = { Text("Name:") },
                                modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.height(36.dp))
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                Text(
                                    text = "Take a picture",
                                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp))
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            // Button to take picture
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                IconButton(
                                    onClick = {
                                        isPictureTaken = true
                                        resultLauncher.launch(takePicture)

                                    }) {
                                    Icon(
                                        painterResource(R.drawable.camera_icon),
                                        contentDescription = "camera_icon",
                                        modifier = Modifier.size(60.dp))
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(48.dp)) {
                            Button(
                                onClick = {
                                    // ADD HERE CODE FOR ADDING CHECKPOINTS, INPUT TITLE STORED IN title
                                    val title = checkpointTitle
                                    showDialog = false
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff00b3ff)),
                                modifier = Modifier.width(150.dp).align(Alignment.Center)) {
                                Text(
                                    "Confirm",
                                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                                    color = Color.White)
                            }
                        }
                    },
                    dismissButton = { Spacer(modifier = Modifier.height(36.dp)) },
                    modifier = Modifier.width(300.dp))
            }
        })
}

fun updateMap(googleMap: GoogleMap, locationViewModel: LocationViewModel) {
    val allocations = locationViewModel.getAllocations() ?: return
    if (allocations.size == 1) {
        // Add marker for the only allocation
        googleMap.addMarker(MarkerOptions().position(allocations.first().toLatLng()))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(allocations.first().toLatLng(), 20f))
    } else {
        // Draw polyline connecting the last two allocations
        val lastAllocation = allocations.last()
        val secondLastAllocation = allocations[allocations.size - 2]
        val polylineOptions =
            PolylineOptions().apply {
                color(android.graphics.Color.BLUE)
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

fun initMap(googleMap: GoogleMap) {
    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID)
    googleMap.uiSettings.isZoomControlsEnabled = true
}

fun locationPermission(
    locationViewModel: LocationViewModel,
    context: Context,
    launcherMultiplePermissions: ActivityResultLauncher<Array<String>>,
    permissions: Array<String>
) {
    if (permissions.all {
            PermissionChecker.checkSelfPermission(context, it) == PermissionChecker.PERMISSION_GRANTED
        }) {
        println("Permission successful")
        // Get the location
        locationViewModel.startLocationUpdates(context)
    } else {
        println("Ask Permission")
        launcherMultiplePermissions.launch(permissions)
    }
}
