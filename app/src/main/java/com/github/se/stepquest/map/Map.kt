package com.github.se.stepquest.map
// Map.kt
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun Map() {
//    val todos by overviewViewModel.toDoList.collectAsState()
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
    ) {}
}
