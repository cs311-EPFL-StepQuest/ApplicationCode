package com.github.se.stepquest.map
// Map.kt
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.github.se.stepquest.R
import com.google.maps.android.compose.GoogleMap

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Map() {
  var showDialog by remember { mutableStateOf(false) }
  var checkpointTitle by remember { mutableStateOf("") }

  Scaffold(
      content = {
        Box(modifier = Modifier.fillMaxSize()) {
          GoogleMap(
              modifier = Modifier.fillMaxSize().testTag("GoogleMap"),
          )
          FloatingActionButton(
              onClick = { showDialog = true },
              modifier =
                  Modifier.padding(16.dp)
                      .align(Alignment.BottomEnd)
                      .offset(y = (-96).dp)
                      .size(48.dp)) {
                Box(
                    modifier =
                        Modifier.size(48.dp) // Adjust the size of the icon container
                            .background(Color.Blue, CircleShape), // Blue background
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
              onDismissRequest = { showDialog = false },
              title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text("New Checkpoint", modifier = Modifier.weight(1f)) // Title "New Checkpoint"
                  IconButton(onClick = { showDialog = false }, modifier = Modifier.size(36.dp)) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Black // Black cancel button
                        )
                  }
                }
              },
              text = {
                Column(modifier = Modifier.padding(16.dp)) {
                  Text(
                      "Checkpoint name",
                      modifier = Modifier.padding(bottom = 8.dp)) // Prompt "Checkpoint name"
                  TextField(
                      value = checkpointTitle,
                      onValueChange = { checkpointTitle = it },
                      label = { Text("Title") },
                      modifier = Modifier.fillMaxWidth())
                }
              },
              confirmButton = {
                Box(
                    modifier =
                        Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(48.dp) // Set the height of the button
                    ) {
                      Button(
                          onClick = {
                            // Here you can handle adding the checkpoint to the map
                            val title = checkpointTitle
                            showDialog = false
                          },
                          modifier =
                              Modifier.fillMaxWidth()
                                  .width(50.dp) // Set the width of the button
                                  .align(Alignment.Center)) {
                            Text("Confirm", color = Color.White) // Blue confirm button
                      }
                    }
              },
              dismissButton = {
                // Empty composable to maintain space for dismiss button
                Spacer(modifier = Modifier.height(36.dp))
              },
              modifier = Modifier.width(300.dp) // Set width of the AlertDialog
              )
        }
      })
}
