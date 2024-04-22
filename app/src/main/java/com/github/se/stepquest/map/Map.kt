package com.github.se.stepquest.map
// Map.kt
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Preview
@Composable
fun MyComposablePreview() {
  Map() // Call your composable function here
}
