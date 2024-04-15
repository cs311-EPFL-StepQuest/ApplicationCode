package com.github.se.stepquest.map
// Map.kt
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.GoogleMap

@Composable
fun Map() {
  //  println("Here in Map")
  GoogleMap(
      modifier = Modifier.fillMaxSize(),
  ) {}
}
