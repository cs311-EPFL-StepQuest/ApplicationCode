package com.github.se.stepquest.map
// Map.kt

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.se.stepquest.R
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings

@Composable
fun Map() {
  // The gestures are still enabled, but the zoom controls are hidden
  var uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = false)) }
  var showProgression by remember { mutableStateOf(false) }
  var routeLength by rememberSaveable { mutableFloatStateOf(0f) }
  var numCheckpoints by rememberSaveable { mutableIntStateOf(0) }

  GoogleMap(modifier = Modifier.fillMaxSize().testTag("GoogleMap"), uiSettings = uiSettings)

  // Button to open route progression
  Box(
      modifier = Modifier.fillMaxSize().padding(bottom = 20.dp, end = 15.dp),
      contentAlignment = Alignment.BottomEnd) {
        IconButton(onClick = { showProgression = true }, modifier = Modifier.size(60.dp)) {
          Image(
              painter = painterResource(R.drawable.end_route),
              contentDescription = "End Route",
          )
        }
      }

  // Open the progression screen
  if (showProgression) {
    RouteProgression(onDismiss = { showProgression = false }, routeLength, numCheckpoints)
  }
}

@Preview
@Composable
fun PreviewMap() {
  Map()
}
