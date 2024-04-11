package com.github.se.stepquest.map
// Map.kt
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.google.maps.android.compose.GoogleMap

@Composable
fun Map() {
  GoogleMap(modifier = Modifier.fillMaxSize().testTag("GoogleMap"),)
}
